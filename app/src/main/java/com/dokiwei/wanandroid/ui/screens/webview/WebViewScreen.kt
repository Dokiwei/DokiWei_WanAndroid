package com.dokiwei.wanandroid.ui.screens.webview

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import com.google.accompanist.web.WebView
import com.google.accompanist.web.rememberWebViewState

/**
 * @author DokiWei
 * @date 2023/7/13 23:35
 */
@SuppressLint("SetJavaScriptEnabled")
@Composable
fun WebViewScreen(link: String) {
    val state = rememberWebViewState(link)
    WebView(
        state,
        onCreated = {
            it.settings.javaScriptEnabled = true
        }
    )
}