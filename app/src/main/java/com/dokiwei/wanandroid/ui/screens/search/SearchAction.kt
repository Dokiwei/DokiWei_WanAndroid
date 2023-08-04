package com.dokiwei.wanandroid.ui.screens.search

import com.dokiwei.wanandroid.bean.ArticleBean
import com.dokiwei.wanandroid.bean.HotKeyBean

/**
 * @author DokiWei
 * @date 2023/7/31 14:32
 */
sealed class SearchAction {
    data class ShowToast(val msg: String) : SearchAction()
    data class OutputLogcat(val tag: String = "SearchAction", val msg: String, val level: Int = 0) :
        SearchAction()

    data class SetSearchResult(val page: Int = 0, val dataList: List<ArticleBean>?) : SearchAction()
    data class LoadMore(val k: String) : SearchAction()
    data class Refresh(val k: String) : SearchAction()
    data class UpdateScrollToTop(val boolean: Boolean) : SearchAction()
    object RevertToTheDefaultShowResult : SearchAction()
    object RevertToTheDefaultPageIndex : SearchAction()
    data class SetHotKey(val data: List<HotKeyBean>) : SearchAction()
}