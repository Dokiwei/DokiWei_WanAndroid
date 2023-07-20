package com.dokiwei.wanandroid.network.api

import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * @author DokiWei
 * @date 2023/7/20 17:28
 */
interface MessageApi {
    @GET("message/lg/count_unread/json")
    suspend fun getUnreadQuantity():ResponseBody


    //page 1
    @GET("lg/readed_list/{page}/json")
    suspend fun getRead(
        @Path("page") page:Int
    ):ResponseBody


    //page -1
    @GET("lg/unread_list/{page}/json")
    suspend fun getUnread(
        @Path("page") page:Int
    ):ResponseBody
}