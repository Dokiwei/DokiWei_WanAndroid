package com.dokiwei.wanandroid.network.api

import okhttp3.ResponseBody
import retrofit2.http.GET

/**
 * @author DokiWei
 * @date 2023/7/9 19:30
 */
interface LogoutApi {
    @GET("user/logout/json")
    suspend fun logout(
    ): ResponseBody
}