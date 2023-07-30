package com.dokiwei.wanandroid.util

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

/**
 * @author DokiWei
 * @date 2023/7/8 16:46
 *
 * modifier的自定义扩展方法
 *
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

fun NavController.myCustomNavigate(route: String) {
    navigate(route) {
        launchSingleTop = true
        restoreState = true
    }
}
