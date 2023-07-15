package com.dokiwei.wanandroid.network.api

import okhttp3.ResponseBody
import retrofit2.http.GET

/**
 * @author DokiWei
 * @date 2023/7/15 17:54
 */
interface ProjectTitleApi {
    @GET("project/tree/json")
    suspend fun getProjectTitle():ResponseBody
}