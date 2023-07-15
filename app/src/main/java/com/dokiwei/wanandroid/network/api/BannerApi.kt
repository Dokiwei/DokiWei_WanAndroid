package com.dokiwei.wanandroid.network.api

import okhttp3.ResponseBody
import retrofit2.http.GET

/**
 * @author DokiWei
 * @date 2023/7/11 17:33
 */
interface BannerApi {
    @GET("banner/json")
    suspend fun banner(
    ): ResponseBody
}