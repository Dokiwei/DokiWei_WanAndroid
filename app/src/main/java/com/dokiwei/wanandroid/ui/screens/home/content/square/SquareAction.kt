package com.dokiwei.wanandroid.ui.screens.home.content.square

import com.dokiwei.wanandroid.bean.ArticleBean

/**
 * @author DokiWei
 * @date 2023/7/26 23:41
 */
sealed class SquareAction {
    data class ShowToast(val msg: String) : SquareAction()
    data class OutputLogcat(val tag: String = "QaAction", val msg: String, val level: Int = 0) :
        SquareAction()

    data class SetArticleData(val page: Int = 0, val dataList: List<ArticleBean>?) : SquareAction()
    object LoadMore : SquareAction()
    object Refresh : SquareAction()
}