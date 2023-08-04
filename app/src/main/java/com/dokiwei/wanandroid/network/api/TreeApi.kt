package com.dokiwei.wanandroid.network.api

import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * @author DokiWei
 * @date 2023/7/31 18:42
 */
interface TreeApi {
    /**
     * 体系树
     */
    @GET("tree/json")
    suspend fun tree(
    ): ResponseBody

    /**
     * 子体系文章
     * @param page:页码(从0开始)
     * @param id:父体系cid
     */
    @GET("article/list/{page}/json")
    suspend fun treeChildren(
        @Path("page") page: Int,
        @Query("cid") id: Int
    ):ResponseBody


    /**
     * 搜索作者
     * @param page:页码(从0开始)
     * @param author:根据该值传入的作者名字进行搜索
     */
    @GET("article/list/{page}/json")
    suspend fun searchAuthor(
        @Path("page") page: Int,
        @Query("author") author: String
    ):ResponseBody
}