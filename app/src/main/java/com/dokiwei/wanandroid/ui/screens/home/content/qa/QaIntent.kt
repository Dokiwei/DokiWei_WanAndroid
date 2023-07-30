package com.dokiwei.wanandroid.ui.screens.home.content.qa


/**
 * @author DokiWei
 * @date 2023/7/26 23:01
 */
sealed class QaIntent {
    object Refresh : QaIntent()
    object LoadMore : QaIntent()
    data class GetArticleData(val page: Int = 0) : QaIntent()
}