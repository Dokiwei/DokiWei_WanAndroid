package com.dokiwei.wanandroid.ui.widgets

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.paging.ItemSnapshotList
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Scale
import com.dokiwei.wanandroid.model.apidata.BannerData
import kotlinx.coroutines.delay

/**
 * @author DokiWei
 * @date 2023/7/16 23:18
 *
 * 轮播图
 *
 * @param pageCount:轮播图数量
 * @param bannerData:轮播图数据
 * @param onClick:轮播图点击事件
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Banner(
    pageCount: Int,
    bannerData: ItemSnapshotList<BannerData>,
    onClick: (BannerData) -> Unit
) {
    val pagerState = rememberPagerState(
        pageCount = { pageCount },
        initialPage = 0,
        initialPageOffsetFraction = 0f
    )
    //轮播图定时器
    LaunchedEffect(pagerState.settledPage) {
        delay(3000)
        val nextPage =
            if (pagerState.currentPage + 1 == bannerData.size) 0 else pagerState.currentPage + 1
        pagerState.animateScrollToPage(nextPage)
    }
    HorizontalPager(
        state = pagerState,
        contentPadding = PaddingValues(horizontal = 65.dp)
    ) { index ->
        val scaleAnim by animateFloatAsState(
            targetValue = if (pagerState.currentPage == index) 1f else 0.85f,
            label = "轮播图聚焦动画"
        )

        Box {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(bannerData[index]!!.imagePath)
                    .scale(Scale.FILL).build(),
                contentDescription = bannerData[index]!!.title,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .fillMaxWidth()
                    .scale(scaleAnim)
                    .clip(RoundedCornerShape(20.dp))
                    .clickable {
                        onClick(bannerData[pagerState.currentPage]!!)
                    }
            )
            PageIndicator(
                pagerState = pagerState,
                pageCount = bannerData.size,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(8.dp)
            )
        }
    }
}