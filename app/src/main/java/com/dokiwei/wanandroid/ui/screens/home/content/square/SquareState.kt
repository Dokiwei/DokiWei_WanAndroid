package com.dokiwei.wanandroid.ui.screens.home.content.square

import androidx.compose.runtime.snapshots.SnapshotStateList
import com.dokiwei.wanandroid.bean.ArticleBean

/**
 * @author DokiWei
 * @date 2023/7/26 23:41
 */
data class SquareState(
    val isRefreshing: Boolean = false,
    val isLoadingMore: Boolean = false,
    val nowPageIndex: Int = 0,
    val userArticleList: SnapshotStateList<ArticleBean> = SnapshotStateList(),
    val msg: String = ""
)