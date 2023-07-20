package com.dokiwei.wanandroid.ui.component

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * @author DokiWei
 * @date 2023/7/15 20:07
 *
 * 文章列表卡片ui
 *
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardContent(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    content: @Composable (ColumnScope.() -> Unit)
) {
    Card(
        onClick = { onClick() },
        modifier = modifier.cardContent(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(),
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
    ) {
        content()
    }
}