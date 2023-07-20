package com.dokiwei.wanandroid.network.api

import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * @author DokiWei
 * @date 2023/7/15 19:16
 */
interface ProjectApi {
    //获取项目
    @GET("project/list/{page}/json")
    suspend fun getProjectList(
        @Path("page") page: Int,
        @Query("cid") id: Int
    ): ResponseBody

    //获取项目标题
    @GET("project/tree/json")
    suspend fun getProjectTitle():ResponseBody
}
