package com.dokiwei.wanandroid.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * @author DokiWei
 * @date 2023/7/15 20:07
 */
@Composable
fun CardContent(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    content: @Composable() (ColumnScope.() -> Unit)
) {
    Card(
        modifier = modifier
            .cardContent()
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(),
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
    ) {
        content()
    }
}