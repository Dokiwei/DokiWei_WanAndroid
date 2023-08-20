package com.dokiwei.wanandroid.ui.screen.home.content.home

import androidx.paging.PagingData
import com.dokiwei.wanandroid.model.apidata.BannerData
import com.dokiwei.wanandroid.model.entity.home.HomeEntity
import kotlinx.coroutines.flow.Flow

/**
 * @author DokiWei
 * @date 2023/7/26 20:59
 */
data class HomeState(
    val bannerData:  Flow<PagingData<BannerData>>,
    val articleData: Flow<PagingData<HomeEntity>>
)