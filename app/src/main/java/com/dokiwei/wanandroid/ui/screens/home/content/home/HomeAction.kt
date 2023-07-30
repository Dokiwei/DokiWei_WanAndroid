package com.dokiwei.wanandroid.ui.screens.home.content.home

import com.dokiwei.wanandroid.bean.ArticleBean
import com.dokiwei.wanandroid.bean.BannerBean

/**
 * @author DokiWei
 * @date 2023/7/26 21:18
 */
sealed class HomeAction {
    data class ShowToast(val msg: String) : HomeAction()
    data class OutputLogcat(val tag: String = "HomeAction", val msg: String, val level: Int = 0) :
        HomeAction()

    data class SetArticleData(val page: Int = 0, val dataList: List<ArticleBean>?) : HomeAction()
    data class SetBannerData(val dataList: List<BannerBean>) : HomeAction()
    object LoadMore : HomeAction()
    object Refresh : HomeAction()
}