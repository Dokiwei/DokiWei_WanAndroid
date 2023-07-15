package com.dokiwei.wanandroid.network.api

import okhttp3.ResponseBody
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

/**
 * @author DokiWei
 * @date 2023/7/9 19:18
 */
interface RegisterApi {
    @FormUrlEncoded
    @POST("user/register")
    suspend fun register(
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("repassword") rePassword: String,
    ): ResponseBody
}