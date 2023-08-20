package com.dokiwei.wanandroid.network.api

import com.dokiwei.wanandroid.model.util.Constants
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * @author DokiWei
 * @date 2023/7/15 19:16
 */
interface ProjectApi {
    /**
     * 项目
     * @param page:页码(从0开始)
     * @param id:项目分类id
     */
    @GET("project/list/{page}/json")
    suspend fun getProjectList(
        @Path("page") page: Int,
        @Query("cid") id: Int,
        @Query("page_size") pageSize: Int= Constants.API_PAGE_SIZE
    ): ResponseBody

    /**
     * 项目分类
     */
    @GET("project/tree/json")
    suspend fun getProjectTitle():ResponseBody
}
