package com.dokiwei.wanandroid.ui.screens.webview

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.dokiwei.wanandroid.network.webview.WebViewManager
import com.google.accompanist.web.AccompanistWebChromeClient
import com.google.accompanist.web.AccompanistWebViewClient
import com.google.accompanist.web.WebView
import com.google.accompanist.web.rememberWebViewNavigator
import com.google.accompanist.web.rememberWebViewState
import kotlinx.coroutines.delay

/**
 * @author DokiWei
 * @date 2023/7/13 23:35
 */
@SuppressLint("SetJavaScriptEnabled")
@Composable
fun WebViewScreen(originalUrl: String, navController: NavController) {
    val context = LocalContext.current
    var webView by remember {
        mutableStateOf<WebView?>(null)
    }
    val state = rememberWebViewState(originalUrl)
    val navigator = rememberWebViewNavigator()

    var progress by remember { mutableFloatStateOf(0f) }

    val adHosts = ""

    val client = object : AccompanistWebViewClient() {

        override fun shouldInterceptRequest(view: WebView, request: WebResourceRequest): WebResourceResponse? {
            val url = request.url.toString()
            return if (adHosts.any { url.contains(it) }) {
                WebResourceResponse("text/plain", "UTF-8", null)
            } else {
                super.shouldInterceptRequest(view, request)
            }
        }

        override fun shouldOverrideUrlLoading(
            view: WebView?, request: WebResourceRequest?
        ): Boolean {
            if (view != null && request != null && request.url != null) {
                if ("http" != request.url.scheme && "https" != request.url.scheme) {
                    try {
                        view.context.startActivity(Intent(Intent.ACTION_VIEW, request.url))
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    return true
                }
            }
            return false
        }
    }

    val chromeClient = object : AccompanistWebChromeClient() {

        override fun onProgressChanged(view: WebView, newProgress: Int) {
            super.onProgressChanged(view, newProgress)
            progress = (newProgress / 100f).coerceIn(0f, 1f)
        }
    }

    Scaffold(bottomBar = {
        Column {
            //进度条
            AnimatedVisibility(visible = (progress > 0f && progress < 1f)) {
                LinearProgressIndicator(
                    progress = progress,
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.primary,
                )
            }
            BottomAppBar {

                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    (0..3).forEach { index ->
                        var scale by remember {
                            mutableFloatStateOf(1f)
                        }
                        val scaleAnim by animateFloatAsState(
                            targetValue = scale,
                            label = "点赞动画",
                            animationSpec = spring(Spring.DampingRatioHighBouncy)
                        )
                        LaunchedEffect(scale){
                            delay(200)
                            scale = 1f
                        }
                        IconButton(onClick = {
                            scale += 0.4f
                            when (index) {
                                0 -> navController.navigateUp()
                                1 -> navigator.navigateBack()
                                2 -> navigator.reload()
                                3 -> try {
                                    val intent = Intent(
                                        Intent.ACTION_VIEW, Uri.parse(state.lastLoadedUrl)
                                    )
                                    intent.addCategory(Intent.CATEGORY_BROWSABLE)
                                    context.startActivity(intent)
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }

                        }) {
                            Icon(
                                modifier = Modifier.scale(scaleAnim),
                                imageVector = when (index) {
                                    0 -> Icons.Default.ArrowBack
                                    1 -> Icons.Default.KeyboardArrowUp
                                    2 -> Icons.Default.Refresh
                                    else -> Icons.Default.ExitToApp
                                },
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }

        }
    }) { paddingValues ->
        Column(Modifier.padding(paddingValues)) {
            val backgroundColor= MaterialTheme.colorScheme.background.toArgb()
            WebView(state = state,
                captureBackPresses = false,
                navigator = navigator,
                onCreated = { webView ->
                    webView.settings.javaScriptEnabled = true
                    webView.setBackgroundColor(backgroundColor)
                },
                onDispose = { webView ->
                    WebViewManager.recycle(webView)
                },
                client = client,
                chromeClient = chromeClient,
                factory = { context -> WebViewManager.obtain(context).also { webView = it } }
            )
        }
    }

}

