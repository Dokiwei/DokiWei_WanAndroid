package com.dokiwei.wanandroid.ui.screens.home.content.qa

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dokiwei.wanandroid.network.impl.HomeApiImpl
import com.dokiwei.wanandroid.util.ToastAndLogcatUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

/**
 * @author DokiWei
 * @date 2023/7/14 23:15
 */
class QaViewModel : ViewModel() {
    private val homeApiImpl = HomeApiImpl()

    private val _state = MutableStateFlow(QaState())
    val state = _state

    fun dispatch(intent: QaIntent) {
        when (intent) {
            is QaIntent.Refresh -> onRefresh()
            is QaIntent.LoadMore -> loadMore()
            is QaIntent.GetArticleData -> viewModelScope.launch { getQaList(intent.page) }
        }
    }

    private fun handleAction(action: QaAction) {
        when (action) {
            is QaAction.OutputLogcat -> ToastAndLogcatUtil.log(
                action.tag, action.msg, action.level
            )

            is QaAction.ShowToast -> _state.value = _state.value.copy(msg = action.msg)

            is QaAction.SetArticleData -> {
                if (action.page == 1) _state.value.qaList.clear()
                action.dataList?.let { list ->
                    list.forEach {
                        _state.value.qaList.add(it)
                    }
                }
            }

            is QaAction.LoadMore -> {
                _state.value = _state.value.copy(nowPageIndex = _state.value.nowPageIndex + 1)
                viewModelScope.launch(Dispatchers.IO) {
                    _state.value = _state.value.copy(isLoadingMore = true)
                    runBlocking(Dispatchers.IO) {
                        getQaList(_state.value.nowPageIndex)
                    }
                    _state.value = _state.value.copy(isLoadingMore = false)
                }
            }

            is QaAction.Refresh -> {
                viewModelScope.launch(Dispatchers.IO) {
                    _state.value = _state.value.copy(isRefreshing = true)
                    runBlocking(Dispatchers.IO) {
                        getQaList()
                    }
                    _state.value = _state.value.copy(isRefreshing = false)
                }
            }
        }
    }

    //初始化问答列表
    init {
        viewModelScope.launch { getQaList() }
    }

    //刷新列表
    private fun onRefresh() {
        handleAction(QaAction.Refresh)
    }

    //加载更多问答
    private fun loadMore() {
        handleAction(QaAction.LoadMore)
        handleAction(QaAction.ShowToast("加载新内容"))
    }

    //获取问答列表
    private suspend fun getQaList(page: Int = 1) {
        val result = homeApiImpl.getQa(page)
        if (result.isSuccess) handleAction(
            QaAction.SetArticleData(
                page, result.getOrNull()
            )
        )
        else handleAction(
            QaAction.OutputLogcat(
                msg = result.exceptionOrNull().toString()
            )
        )
    }



}