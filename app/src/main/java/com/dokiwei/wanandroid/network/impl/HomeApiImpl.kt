package com.dokiwei.wanandroid.network.impl

import com.dokiwei.wanandroid.bean.ArticleBean
import com.dokiwei.wanandroid.bean.BannerBean
import com.dokiwei.wanandroid.bean.HotKeyBean
import com.dokiwei.wanandroid.network.client.RetrofitClient
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

/**
 * @author DokiWei
 * @date 2023/7/11 20:02
 */
class HomeApiImpl {
    private val json = Json { ignoreUnknownKeys = true }

    suspend fun getHomeArticle(page: Int): Result<List<ArticleBean>> {
        return try {
            val response = RetrofitClient.homeApi.homeArticle(page)
            val responseBody = response.string()
            val jsonElement = json.parseToJsonElement(responseBody).jsonObject
            val errorCode = jsonElement["errorCode"]?.jsonPrimitive?.int ?: -1
            if (errorCode == 0) {
                val dataJson = jsonElement["data"]?.jsonObject?.get("datas")?.jsonArray
                    ?: error("Missing data field")
                val articleList = json.decodeFromJsonElement<List<ArticleBean>>(dataJson)
                Result.success(articleList)
            } else {
                Result.failure(Exception("Error code: $errorCode"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getBanner(): Result<List<BannerBean>> {
        return try {
            val response = RetrofitClient.homeApi.banner()
            val responseBody = response.string()
            val jsonElement = json.parseToJsonElement(responseBody).jsonObject
            val errorCode = jsonElement["errorCode"]?.jsonPrimitive?.int ?: -1
            if (errorCode == 0) {
                val dataJson = jsonElement["data"]?.jsonArray ?: error("Missing data field")
                val bannerList = json.decodeFromJsonElement<List<BannerBean>>(dataJson)
                Result.success(bannerList)
            } else {
                val errorMsg = jsonElement["errorMsg"]?.jsonPrimitive?.content ?: "获取数据为空"
                Result.failure(Exception("Error code: $errorCode \nError Msg: $errorMsg"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getQa(page: Int): Result<List<ArticleBean>> {
        return try {
            val response = RetrofitClient.homeApi.qaArticle(page)
            val responseBody = response.string()
            val jsonElement = json.parseToJsonElement(responseBody).jsonObject
            val errorCode = jsonElement["errorCode"]?.jsonPrimitive?.int ?: -1
            if (errorCode == 0) {
                val dataJson = jsonElement["data"]?.jsonObject?.get("datas")?.jsonArray
                    ?: error("Missing data field")
                val qaList = json.decodeFromJsonElement<List<ArticleBean>>(dataJson)
                Result.success(qaList)
            } else {
                Result.failure(Exception("Error code: $errorCode"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getSquareArticle(page: Int): Result<List<ArticleBean>> {
        return try {
            val response = RetrofitClient.homeApi.squareArticle(page)
            val responseBody = response.string()
            val jsonElement = json.parseToJsonElement(responseBody).jsonObject
            val errorCode = jsonElement["errorCode"]?.jsonPrimitive?.int ?: -1
            if (errorCode == 0) {
                val dataJson = jsonElement["data"]?.jsonObject?.get("datas")?.jsonArray
                    ?: error("Missing data field")
                val articleList = json.decodeFromJsonElement<List<ArticleBean>>(dataJson)
                Result.success(articleList)
            } else {
                Result.failure(Exception("Error code: $errorCode"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun search(page: Int, k: String): Result<List<ArticleBean>> {
        return try {
            val response = RetrofitClient.homeApi.search(page, k)
            val responseBody = response.string()
            val jsonElement = json.parseToJsonElement(responseBody).jsonObject
            val errorCode = jsonElement["errorCode"]?.jsonPrimitive?.int ?: -1
            if (errorCode == 0) {
                val dataJson = jsonElement["data"]?.jsonObject?.get("datas")?.jsonArray
                    ?: error("Missing data field")
                val articleList = json.decodeFromJsonElement<List<ArticleBean>>(dataJson)
                Result.success(articleList)
            } else {
                Result.failure(Exception("Error code: $errorCode"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getHotKey(): Result<List<HotKeyBean>> {
        return try {
            val response = RetrofitClient.homeApi.searchHotKey()
            val responseBody = response.string()
            val jsonElement = json.parseToJsonElement(responseBody).jsonObject
            val errorCode = jsonElement["errorCode"]?.jsonPrimitive?.int ?: -1
            if (errorCode == 0) {
                val dataJson = jsonElement["data"]?.jsonArray ?: error("Missing data field")
                val hotKeys = json.decodeFromJsonElement<List<HotKeyBean>>(dataJson)
                Result.success(hotKeys)
            } else {
                val errorMsg = jsonElement["errorMsg"]?.jsonPrimitive?.toString() ?: ""
                Result.failure(Exception("Error code: $errorCode  Error Msg: $errorMsg"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

