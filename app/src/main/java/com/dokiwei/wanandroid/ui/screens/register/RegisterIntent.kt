package com.dokiwei.wanandroid.ui.screens.register

/**
 * @author DokiWei
 * @date 2023/7/26 0:43
 */
sealed class RegisterIntent {
    data class Register(val username: String, val password: String, val rePassword: String) :
        RegisterIntent()

    data class CheckText(
        val text: String,
        val confirmPsd: String? = null,
        val field: String? = null
    ) : RegisterIntent()

    data class SaveUserData(
        val isLoggedIn: Boolean,
        val isChecked: Boolean,
        val username: String,
        val password: String
    ) : RegisterIntent()
}