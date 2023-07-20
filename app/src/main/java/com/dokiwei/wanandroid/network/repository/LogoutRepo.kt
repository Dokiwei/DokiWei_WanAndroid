package com.dokiwei.wanandroid.network.repository

import com.dokiwei.wanandroid.network.client.RetrofitClient
import org.json.JSONObject

/**
 * @author DokiWei
 * @date 2023/7/9 23:46
 */
class LogoutRepo {
    suspend fun logout(): Result<Boolean> {
        return try {
            val response = RetrofitClient.logoutApi.logout()
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