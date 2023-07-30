package com.dokiwei.wanandroid.ui.screens.home.content.qa

import com.dokiwei.wanandroid.bean.ArticleBean

/**
 * @author DokiWei
 * @date 2023/7/26 23:02
 */
sealed class QaAction {
    data class ShowToast(val msg: String) : QaAction()
    data class OutputLogcat(val tag: String = "QaAction", val msg: String, val level: Int = 0) :
        QaAction()

    data class SetArticleData(val page: Int = 0, val dataList: List<ArticleBean>?) : QaAction()
    object LoadMore : QaAction()
    object Refresh : QaAction()
}