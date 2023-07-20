package com.dokiwei.wanandroid.network.api

import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

/**
 * @author DokiWei
 * @date 2023/7/19 18:41
 */
interface CollectApi {
    //我的收藏列表
    @GET("lg/collect/list/{page}/json")
    suspend fun myLike(
        @Path("page") page: Int
    ): ResponseBody

    //收藏站内文章
    @POST("lg/collect/{id}/json")
    suspend fun like(
        @Path("id") id: Int
    ): ResponseBody

    //取消收藏站内文章
    @POST("lg/uncollect_originId/{id}/json")
    suspend fun unlike(
        @Path("id") id: Int
    ): ResponseBody

    //收藏站外文章
    @POST("lg/collect/add/json")
    suspend fun likeCustom(
        @Path("id") id: Int
    ): ResponseBody

    //取消收藏站外文章
    @POST("lg/uncollect/{id}/json")
    suspend fun unLikeCustom(
        @Path("id") id: Int
    ): ResponseBody
}