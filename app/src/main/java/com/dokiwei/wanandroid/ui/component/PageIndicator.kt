package com.dokiwei.wanandroid.ui.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * @author DokiWei
 * @date 2023/7/11 19:27
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PageIndicator(
    pagerState: PagerState,
    pageCount: Int,
    modifier: Modifier = Modifier,
    indicatorColor: Color = MaterialTheme.colorScheme.primary,
    indicatorSize: Dp = 20.dp,
    spacing: Dp = 8.dp
) {
    val sizePx = indicatorSize.value
    val spacingPx = spacing.value

    Canvas(modifier) {
        val xStart = (size.width - (pageCount * sizePx + (pageCount - 1) * spacingPx)) / 2
        val y = size.height / 2

        for (i in 0 until pageCount) {
            val x = xStart + i * (sizePx + spacingPx)
            drawCircle(
                color = indicatorColor,
                radius = sizePx / 2,
                center = Offset(x + sizePx / 2, y),
                alpha = if (i == pagerState.currentPage) 1f else 0.5f
            )
        }
    }
}