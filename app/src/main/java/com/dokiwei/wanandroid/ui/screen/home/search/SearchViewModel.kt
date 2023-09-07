package com.dokiwei.wanandroid.ui.screen.home.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.dokiwei.wanandroid.data.base.BasePagingSource
import com.dokiwei.wanandroid.model.util.ToastAndLogcatUtil
import com.dokiwei.wanandroid.network.impl.HomeApiImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

/**
 * @author DokiWei
 * @date 2023/7/27 19:18
 */


class SearchViewModel : ViewModel() {
    private val homeApiImpl = HomeApiImpl()

    private val _state = MutableStateFlow(SearchState(data = searchResult()))
    val state = _state

    private fun searchResult(k: String = "") = Pager(config = PagingConfig(
        pageSize = 20, initialLoadSize = 40
    ), pagingSourceFactory = {
        BasePagingSource { HomeApiImpl().search(it, k) }
    }).flow.cachedIn(viewModelScope)

    init {
        viewModelScope.launch(Dispatchers.IO) { getHotKey() }
    }

    fun dispatch(intent: SearchIntent) {
        when (intent) {
            is SearchIntent.ChangeShowResult -> _state.value =
                _state.value.copy(isShowResult = intent.boolean)

            is SearchIntent.GetSearchResult -> _state.value =
                _state.value.copy(data = searchResult(intent.searchKey))
        }
    }

    private suspend fun getHotKey() {
        val result = homeApiImpl.getHotKey()
        result.getOrNull()?.let {
            _state.value = _state.value.copy(hotKeys = result.getOrNull() ?: emptyList())
        }
        result.exceptionOrNull()?.let {
            ToastAndLogcatUtil.log("SearchViewModel", "获取hotkey失败:$it")
        }
    }

}