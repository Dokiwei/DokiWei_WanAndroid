package com.dokiwei.wanandroid.ui.screens.home.content.qa

import androidx.compose.runtime.snapshots.SnapshotStateList
import com.dokiwei.wanandroid.bean.ArticleBean

/**
 * @author DokiWei
 * @date 2023/7/26 23:02
 */
data class QaState(
    val isRefreshing: Boolean = false,
    val isLoadingMore: Boolean = false,
    val nowPageIndex: Int = 1,
    val qaList: SnapshotStateList<ArticleBean> = SnapshotStateList(),
    val msg: String = ""
)