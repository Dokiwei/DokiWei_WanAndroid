package com.dokiwei.wanandroid.network.api

import com.dokiwei.wanandroid.model.util.Constants
import okhttp3.ResponseBody
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

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
        @Field("username") username: String, @Field("password") password: String
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


    /**
     * 获取当前用户积分
     */
    @GET("lg/coin/userinfo/json")
    suspend fun coinInfo(
    ): ResponseBody

    /**
     * 未读消息数量(需登录)
     */
    @GET("message/lg/count_unread/json")
    suspend fun getUnreadQuantity(): ResponseBody

    /**
     * 已读消息(需要登录)
     * @param page:页码(从1开始)
     */
    @GET("message/lg/readed_list/{page}/json")
    suspend fun getRead(
        @Path("page") page: Int, @Query("page_size") pageSize: Int = Constants.API_PAGE_SIZE
    ): ResponseBody


    /**
     * 未读消息(需要登录,此接口一旦访问，则所有该用户的消息都会被认为已读)
     * @param page:页码(从1开始)
     */
    @GET("message/lg/unread_list/{page}/json")
    suspend fun getUnread(
        @Path("page") page: Int, @Query("page_size") pageSize: Int = Constants.API_PAGE_SIZE
    ): ResponseBody

    /**
     * 获取当前积分详细
     */
    @GET("lg/coin/list/{page}/json")
    suspend fun getCoinList(
        @Path("page") page: Int,
        @Query("page_size") pageSize: Int = Constants.API_PAGE_SIZE
    ): ResponseBody


    /**
     * 获取当前积分排行
     */
    @GET("coin/rank/1/json")
    suspend fun getRank():ResponseBody
}
