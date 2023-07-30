package com.dokiwei.wanandroid.network.repository

import com.dokiwei.wanandroid.bean.UserInfoBean
import com.dokiwei.wanandroid.network.client.RetrofitClient
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

/**
 * @author DokiWei
 * @date 2023/7/13 16:55
 */
class UserInfoRepo {
    private val json = Json { ignoreUnknownKeys = true }

    suspend fun getUserInfo(): Result<UserInfoBean> {
        return try {
            val response = RetrofitClient.userInfoApi.userInfo()
            val responseBody = response.string()
            val jsonElement = json.parseToJsonElement(responseBody).jsonObject
            val errorCode = jsonElement["errorCode"]?.jsonPrimitive?.int ?: -1
            if (errorCode == 0) {
                val dataJson = jsonElement["data"]?.jsonObject ?: error("Missing data field")
                val userInfo = json.decodeFromJsonElement<UserInfoBean>(dataJson)
                Result.success(userInfo)
            } else {
                Result.failure(Exception("Error code: $errorCode"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}