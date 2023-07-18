package com.dokiwei.wanandroid.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import kotlinx.coroutines.delay

/**
 * @author DokiWei
 * @date 2023/7/15 14:40
 *
 * 加载ui
 *
 * @param timeOut:加载超时时间
 */
@Composable
fun Loading(
    timeOut: Long = 5000
) {
    var isTimeOut by remember { mutableStateOf(false) }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background.copy(alpha = 0.3f))
    ) {
        LaunchedEffect(Unit) {
            delay(timeOut)
            isTimeOut = true
        }
        if (!isTimeOut) {
            CircularProgressIndicator(Modifier.align(Alignment.Center))
        } else {
            Text(text = "请求超时,请检查网络后下拉刷新",Modifier.align(Alignment.Center))
        }
    }
}