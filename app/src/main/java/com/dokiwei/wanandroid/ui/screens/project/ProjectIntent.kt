package com.dokiwei.wanandroid.ui.screens.project

/**
 * @author DokiWei
 * @date 2023/7/27 15:01
 */
sealed class ProjectIntent {
    data class SelectedTab(val index: Int) : ProjectIntent()
    data class UpdateTabExpanded(val boolean: Boolean) : ProjectIntent()
    object Refresh : ProjectIntent()
    object LoadMore : ProjectIntent()
    data class UpdateScrollToTop(val boolean: Boolean) : ProjectIntent()
}