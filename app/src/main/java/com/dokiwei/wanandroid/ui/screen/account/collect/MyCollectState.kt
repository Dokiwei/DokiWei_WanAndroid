package com.dokiwei.wanandroid.ui.screen.account.collect

import androidx.compose.runtime.snapshots.SnapshotStateList
import com.dokiwei.wanandroid.model.apidata.CollectData

/**
 * @author DokiWei
 * @date 2023/7/27 17:27
 */
data class MyCollectState(
    val nowPageIndex: Int = 0,
    val isRefreshing: Boolean = false,
    val isLoadingMore: Boolean = false,
    val myCollectList: SnapshotStateList<CollectData> = SnapshotStateList(),
    val msg: String = "",
    val scrollToTop: Boolean = false
)