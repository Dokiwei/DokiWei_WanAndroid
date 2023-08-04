package com.dokiwei.wanandroid.network.api

import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * @author DokiWei
 * @date 2023/7/20 17:28
 */
interface MessageApi {
    /**
     * 未读消息数量(需登录)
     */
    @GET("message/lg/count_unread/json")
    suspend fun getUnreadQuantity():ResponseBody

    /**
     * 已读消息(需要登录)
     * @param page:页码(从1开始)
     */
    @GET("lg/readed_list/{page}/json")
    suspend fun getRead(
        @Path("page") page:Int
    ):ResponseBody


    /**
     * 未读消息(需要登录,此接口一旦访问，则所有该用户的消息都会被认为已读)
     * @param page:页码(从-1开始)
     */
    @GET("lg/unread_list/{page}/json")
    suspend fun getUnread(
        @Path("page") page:Int
    ):ResponseBody
}