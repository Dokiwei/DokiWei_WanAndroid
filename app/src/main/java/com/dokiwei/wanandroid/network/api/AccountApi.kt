package com.dokiwei.wanandroid.network.api

import okhttp3.ResponseBody
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

/**
 * @author DokiWei
 * @date 2023/7/9 19:10
 */
interface AccountApi {
    /**
     * 登录
     * @param username:用户名
     * @param password:用户密码
     */
    @FormUrlEncoded
    @POST("user/login")
    suspend fun login(
        @Field("username") username: String,
        @Field("password") password: String
    ): ResponseBody

    /**
     * 登出
     */
    @GET("user/logout/json")
    suspend fun logout(
    ): ResponseBody

    /**
     * 注册
     * @param username:用户名
     * @param password:用户密码
     * @param rePassword:确认密码
     */
    @FormUrlEncoded
    @POST("user/register")
    suspend fun register(
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("repassword") rePassword: String,
    ): ResponseBody

    /**
     * 获取当前用户信息
     */
    @GET("user/lg/userinfo/json")
    suspend fun userInfo(
    ): ResponseBody

}
