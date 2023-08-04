package com.dokiwei.wanandroid.network.webview

import android.content.Context
import android.content.MutableContextWrapper
import android.view.ViewGroup
import android.webkit.WebView

/**
 * @author DokiWei
 * @date 2023/7/31 15:02
 */
class WebViewManager private constructor() {

    companion object {

        private val webViewCache: MutableList<WebView> = ArrayList(1)

        private fun create(context: Context): WebView {
            // ......
            return WebView(context)
        }

        fun obtain(context: Context): WebView {
            if (webViewCache.isEmpty()) {
                webViewCache.add(create(MutableContextWrapper(context)))
            }
            val webView = webViewCache.removeFirst()
            val contextWrapper = webView.context as MutableContextWrapper
            contextWrapper.baseContext = context
            webView.clearHistory()
            webView.resumeTimers()
            return webView
        }

        fun recycle(webView: WebView) {
            try {
                webView.stopLoading()
                webView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null)
                webView.clearHistory()
                webView.pauseTimers()
                webView.webChromeClient = null
                val parent = webView.parent
                if (parent != null) {
                    (parent as ViewGroup).removeView(webView)
                }
                val contextWrapper = webView.context as MutableContextWrapper
                contextWrapper.baseContext = webView.context.applicationContext
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                if (!webViewCache.contains(webView)) {
                    webViewCache.add(webView)
                }
            }
        }

    }

}
