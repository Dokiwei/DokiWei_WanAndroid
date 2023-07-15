package com.dokiwei.wanandroid.network.client

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response

/**
 * @author DokiWei
 * @date 2023/7/7 22:46
 */
class LoggingInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val t1 = System.nanoTime()
        Log.e("拦截器", "客户端请求 ${request.url} on ${chain.connection()} 请求体 ${request.headers}")
        val response = chain.proceed(request)
        val t2 = System.nanoTime()
        Log.e("拦截器", "服务器回应 ${response.request.url} 耗时 ${(t2 - t1) / 1e6}ms 回应体 ${response.headers}")
        return response
    }
}
