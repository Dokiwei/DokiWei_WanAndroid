package com.dokiwei.wanandroid.network.repository

import com.dokiwei.wanandroid.data.MyLikeData
import com.dokiwei.wanandroid.network.client.RetrofitClient
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import org.json.JSONObject

/**
 * @author DokiWei
 * @date 2023/7/11 22:47
 */
class CollectRepo {
    private val json = Json { ignoreUnknownKeys = true }

    suspend fun myLike(page: Int): Result<List<MyLikeData>> {
        return try {
            val response = RetrofitClient.collectApi.myLike(page)
            val responseBody = response.string()
            val jsonElement = json.parseToJsonElement(responseBody).jsonObject
            val errorCode = jsonElement["errorCode"]?.jsonPrimitive?.int ?: -1
            if (errorCode == 0) {
                val dataJson = jsonElement["data"]?.jsonObject?.get("datas")?.jsonArray
                    ?: error("Missing data field")
                val myLikeList = json.decodeFromJsonElement<List<MyLikeData>>(dataJson)
                Result.success(myLikeList)
            } else {
                Result.failure(Exception("Error code:$errorCode"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun like(id: Int): Result<Boolean> {
        return try {
            val response = RetrofitClient.collectApi.like(id)
            val responseBody = response.string()
            val jsonObject = JSONObject(responseBody)
            val errorCode = jsonObject.getInt("errorCode")
            if (errorCode == 0) {
                Result.success(true)
            } else {
                Result.failure(Exception("Error code: $errorCode"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun likeCustom(id:Int): Result<Boolean>{
        return try {
            val response = RetrofitClient.collectApi.likeCustom(id)
            val responseBody = response.string()
            val jsonObject = JSONObject(responseBody)
            val errorCode = jsonObject.getInt("errorCode")
            if (errorCode == 0) {
                Result.success(true)
            } else {
                Result.failure(Exception("Error code: $errorCode"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun unlike(id: Int): Result<Boolean> {
        return try {
            val response = RetrofitClient.collectApi.unlike(id)
            val responseBody = response.string()
            val jsonObject = JSONObject(responseBody)
            val errorCode = jsonObject.getInt("errorCode")
            if (errorCode == 0) {
                Result.success(true)
            } else {
                Result.failure(Exception("Error code: $errorCode"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun unlikeCustom(id: Int): Result<Boolean> {
        return try {
            val response = RetrofitClient.collectApi.unLikeCustom(id)
            val responseBody = response.string()
            val jsonObject = JSONObject(responseBody)
            val errorCode = jsonObject.getInt("errorCode")
            if (errorCode == 0) {
                Result.success(true)
            } else {
                Result.failure(Exception("Error code: $errorCode"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}