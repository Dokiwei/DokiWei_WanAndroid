package com.dokiwei.wanandroid.ui.screens.project

import androidx.compose.runtime.snapshots.SnapshotStateList
import com.dokiwei.wanandroid.bean.ProjectBean
import com.dokiwei.wanandroid.bean.ProjectTabsBean

/**
 * @author DokiWei
 * @date 2023/7/27 15:01
 */
data class ProjectState(
    val projectTabs: SnapshotStateList<ProjectTabsBean> = SnapshotStateList(),
    val projectList: SnapshotStateList<ProjectBean> = SnapshotStateList(),
    val selectedTabIndex: Int = 0,
    val isTabExpand: Boolean = false,
    val nowPageIndex: Int = 0,
    val isRefreshing: Boolean = false,
    val isLoadingMore: Boolean = false,
    val msg: String = "",
    val scrollToTop: Boolean = false
)