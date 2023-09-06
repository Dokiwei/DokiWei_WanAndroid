package com.dokiwei.wanandroid.ui.screen.home.search

/**
 * @author DokiWei
 * @date 2023/7/31 14:32
 */
sealed class SearchIntent {
    data class ChangeShowResult(val boolean: Boolean) : SearchIntent()
    data class GetSearchResult(val searchKey: String) : SearchIntent()
}