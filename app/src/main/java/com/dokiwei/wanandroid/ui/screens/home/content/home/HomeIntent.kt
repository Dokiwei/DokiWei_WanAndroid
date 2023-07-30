package com.dokiwei.wanandroid.ui.screens.home.content.home

/**
 * @author DokiWei
 * @date 2023/7/26 21:03
 */
sealed class HomeIntent {
    object Refresh : HomeIntent()
    object LoadMore : HomeIntent()
    data class GetArticleData(val page: Int = 0) : HomeIntent()
}