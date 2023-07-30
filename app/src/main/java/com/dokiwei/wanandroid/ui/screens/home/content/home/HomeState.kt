package com.dokiwei.wanandroid.ui.screens.home.content.home

import androidx.compose.runtime.snapshots.SnapshotStateList
import com.dokiwei.wanandroid.bean.ArticleBean
import com.dokiwei.wanandroid.bean.BannerBean

/**
 * @author DokiWei
 * @date 2023/7/26 20:59
 */
data class HomeState(
    val isRefreshing: Boolean = false,
    val isLoadingMore: Boolean = false,
    val nowPageIndex: Int = 0,
    val bannerList: List<BannerBean> = emptyList(),
    val articleList: SnapshotStateList<ArticleBean> = SnapshotStateList(),
    val msg: String = ""
)