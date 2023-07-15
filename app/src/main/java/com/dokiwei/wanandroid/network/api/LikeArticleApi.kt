package com.dokiwei.wanandroid.network.api

import okhttp3.ResponseBody
import retrofit2.http.POST
import retrofit2.http.Path

/**
 * @author DokiWei
 * @date 2023/7/11 22:43
 */
interface LikeArticleApi {
    @POST("lg/collect/{id}/json")
    suspend fun likeArticle(
        @Path("id") id: Int
    ): ResponseBody
}