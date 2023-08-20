package com.dokiwei.wanandroid.ui.widgets

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.Dp
import com.dokiwei.wanandroid.R
import kotlinx.coroutines.delay

/**
 * @author DokiWei
 * @date 2023/7/15 15:41
 *
 * 收藏按钮
 *
 * @param modifier:Modifier
 * @param size:图标大小
 * @param liked:是否已收藏
 * @param onclick:点击事件
 */
@Composable
fun LikeIcon(
    modifier: Modifier = Modifier,
    size: Dp,
    liked: Boolean,
    onclick: () -> Unit
) {
    var scale by remember {
        mutableFloatStateOf(1f)
    }
    val scaleAnim by animateFloatAsState(
        targetValue = scale,
        label = "点赞动画",
        animationSpec = spring(Spring.DampingRatioHighBouncy)
    )
    LaunchedEffect(scale) {
        delay(100)
        scale = 1f
    }
    IconButton(
        modifier = modifier,
        onClick = {
            scale += 0.2f
            onclick()
        }
    ) {
        Icon(
            modifier = Modifier
                .size(size)
                .scale(scaleAnim),
            imageVector = if (liked) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
            contentDescription = null,
            tint = if (liked) colorResource(id = R.color.like) else MaterialTheme.colorScheme.onSurface
        )
    }
}