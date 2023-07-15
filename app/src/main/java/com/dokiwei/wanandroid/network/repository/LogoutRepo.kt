package com.dokiwei.wanandroid.network.repository

import com.dokiwei.wanandroid.network.client.RetrofitClient
import org.json.JSONObject

/**
 * @author DokiWei
 * @date 2023/7/9 23:46
 */
class LogoutRepo {
    suspend fun logout(callback: (Boolean) -> Unit) {
        val response = RetrofitClient.logoutApi.logout()
        val responseBody = response.string()
        val json = JSONObject(responseBody)
        val errorCode = json.getInt("errorCode")
        callback(errorCode == 0)
    }
}