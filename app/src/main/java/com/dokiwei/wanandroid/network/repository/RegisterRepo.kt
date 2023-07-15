package com.dokiwei.wanandroid.network.repository

import com.dokiwei.wanandroid.network.client.RetrofitClient
import org.json.JSONObject

/**
 * @author DokiWei
 * @date 2023/7/9 22:51
 */
class RegisterRepo {
    suspend fun register(
        username: String,
        password: String,
        rePassword: String,
        callback: (Boolean) -> Unit
    ) {
        callback(
            JSONObject(
                RetrofitClient.registerApi.register(username, password, rePassword).string()
            ).getInt("errorCode") == 0
        )
    }
}