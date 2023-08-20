package com.dokiwei.wanandroid.ui.widgets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * @author DokiWei
 * @date 2023/8/19 20:24
 */
@Composable
fun SettingButton(
    imageVector: ImageVector,
    title: String,
    badge: @Composable (() -> Unit)? = null,
    trailingContent: @Composable (() -> Unit)? = null,
    onClick: () -> Unit
) {
    ListItem(modifier = Modifier.clickable { onClick() },
//        colors = ListItemDefaults.colors(containerColor = colorResource(id = R.color.button)),
        leadingContent = {
            Icon(
                imageVector = imageVector,
                tint = MaterialTheme.colorScheme.primary,
                contentDescription = null,
            )
        },
        headlineContent = {
            badge?.invoke()
            if (badge == null) Text(text = title)
        },
        trailingContent = {
            Row (verticalAlignment = Alignment.CenterVertically){
                trailingContent?.invoke()
                Icon(
                    imageVector = Icons.Default.KeyboardArrowRight,
                    contentDescription = null,
                )
            }
        })
}