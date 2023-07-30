package com.dokiwei.wanandroid.network.repository

import com.dokiwei.wanandroid.bean.ArticleBean
import com.dokiwei.wanandroid.network.client.RetrofitClient
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

/**
 * @author DokiWei
 * @date 2023/7/14 23:04
 */
class QaRepo {
    private val json = Json { ignoreUnknownKeys = true }

    suspend fun getQa(page: Int): Result<List<ArticleBean>> {
        return try {
            val response = RetrofitClient.qaApi.qaApi(page)
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
}