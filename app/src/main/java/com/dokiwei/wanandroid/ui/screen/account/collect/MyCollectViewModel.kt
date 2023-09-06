package com.dokiwei.wanandroid.ui.screen.account.collect

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dokiwei.wanandroid.model.util.ToastAndLogcatUtil
import com.dokiwei.wanandroid.network.impl.CollectApiImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

/**
 * @author DokiWei
 * @date 2023/7/19 21:08
 */

class MyCollectViewModel : ViewModel() {
    private val collectApiImpl = CollectApiImpl()

    private val _state = MutableStateFlow(MyCollectState())
    val state = _state

    //初始化
    init {
        viewModelScope.launch { getMyLike() }
    }

    fun dispatch(intent: MyCollectIntent) {
        when (intent) {
            is MyCollectIntent.Refresh -> onRefresh()
            is MyCollectIntent.LoadMore -> loadMore()
            is MyCollectIntent.UpdateScrollToTop -> handleAction(
                MyCollectAction.UpdateScrollToTop(
                    intent.boolean
                )
            )
        }
    }

    private fun handleAction(action: MyCollectAction) {
        when (action) {
            is MyCollectAction.Refresh -> {
                viewModelScope.launch {
                    _state.value = _state.value.copy(isRefreshing = true)
                    runBlocking(Dispatchers.IO) { getMyLike() }
                    _state.value = _state.value.copy(isRefreshing = false)
                }
            }

            is MyCollectAction.LoadMore -> {
                _state.value = _state.value.copy(nowPageIndex = _state.value.nowPageIndex + 1)
                viewModelScope.launch {
                    _state.value = _state.value.copy(isLoadingMore = true)
                    runBlocking(Dispatchers.IO) {
                        getMyLike(_state.value.nowPageIndex)
                    }
                    _state.value = _state.value.copy(isLoadingMore = false)
                }
            }

            is MyCollectAction.SetMyCollectList -> {
                if (action.page == 0) _state.value.myCollectList.clear()
                action.data?.let {
                    it.forEach { data ->
                        _state.value.myCollectList.add(data)
                    }
                }
            }

            is MyCollectAction.ShowToast -> _state.value = _state.value.copy(msg = action.msg)
            is MyCollectAction.OutputLogcat -> ToastAndLogcatUtil.log(
                "MyCollectAction", action.msg
            )

            is MyCollectAction.UpdateScrollToTop -> _state.value =
                _state.value.copy(scrollToTop = action.boolean)
        }
    }

    //刷新数据
    private fun onRefresh() {
        handleAction(MyCollectAction.Refresh)
    }

    private fun loadMore() {
        handleAction(MyCollectAction.LoadMore)
    }

    //获取我的收藏
    private suspend fun getMyLike(page: Int = 0) {
        viewModelScope.launch {
            val result = collectApiImpl.myLike(page)
            if (result.isSuccess) handleAction(
                MyCollectAction.SetMyCollectList(
                    page,
                    result.getOrNull()
                )
            )
            else handleAction(
                MyCollectAction.OutputLogcat(
                    msg = "获取我的收藏失败${
                        result.exceptionOrNull().toString()
                    }"
                )
            )
        }
    }

}