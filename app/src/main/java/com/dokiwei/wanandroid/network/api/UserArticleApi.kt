package com.dokiwei.wanandroid.network.api

import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * @author DokiWei
 * @date 2023/7/14 22:25
 */
interface UserArticleApi {
    @GET("user_article/list/{page}/json")
    suspend fun getUserArticleList(
        @Path("page") page: Int
    ): ResponseBody
}