package com.dokiwei.wanandroid.ui.screens.project

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dokiwei.wanandroid.data.ProjectData
import com.dokiwei.wanandroid.data.ProjectTitleData
import com.dokiwei.wanandroid.network.repository.CollectArticleRepo
import com.dokiwei.wanandroid.network.repository.ProjectRepo
import com.dokiwei.wanandroid.util.ToastUtil
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

/**
 * @author DokiWei
 * @date 2023/7/15 16:18
 */
class ProjectViewModel : ViewModel() {
    private val projectRepo = ProjectRepo()
    private val collectArticleRepo = CollectArticleRepo()

    //项目标题数据
    private val _projectTitleList = MutableStateFlow(SnapshotStateList<ProjectTitleData>())
    val projectTitleList = _projectTitleList

    //项目数据
    private val _projectList = MutableStateFlow(SnapshotStateList<ProjectData>())
    val projectList = _projectList

    //当前选择的tab
    private val _selectedTabIndex = mutableIntStateOf(0)
    val selectedTabIndex = _selectedTabIndex
    fun onTabSelected(index: Int) {
        _selectedTabIndex.intValue = index
        getProjectList(id = _projectTitleList.value[_selectedTabIndex.intValue].id)
        _isTabExpand.value = false
    }

    //全部项目是否显示
    private val _isTabExpand = mutableStateOf(false)
    val isTabExpand = _isTabExpand
    fun onTabExpanded(boolean: Boolean) {
        _isTabExpand.value = boolean
    }

    //当前页
    private val _nowPageIndex = MutableStateFlow(0)

    //是否刷新
    private val _isRefreshing = mutableStateOf(false)
    val isRefreshing = _isRefreshing

    //初始化
    init {
        viewModelScope.launch {
            getProjectTitleList()
            if (_projectTitleList.value.isNotEmpty()) {
                getProjectList(id = _projectTitleList.value[_selectedTabIndex.intValue].id)
            }
        }
    }

    //刷新数据
    fun onRefresh() {
        _isRefreshing.value = true
        getProjectList(id = _projectTitleList.value[_selectedTabIndex.intValue].id)
        _isRefreshing.value = false
    }

    fun loadMore(): Boolean {
        if (_nowPageIndex.value < 40) {
            _nowPageIndex.value += 1
            getProjectList(
                _nowPageIndex.value,
                _projectTitleList.value[_selectedTabIndex.intValue].id
            )
            return true
        }
        return false
    }

    //获取项目tab
    private suspend fun getProjectTitleList() {
        val result = projectRepo.getProjectTitle()
        if (result.isSuccess) result.getOrNull()?.let { list ->
            list.forEach {
                _projectTitleList.value.add(it)
            }
            val lastId = _projectTitleList.value.removeLast().id
            val lastElement = ProjectTitleData(lastId,"最新项目")
            _projectTitleList.value.add(0, lastElement)
        }
        else Log.e("获取项目标题失败", result.exceptionOrNull().toString())
    }

    //获取项目内容
    private fun getProjectList(page: Int = 0, id: Int) {
        viewModelScope.launch {
            val result = projectRepo.getProject(page, id)
            if (result.isSuccess) {
                if (page == 0) _projectList.value.clear()
                result.getOrNull()?.let { list ->
                    list.forEach {
                        _projectList.value.add(it)
                    }
                }
            } else Log.e("获取项目失败", result.exceptionOrNull().toString())
        }
    }

    //收藏
    fun likeArticle(id: Int, context: Context, callBack: (Boolean) -> Unit) {
        viewModelScope.launch {
            val result = collectArticleRepo.likeArticle(id)
            if (result.isFailure) {
                ToastUtil.showMsg(context, "收藏失败")
                callBack(false)
                Log.e("收藏操作异常", result.exceptionOrNull().toString())
            } else {
                ToastUtil.showMsg(context, "收藏成功")
                callBack(true)
            }
        }
    }

    //取消收藏
    fun unlikeArticle(id: Int, context: Context, callBack: (Boolean) -> Unit) {
        viewModelScope.launch {
            val result = collectArticleRepo.unLikeArticle(id)
            if (result.isFailure) {
                ToastUtil.showMsg(context, "取消收藏失败")
                callBack(true)
                Log.e("取消收藏操作异常", result.exceptionOrNull().toString())
            } else {
                ToastUtil.showMsg(context, "取消收藏成功")
                callBack(false)
            }
        }
    }

}