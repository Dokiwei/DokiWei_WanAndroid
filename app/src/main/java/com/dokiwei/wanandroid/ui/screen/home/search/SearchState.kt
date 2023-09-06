package com.dokiwei.wanandroid.ui.screen.home.search

import androidx.paging.PagingData
import com.dokiwei.wanandroid.model.apidata.ArticleData
import com.dokiwei.wanandroid.model.apidata.HotKeyData
import kotlinx.coroutines.flow.Flow

/**
 * @author DokiWei
 * @date 2023/7/31 14:31
 */
data class SearchState(
    val hotKeys: List<HotKeyData> = emptyList(),
    val isShowResult: Boolean = false,
    val data: Flow<PagingData<ArticleData>>
)