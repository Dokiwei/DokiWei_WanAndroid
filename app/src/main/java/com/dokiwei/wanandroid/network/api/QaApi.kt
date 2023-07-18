package com.dokiwei.wanandroid.network.api

import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * @author DokiWei
 * @date 2023/7/14 23:02
 */
interface QaApi {
    @GET("wenda/list/{page}/json")
    suspend fun qaApi(
        @Path("page") page:Int
    ):ResponseBody
}