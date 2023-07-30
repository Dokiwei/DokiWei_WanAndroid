package com.dokiwei.wanandroid.network.repository

import com.dokiwei.wanandroid.bean.ArticleBean
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
 * @date 2023/7/27 18:31
 */
class SearchRepo {
    private val json = Json { ignoreUnknownKeys = true }

    suspend fun search(page: Int, k: String): Result<List<ArticleBean>> {
        return try {
            val response = RetrofitClient.searchApi.search(page, k)
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

    suspend fun hotKey(): Result<List<HotKeyBean>> {
        return try {
            val response = RetrofitClient.searchApi.getSearchHotKey()
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