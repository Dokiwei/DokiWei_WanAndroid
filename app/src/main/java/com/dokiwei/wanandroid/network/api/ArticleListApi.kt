package com.dokiwei.wanandroid.network.api

import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * @author DokiWei
 * @date 2023/7/11 20:03
 */
interface ArticleListApi {
    @GET("article/list/{page}/json")
    suspend fun getArticleList(
        @Path("page") page: Int
    ): ResponseBody
}