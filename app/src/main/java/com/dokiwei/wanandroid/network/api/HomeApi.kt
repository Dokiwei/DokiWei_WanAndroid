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
 * @date 2023/7/11 20:03
 */
interface HomeApi {

    /**
     * 首页文章
     */
    @GET("article/list/{page}/json")
    suspend fun homeArticle(
        @Path("page") page: Int,
        @Query("page_size") pageSize: Int= Constants.API_PAGE_SIZE
    ): ResponseBody

    /**
     * 问答文章
     */
    @GET("wenda/list/{page}/json")
    suspend fun qaArticle(
        @Path("page") page:Int,
        @Query("page_size") pageSize: Int= Constants.API_PAGE_SIZE
    ):ResponseBody

    /**
     * 广场文章
     */
    @GET("user_article/list/{page}/json")
    suspend fun squareArticle(
        @Path("page") page: Int,
        @Query("page_size") pageSize: Int= Constants.API_PAGE_SIZE
    ): ResponseBody

    /**
     * 轮播图
     */
    @GET("banner/json")
    suspend fun banner(
    ): ResponseBody

    /**
     * 热词
     */
    @GET("hotkey/json")
    suspend fun searchHotKey(): ResponseBody

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