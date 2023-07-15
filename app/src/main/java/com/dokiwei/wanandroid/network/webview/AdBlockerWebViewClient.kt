package com.dokiwei.wanandroid.network.webview

import android.os.Build
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.annotation.RequiresApi

/**
 * @author DokiWei
 * @date 2023/7/11 19:40
 */
class AdBlockerWebViewClient : WebViewClient() {
    private val adHosts = setOf("adserver.example.com")

    override fun shouldInterceptRequest(view: WebView, request: WebResourceRequest): WebResourceResponse? {
        val url = request.url.toString()
        return if (adHosts.any { url.contains(it) }) {
            WebResourceResponse("text/plain", "UTF-8", null)
        } else {
            super.shouldInterceptRequest(view, request)
        }
    }
}
