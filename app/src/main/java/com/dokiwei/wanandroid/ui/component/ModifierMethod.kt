package com.dokiwei.wanandroid.ui.component

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.unit.dp

/**
 * @author DokiWei
 * @date 2023/7/8 16:46
 */
fun Modifier.mainBody() =
    composed {
        this
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    }

@SuppressLint("UnnecessaryComposedModifier")
fun Modifier.cardContent() = composed {
    this
        .fillMaxWidth()
        .padding(10.dp, 5.dp)
}