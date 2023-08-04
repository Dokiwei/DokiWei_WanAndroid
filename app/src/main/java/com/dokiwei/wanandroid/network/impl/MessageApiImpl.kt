package com.dokiwei.wanandroid.network.impl

import com.dokiwei.wanandroid.bean.MessageBean
import com.dokiwei.wanandroid.network.client.RetrofitClient
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

/**
 * @author DokiWei
 * @date 2023/7/20 17:34
 */
class MessageApiImpl {
    private val json = Json { ignoreUnknownKeys = true }

    suspend fun getUnreadQuantity() {

    }

    suspend fun getRead(page: Int): Result<List<MessageBean>> {
        return try {
            val response = RetrofitClient.messageApi.getRead(page)
            val responseBody = response.string()
            val jsonElement = json.parseToJsonElement(responseBody).jsonObject
            val errorCode = jsonElement["errorCode"]?.jsonPrimitive?.int ?: -1
            if (errorCode == 0) {
                val dataJson = jsonElement["data"]?.jsonObject?.get("datas")?.jsonArray
                    ?: error("Missing data field")
                val message = json.decodeFromJsonElement<List<MessageBean>>(dataJson)
                Result.success(message)
            } else {
                Result.failure(Exception("Error code: $errorCode"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getUnread(page: Int): Result<List<MessageBean>> {
        return try {
            val response = RetrofitClient.messageApi.getUnread(page)
            val responseBody = response.string()
            val jsonElement = json.parseToJsonElement(responseBody).jsonObject
            val errorCode = jsonElement["errorCode"]?.jsonPrimitive?.int ?: -1
            if (errorCode == 0) {
                val dataJson = jsonElement["data"]?.jsonObject?.get("datas")?.jsonArray
                    ?: error("Missing data field")
                val message = json.decodeFromJsonElement<List<MessageBean>>(dataJson)
                Result.success(message)
            } else {
                Result.failure(Exception("Error code: $errorCode"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}