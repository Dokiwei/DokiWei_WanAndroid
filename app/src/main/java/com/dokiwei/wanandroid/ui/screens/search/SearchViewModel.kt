package com.dokiwei.wanandroid.ui.screens.search

import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dokiwei.wanandroid.bean.ArticleBean
import com.dokiwei.wanandroid.bean.HotKeyBean
import com.dokiwei.wanandroid.network.repository.SearchRepo
import com.dokiwei.wanandroid.util.ToastAndLogcatUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

/**
 * @author DokiWei
 * @date 2023/7/27 19:18
 */
data class SearchState(
    val isRefreshing: Boolean = false,
    val isLoadingMore: Boolean = false,
    val msg: String = "",
    val searchResult: SnapshotStateList<ArticleBean> = SnapshotStateList(),
    val nowPageIndex: Int = 0,
    val scrollToTop: Boolean = false,
    val hotKeys: List<HotKeyBean> = emptyList(),
    val isShowResult: Boolean = false
)

sealed class SearchIntent {
    data class Refresh(val k: String) : SearchIntent()
    data class LoadMore(val k: String) : SearchIntent()
    data class GetSearchResult(val page: Int = 0, val k: String) : SearchIntent()
    data class UpdateScrollToTop(val boolean: Boolean) : SearchIntent()
    object RevertToTheDefaultShowResult : SearchIntent()
    object RevertToTheDefaultPageIndex : SearchIntent()
}

sealed class SearchAction {
    data class ShowToast(val msg: String) : SearchAction()
    data class OutputLogcat(val tag: String = "SearchAction", val msg: String, val level: Int = 0) :
        SearchAction()

    data class SetSearchResult(val page: Int = 0, val dataList: List<ArticleBean>?) : SearchAction()
    data class LoadMore(val k: String) : SearchAction()
    data class Refresh(val k: String) : SearchAction()
    data class UpdateScrollToTop(val boolean: Boolean) : SearchAction()
    object RevertToTheDefaultShowResult : SearchAction()
    object RevertToTheDefaultPageIndex : SearchAction()
    data class SetHotKey(val data: List<HotKeyBean>) : SearchAction()
}

class SearchViewModel : ViewModel() {
    private val searchRepo = SearchRepo()

    private val _state = MutableStateFlow(SearchState())
    val state = _state

    init {
        viewModelScope.launch(Dispatchers.IO) { getHotKey() }
    }

    fun dispatch(intent: SearchIntent) {
        when (intent) {
            is SearchIntent.Refresh -> onRefresh(intent.k)
            is SearchIntent.LoadMore -> loadMore(intent.k)
            is SearchIntent.GetSearchResult -> viewModelScope.launch(Dispatchers.IO) {
                search(
                    intent.page,
                    intent.k
                )
            }

            is SearchIntent.RevertToTheDefaultPageIndex -> handleAction(SearchAction.RevertToTheDefaultPageIndex)
            is SearchIntent.UpdateScrollToTop -> handleAction(
                SearchAction.UpdateScrollToTop(
                    intent.boolean
                )
            )

            is SearchIntent.RevertToTheDefaultShowResult -> handleAction(SearchAction.RevertToTheDefaultShowResult)
        }
    }

    private fun handleAction(action: SearchAction) {
        when (action) {
            is SearchAction.SetHotKey -> _state.value = _state.value.copy(hotKeys = action.data)

            is SearchAction.RevertToTheDefaultPageIndex -> _state.value =
                _state.value.copy(nowPageIndex = 0)

            is SearchAction.OutputLogcat -> ToastAndLogcatUtil.log(
                action.tag, action.msg, action.level
            )

            is SearchAction.ShowToast -> _state.value = _state.value.copy(msg = action.msg)

            is SearchAction.SetSearchResult -> {
                if (action.page == 0) _state.value.searchResult.clear()
                action.dataList?.let { list ->
                    list.forEach {
                        _state.value.searchResult.add(it)
                    }
                }
                _state.value = _state.value.copy(isShowResult = true)
            }

            is SearchAction.UpdateScrollToTop -> _state.value =
                _state.value.copy(scrollToTop = action.boolean)

            is SearchAction.RevertToTheDefaultShowResult -> _state.value =
                _state.value.copy(isShowResult = false)

            is SearchAction.LoadMore -> {
                _state.value = _state.value.copy(nowPageIndex = _state.value.nowPageIndex + 1)
                viewModelScope.launch {
                    _state.value = _state.value.copy(isLoadingMore = true)
                    runBlocking(Dispatchers.IO) {
                        search(_state.value.nowPageIndex, action.k)
                    }
                    _state.value = _state.value.copy(isLoadingMore = false)
                }
            }

            is SearchAction.Refresh -> {
                viewModelScope.launch {
                    _state.value = _state.value.copy(isRefreshing = true)
                    runBlocking(Dispatchers.IO) {
                        search(k = action.k)
                    }
                    _state.value = _state.value.copy(isRefreshing = false)
                }
            }
        }
    }


    private fun onRefresh(k: String) {
        handleAction(SearchAction.Refresh(k))
    }

    private fun loadMore(k: String) {
        handleAction(SearchAction.LoadMore(k))
        handleAction(SearchAction.ShowToast("加载新内容"))
    }

    private suspend fun getHotKey() {
        val result = searchRepo.hotKey()
        if (result.isSuccess) handleAction(
            SearchAction.SetHotKey(
                result.getOrNull() ?: emptyList()
            )
        )
        else handleAction(
            SearchAction.OutputLogcat(
                msg = "获取热搜词失败:${
                    result.exceptionOrNull().toString()
                }"
            )
        )
    }

    private suspend fun search(page: Int = 0, k: String) {
        val result = searchRepo.search(page, k)
        if (result.isSuccess) handleAction(
            SearchAction.SetSearchResult(
                page, result.getOrNull()
            )
        )
        else handleAction(
            SearchAction.OutputLogcat(
                msg = result.exceptionOrNull().toString()
            )
        )
    }

}