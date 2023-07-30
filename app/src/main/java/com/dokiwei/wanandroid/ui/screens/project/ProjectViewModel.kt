package com.dokiwei.wanandroid.ui.screens.project

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dokiwei.wanandroid.bean.ProjectTabsBean
import com.dokiwei.wanandroid.network.repository.ProjectRepo
import com.dokiwei.wanandroid.util.ToastAndLogcatUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

/**
 * @author DokiWei
 * @date 2023/7/15 16:18
 */
class ProjectViewModel : ViewModel() {
    private val projectRepo = ProjectRepo()

    private val _state = MutableStateFlow(ProjectState())
    val state = _state

    //初始化
    init {
        viewModelScope.launch(Dispatchers.IO) {
            runBlocking(Dispatchers.IO) { getProjectTitleList() }
            getProjectList(id = _state.value.projectTabs[_state.value.selectedTabIndex].id)
        }
    }

    fun dispatch(intent: ProjectIntent) {
        when (intent) {
            is ProjectIntent.LoadMore -> loadMore()
            is ProjectIntent.Refresh -> onRefresh()
            is ProjectIntent.SelectedTab -> onTabSelected(intent.index)
            is ProjectIntent.UpdateTabExpanded -> onTabExpanded(intent.boolean)
            is ProjectIntent.UpdateScrollToTop -> handleAction(
                ProjectAction.UpdateScrollToTop(
                    intent.boolean
                )
            )
        }
    }

    private fun handleAction(action: ProjectAction) {
        when (action) {
            is ProjectAction.LoadMore -> {
                _state.value = _state.value.copy(nowPageIndex = _state.value.nowPageIndex + 1)
                viewModelScope.launch(Dispatchers.IO) {
                    _state.value = _state.value.copy(isLoadingMore = true)
                    runBlocking(Dispatchers.IO) {
                        getProjectList(
                            _state.value.nowPageIndex,
                            _state.value.projectTabs[_state.value.selectedTabIndex].id
                        )
                    }
                    _state.value = _state.value.copy(isLoadingMore = false)
                }
            }

            is ProjectAction.Refresh -> {
                viewModelScope.launch {
                    _state.value = _state.value.copy(isRefreshing = true)
                    runBlocking(Dispatchers.IO) { getProjectList(id = _state.value.projectTabs[_state.value.selectedTabIndex].id) }
                    _state.value = _state.value.copy(isRefreshing = false)
                }
            }

            is ProjectAction.SetArticleData -> {
                if (action.page == 0) _state.value.projectList.clear()
                action.dataList?.let { list ->
                    list.forEach {
                        _state.value.projectList.add(it)
                    }
                }
            }

            is ProjectAction.SetTabsData -> {
                action.dataList?.let { list ->
                    list.forEach {
                        _state.value.projectTabs.add(it)
                    }
                    val lastId = _state.value.projectTabs.removeLast().id
                    val lastElement = ProjectTabsBean(lastId, "最新项目")
                    _state.value.projectTabs.add(0, lastElement)
                }
            }

            is ProjectAction.OutputLogcat -> ToastAndLogcatUtil.log(
                action.tag, action.msg, action.level
            )

            is ProjectAction.ShowToast -> _state.value = _state.value.copy(msg = action.msg)
            is ProjectAction.TabSelected -> {
                viewModelScope.launch {
                    _state.value = _state.value.copy(selectedTabIndex = action.index)
                    getProjectList(id = _state.value.projectTabs[_state.value.selectedTabIndex].id)
                    if (_state.value.isTabExpand) _state.value =
                        _state.value.copy(isTabExpand = false)
                }
            }

            is ProjectAction.TabExpanded -> _state.value =
                _state.value.copy(isTabExpand = action.boolean)

            is ProjectAction.UpdateScrollToTop -> _state.value =
                _state.value.copy(scrollToTop = action.boolean)
        }
    }

    private fun onTabSelected(index: Int) {
        handleAction(ProjectAction.TabSelected(index))
    }

    private fun onTabExpanded(boolean: Boolean) {
        handleAction(ProjectAction.TabExpanded(boolean))
    }

    //刷新数据
    private fun onRefresh() {
        handleAction(ProjectAction.Refresh)
    }

    private fun loadMore() {
        handleAction(ProjectAction.LoadMore)
    }

    //获取项目tab
    private suspend fun getProjectTitleList() {
        val result = projectRepo.getProjectTitle()
        if (result.isSuccess) handleAction(ProjectAction.SetTabsData(result.getOrNull()))
        else handleAction(
            ProjectAction.OutputLogcat(
                msg = "获取项目tabs失败:${
                    result.exceptionOrNull().toString()
                }"
            )
        )
    }

    //获取项目内容
    private suspend fun getProjectList(page: Int = 0, id: Int) {
        val result = projectRepo.getProject(page, id)
        if (result.isSuccess) handleAction(
            ProjectAction.SetArticleData(
                page, result.getOrNull()
            )
        )
        else handleAction(
            ProjectAction.OutputLogcat(
                msg = "获取项目失败:${
                    result.exceptionOrNull().toString()
                }"
            )
        )
    }

}