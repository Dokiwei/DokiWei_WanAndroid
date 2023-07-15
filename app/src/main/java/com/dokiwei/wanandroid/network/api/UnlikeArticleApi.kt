package com.dokiwei.wanandroid.network.api

import okhttp3.ResponseBody
import retrofit2.http.POST
import retrofit2.http.Path

/**
 * @author DokiWei
 * @date 2023/7/11 23:07
 */
interface UnlikeArticleApi {
    @POST("lg/uncollect_originId/{id}/json")
    suspend fun unLike(
        @Path("id") id: Int
    ): ResponseBody
}