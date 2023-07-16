package com.dokiwei.wanandroid.ui.component

import androidx.compose.animation.core.animateIntAsState
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * @author DokiWei
 * @date 2023/7/15 16:29
 *
 * 自定义滚动标题栏
 *
 * @param titleList:标题数据
 * @param selectedTabIndex:当前选择项
 * @param onClick:标题项点击事件
*/
@Composable
fun MyScrollableTabRow(
    titleList: List<String>,
    selectedTabIndex: Int,
    onClick: (Int) -> Unit
) {
    ScrollableTabRow(
        selectedTabIndex = selectedTabIndex,
        indicator = { tabPositions ->
            val indicatorWidth = 20.dp
            val indicatorOffset = (tabPositions[selectedTabIndex].width - indicatorWidth) / 2
            Divider(
                Modifier
                    .tabIndicatorOffset(tabPositions[selectedTabIndex])
                    .fillMaxWidth()
                    .wrapContentSize(Alignment.BottomStart)
                    .offset(x = indicatorOffset)
                    .clip(RoundedCornerShape(5.dp))
                    .width(indicatorWidth),
                thickness = 5.dp,
                color = MaterialTheme.colorScheme.primary
            )
        },
        divider = {}
    ) {
        titleList.forEachIndexed { index, title ->
            var fontSize by remember {
                mutableIntStateOf(14)
            }
            val fontSizeAnim by animateIntAsState(targetValue = fontSize, label = "文字动画")
            fontSize = if (selectedTabIndex == index) 18 else 14
            Tab(
                text = {
                    Text(
                        text = title,
                        color = if (selectedTabIndex == index) MaterialTheme.colorScheme.primary else Color.Gray,
                        fontSize = fontSizeAnim.sp
                    )
                },
                selected = selectedTabIndex == index,
                onClick = { onClick(index) }
            )
        }
    }
}
