package com.dokiwei.wanandroid.ui.screens.project

import com.dokiwei.wanandroid.bean.ProjectBean
import com.dokiwei.wanandroid.bean.ProjectTabsBean

/**
 * @author DokiWei
 * @date 2023/7/27 15:01
 */
sealed class ProjectAction {
    data class ShowToast(val msg: String) : ProjectAction()
    data class OutputLogcat(
        val tag: String = "ProjectAction", val msg: String, val level: Int = 0
    ) : ProjectAction()

    data class SetArticleData(val page: Int = 0, val dataList: List<ProjectBean>?) : ProjectAction()
    data class SetTabsData(val dataList: List<ProjectTabsBean>?) : ProjectAction()
    data class TabExpanded(val boolean: Boolean) : ProjectAction()
    data class UpdateScrollToTop(val boolean: Boolean) : ProjectAction()
    data class TabSelected(val index: Int) : ProjectAction()
    object LoadMore : ProjectAction()
    object Refresh : ProjectAction()
}