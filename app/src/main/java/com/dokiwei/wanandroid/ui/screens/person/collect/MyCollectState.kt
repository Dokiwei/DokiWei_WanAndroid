package com.dokiwei.wanandroid.ui.screens.person.collect

import androidx.compose.runtime.snapshots.SnapshotStateList
import com.dokiwei.wanandroid.bean.CollectBean

/**
 * @author DokiWei
 * @date 2023/7/27 17:27
 */
data class MyCollectState(
    val nowPageIndex: Int = 0,
    val isRefreshing: Boolean = false,
    val isLoadingMore: Boolean = false,
    val myCollectList: SnapshotStateList<CollectBean> = SnapshotStateList(),
    val msg: String = "",
    val scrollToTop: Boolean = false
)