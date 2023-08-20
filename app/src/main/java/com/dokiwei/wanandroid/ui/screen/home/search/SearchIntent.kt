package com.dokiwei.wanandroid.ui.screen.home.search

/**
 * @author DokiWei
 * @date 2023/7/31 14:32
 */
sealed class SearchIntent {
    data class Refresh(val k: String) : SearchIntent()
    data class LoadMore(val k: String) : SearchIntent()
    data class GetSearchResult(val page: Int = 0, val k: String) : SearchIntent()
    data class UpdateScrollToTop(val boolean: Boolean) : SearchIntent()
    object RevertToTheDefaultShowResult : SearchIntent()
    object RevertToTheDefaultPageIndex : SearchIntent()
}