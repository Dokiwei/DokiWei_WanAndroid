package com.dokiwei.wanandroid.ui.screens.project

import android.util.Log
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dokiwei.wanandroid.data.ProjectData
import com.dokiwei.wanandroid.data.ProjectTitleData
import com.dokiwei.wanandroid.network.repository.ProjectRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

/**
 * @author DokiWei
 * @date 2023/7/15 16:18
 */
class ProjectViewModel : ViewModel() {
    private val projectRepo = ProjectRepo()

    private val _projectTitleList = MutableStateFlow<List<ProjectTitleData>>(emptyList())
    val projectTitleList = _projectTitleList
    private val _projectList = MutableStateFlow<List<ProjectData>>(emptyList())
    val projectList = _projectList

    private val _selectedTabIndex = mutableIntStateOf(0)
    val selectedTabIndex = _selectedTabIndex
    fun onTabSelected(index: Int) {
        _selectedTabIndex.intValue = index
        getProjectList(_projectTitleList.value[_selectedTabIndex.intValue].id)
        _isTabExpand.value=false
    }
    private val _isTabExpand= mutableStateOf(false)
    val isTabExpand=_isTabExpand
    fun onTabExpanded(boolean: Boolean){
        _isTabExpand.value=boolean
    }

    init {
        viewModelScope.launch {
            getProjectTitleList()
            if (_projectTitleList.value.isNotEmpty()) {
                getProjectList(_projectTitleList.value[_selectedTabIndex.intValue].id)
            }
        }
    }

    //获取项目tab
    private suspend fun getProjectTitleList() {
        val result = projectRepo.getProjectTitle()
        if (result.isSuccess) _projectTitleList.value = result.getOrNull() ?: emptyList()
        else Log.e("获取项目标题失败", result.exceptionOrNull().toString())
    }

    //获取项目内容
    private fun getProjectList(id: Int) {
        viewModelScope.launch {
            val result = projectRepo.getProject(0, id)
            if (result.isSuccess) _projectList.value = result.getOrNull() ?: emptyList()
            else Log.e("获取项目失败", result.exceptionOrNull().toString())
        }
    }

}