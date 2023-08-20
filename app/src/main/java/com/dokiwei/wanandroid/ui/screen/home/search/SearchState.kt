package com.dokiwei.wanandroid.ui.screen.home.search

import androidx.compose.runtime.snapshots.SnapshotStateList
import com.dokiwei.wanandroid.model.apidata.ArticleData
import com.dokiwei.wanandroid.model.apidata.HotKeyData

/**
 * @author DokiWei
 * @date 2023/7/31 14:31
 */
data class SearchState(
    val isRefreshing: Boolean = false,
    val isLoadingMore: Boolean = false,
    val msg: String = "",
    val searchResult: SnapshotStateList<ArticleData> = SnapshotStateList(),
    val nowPageIndex: Int = 0,
    val scrollToTop: Boolean = false,
    val hotKeys: List<HotKeyData> = emptyList(),
    val isShowResult: Boolean = false
)