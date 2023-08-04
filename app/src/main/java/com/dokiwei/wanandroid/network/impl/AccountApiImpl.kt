package com.dokiwei.wanandroid.network.impl

import com.dokiwei.wanandroid.bean.UserInfoBean
import com.dokiwei.wanandroid.network.client.RetrofitClient
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import org.json.JSONObject

/**
 * @author DokiWei
 * @date 2023/7/9 19:08
 */
class AccountApiImpl {
    suspend fun login(username: String, password: String): Result<Boolean> {
        return try {
            val response = RetrofitClient.accountApi.login(username, password)
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


    suspend fun logout(): Result<Boolean> {
        return try {
            val response = RetrofitClient.accountApi.logout()
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

    suspend fun register(
        username: String,
        password: String,
        rePassword: String
    ): Result<Boolean> {
        return try {
            val response = RetrofitClient.accountApi.register(username, password, rePassword)
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

    private val json = Json { ignoreUnknownKeys = true }

    suspend fun getUserInfo(): Result<UserInfoBean> {
        return try {
            val response = RetrofitClient.accountApi.userInfo()
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
