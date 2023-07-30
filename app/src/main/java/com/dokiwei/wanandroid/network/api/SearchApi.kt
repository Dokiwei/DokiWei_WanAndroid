package com.dokiwei.wanandroid.network.api

import okhttp3.ResponseBody
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

/**
 * @author DokiWei
 * @date 2023/7/27 17:57
 */
interface SearchApi {
    /**
     * 热搜词
     */
    @GET("hotkey/json")
    suspend fun getSearchHotKey(): ResponseBody

    /**
     * 搜索
     */
    @POST("article/query/{page}/json")
    @FormUrlEncoded
    suspend fun search(
        @Path("page") page: Int,
        @Field("k") key: String
    ): ResponseBody
}