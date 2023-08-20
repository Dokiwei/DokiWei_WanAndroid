package com.dokiwei.wanandroid.ui.screen.project

import androidx.paging.PagingData
import com.dokiwei.wanandroid.model.entity.project.ProjectEntity
import com.dokiwei.wanandroid.model.entity.project.ProjectTabEntity
import kotlinx.coroutines.flow.Flow

/**
 * @author DokiWei
 * @date 2023/7/27 15:01
 */
data class ProjectState(
    val title: Flow<PagingData<ProjectTabEntity>>,
    val data: Flow<PagingData<ProjectEntity>>,
    val isTabExpand: Boolean = false,
    val scrollToTop: Boolean = false,
    val selectedTabIndex: Int = 0
)