package com.dokiwei.wanandroid.ui.screen.other.sharearticles

import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dokiwei.wanandroid.model.apidata.ArticleData
import com.dokiwei.wanandroid.model.apidata.CoinInfoData
import com.dokiwei.wanandroid.model.util.ToastAndLogcatUtil
import com.dokiwei.wanandroid.network.impl.AccountApiImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

/**
 * @author DokiWei
 * @date 2023/8/21 20:19
 */
data class ShareArticlesData(
    val coinInfoData: CoinInfoData? = null,
    val userArticleData: SnapshotStateList<ArticleData> = SnapshotStateList(),
    val nowPageIndex: Int = 1,
    val isRefreshing: Boolean = false,
    val isLoadingMore: Boolean = false,
    val scrollToTop: Boolean = false
)

sealed class ShareArticlesIntent {
    data class GetUserArticles(val userId: Int) : ShareArticlesIntent()
    data class Refresh(val userId: Int) : ShareArticlesIntent()
    data class LoadMore(val userId: Int) : ShareArticlesIntent()
    data class UpdateScrollToTop(val boolean: Boolean) : ShareArticlesIntent()
}

class ShareArticlesViewModel : ViewModel() {
    private val accountApiImpl = AccountApiImpl()

    private val _state = MutableStateFlow(ShareArticlesData())
    val state = _state

    fun dispatch(intent: ShareArticlesIntent) {
        when (intent) {
            is ShareArticlesIntent.GetUserArticles -> viewModelScope.launch(Dispatchers.IO) {
                getUserArticles(
                    userId = intent.userId
                )
            }

            is ShareArticlesIntent.Refresh -> viewModelScope.launch {
                _state.value = _state.value.copy(isRefreshing = true)
                runBlocking(Dispatchers.IO) { getUserArticles(userId = intent.userId) }
                _state.value = _state.value.copy(isRefreshing = false)
            }

            is ShareArticlesIntent.LoadMore -> {
                _state.value = _state.value.copy(nowPageIndex = _state.value.nowPageIndex + 1)
                viewModelScope.launch {
                    _state.value = _state.value.copy(isLoadingMore = true)
                    runBlocking(Dispatchers.IO) {
                        getUserArticles(_state.value.nowPageIndex, intent.userId)
                    }
                    _state.value = _state.value.copy(isLoadingMore = false)
                }
            }

            is ShareArticlesIntent.UpdateScrollToTop -> _state.value =
                _state.value.copy(scrollToTop = intent.boolean)
        }
    }

    private suspend fun getUserArticles(page: Int = 1, userId: Int) {
        val result = accountApiImpl.getUserArticles(page, userId)
        result.getOrNull()?.let {
            _state.value = _state.value.copy(coinInfoData = it.coinInfo)
            if (page == 1) _state.value.userArticleData.clear()
            it.shareArticles.forEach { data -> _state.value.userArticleData.add(data) }
        }
        result.exceptionOrNull()?.let { ToastAndLogcatUtil.log(msg = it.toString()) }
    }

}