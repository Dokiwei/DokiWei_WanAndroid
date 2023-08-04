package com.dokiwei.wanandroid.ui.screens.search

import androidx.compose.runtime.snapshots.SnapshotStateList
import com.dokiwei.wanandroid.bean.ArticleBean
import com.dokiwei.wanandroid.bean.HotKeyBean

/**
 * @author DokiWei
 * @date 2023/7/31 14:31
 */
data class SearchState(
    val isRefreshing: Boolean = false,
    val isLoadingMore: Boolean = false,
    val msg: String = "",
    val searchResult: SnapshotStateList<ArticleBean> = SnapshotStateList(),
    val nowPageIndex: Int = 0,
    val scrollToTop: Boolean = false,
    val hotKeys: List<HotKeyBean> = emptyList(),
    val isShowResult: Boolean = false
)