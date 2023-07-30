package com.dokiwei.wanandroid.ui.screens.home.content.square

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dokiwei.wanandroid.network.repository.UserArticleRepo
import com.dokiwei.wanandroid.util.ToastAndLogcatUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

/**
 * @author DokiWei
 * @date 2023/7/14 22:50
 */
class SquareViewModel : ViewModel() {
    private val userArticleRepo = UserArticleRepo()

    private val _state = MutableStateFlow(SquareState())
    val state = _state

    //初始化
    init {
        viewModelScope.launch { getUserArticleList() }
    }

    fun dispatch(intent: SquareIntent) {
        when (intent) {
            is SquareIntent.Refresh -> onRefresh()
            is SquareIntent.LoadMore -> loadMore()
        }
    }

    private fun handleAction(action: SquareAction) {
        when (action) {
            is SquareAction.OutputLogcat -> ToastAndLogcatUtil.log(
                action.tag, action.msg, action.level
            )

            is SquareAction.ShowToast -> _state.value = _state.value.copy(msg = action.msg)

            is SquareAction.SetArticleData -> {
                if (action.page == 1) _state.value.userArticleList.clear()
                action.dataList?.let { list ->
                    list.forEach {
                        _state.value.userArticleList.add(it)
                    }
                }
            }

            is SquareAction.LoadMore -> {
                _state.value = _state.value.copy(nowPageIndex = _state.value.nowPageIndex + 1)
                viewModelScope.launch {
                    _state.value = _state.value.copy(isLoadingMore = true)
                    runBlocking(Dispatchers.IO) {
                        getUserArticleList(_state.value.nowPageIndex)
                    }
                    _state.value = _state.value.copy(isLoadingMore = false)
                }
            }

            is SquareAction.Refresh -> {
                viewModelScope.launch {
                    _state.value = _state.value.copy(isRefreshing = true)
                    runBlocking(Dispatchers.IO) {
                        getUserArticleList()
                    }
                    _state.value = _state.value.copy(isRefreshing = false)
                }
            }
        }
    }

    //刷新列表
    private fun onRefresh() {
        handleAction(SquareAction.Refresh)
    }

    //加载更多问答
    private fun loadMore() {
        handleAction(SquareAction.LoadMore)
        handleAction(SquareAction.ShowToast("加载新内容"))
    }

    //获取广场文章
    private suspend fun getUserArticleList(page: Int = 0) {
        val result = userArticleRepo.getArticleList(page)
        if (result.isSuccess) handleAction(
            SquareAction.SetArticleData(
                page, result.getOrNull()
            )
        )
        else handleAction(
            SquareAction.OutputLogcat(
                msg = "获取广场文章失败:${result.exceptionOrNull().toString()}"
            )
        )
    }


}