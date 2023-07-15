package com.dokiwei.wanandroid.network.api

import okhttp3.ResponseBody
import retrofit2.http.GET

/**
 * @author DokiWei
 * @date 2023/7/13 16:52
 */
interface UserInfoApi {
    @GET("user/lg/userinfo/json")
    suspend fun userInfo(
    ): ResponseBody
}