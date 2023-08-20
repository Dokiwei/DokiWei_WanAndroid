package com.dokiwei.wanandroid.network.client

import com.dokiwei.wanandroid.model.util.ToastAndLogcatUtil
import okhttp3.Interceptor
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import okio.Buffer
import java.io.IOException

/**
 * @author DokiWei
 * @date 2023/7/7 22:46
 */
class LoggingInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response: Response = try {
            chain.proceed(request)
        } catch (e: IOException) {
            e.printStackTrace()
            Response.Builder()
                .request(request)
                .protocol(Protocol.HTTP_1_1)
                .code(500)
                .message("网络错误")
                .body("网络错误".toResponseBody(null))
                .build()
        }
        ToastAndLogcatUtil.log(
            "拦截器",
            """
        host:${request.url}
        客户端请求:head-${request.headers}  body-${request.bodyString()}
        服务器回应:code-${response.code}
        耗时:${response.elapsedTime()}ms
    """.trimIndent()
        )
        return response
    }

    // 扩展函数，获取请求的字符串
    private fun Request.bodyString(): String {
        val requestBody = this.body
        if (requestBody != null) {
            val buffer = Buffer()
            requestBody.writeTo(buffer)
            val charset = requestBody.contentType()?.charset(Charsets.UTF_8) ?: Charsets.UTF_8
            return buffer.readString(charset)
        }
        return ""
    }

    // 扩展函数，获取响应的字符串
    private fun Response.bodyString(): String {
        val responseBody = this.body
        val source = responseBody.source()
        source.request(Long.MAX_VALUE)
        val buffer = source.buffer
        val charset = responseBody.contentType()?.charset(Charsets.UTF_8) ?: Charsets.UTF_8
        return buffer.clone().readString(charset)
    }

    // 扩展函数，获取响应的耗时
    private fun Response.elapsedTime(): Long {
        val sentRequestAtMillis = this.sentRequestAtMillis
        val receivedResponseAtMillis = this.receivedResponseAtMillis
        return receivedResponseAtMillis - sentRequestAtMillis
    }
}
