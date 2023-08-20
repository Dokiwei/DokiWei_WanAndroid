package com.dokiwei.wanandroid.model.util

import android.content.Context
import com.dokiwei.wanandroid.model.apidata.LoginData
import com.dokiwei.wanandroid.ui.main.MyApplication

/**
 * @author DokiWei
 * @date 2023/7/8 17:44
 */
// 保存登录状态
object LoginStateHelper {
    fun saveLoginState(
        isLoggedIn: Boolean,
        isChecked: Boolean,
        username: String,
        password: String
    ) {
        val sharedPreferences =
            MyApplication.context.getSharedPreferences("login_state", Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putBoolean("is_logged_in", isLoggedIn)
            putBoolean("is_remember_password", isChecked)
            putString("username", username)
            putString("password", password)
            apply()
        }
    }

    // 读取登录状态
    fun getLoginState(): LoginData {
        val sharedPreferences =
            MyApplication.context.getSharedPreferences("login_state", Context.MODE_PRIVATE)
        val isLoggedIn = sharedPreferences.getBoolean("is_logged_in", false)
        val isRememberPassword = sharedPreferences.getBoolean("is_remember_password", false)
        val username = sharedPreferences.getString("username", null)
        val password = sharedPreferences.getString("password", null)
        return LoginData(isLoggedIn, isRememberPassword, username, password)
    }

    fun saveLogoutState() {
        val sharedPreferences =
            MyApplication.context.getSharedPreferences("login_state", Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putBoolean("is_logged_in", false)
            apply()
        }
    }
}

