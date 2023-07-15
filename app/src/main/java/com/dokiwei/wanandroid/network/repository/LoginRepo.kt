package com.dokiwei.wanandroid.network.repository

import com.dokiwei.wanandroid.network.client.RetrofitClient
import org.json.JSONObject

/**
 * @author DokiWei
 * @date 2023/7/9 19:08
 */
class LoginRepo {
    suspend fun login(username: String, password: String, callback: (Boolean) -> Unit) {
        callback(
            JSONObject(
                RetrofitClient.loginApi.login(username, password).string()
            ).getInt("errorCode") == 0
        )
    }
}
