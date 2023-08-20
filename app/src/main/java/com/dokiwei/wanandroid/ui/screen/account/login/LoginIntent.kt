package com.dokiwei.wanandroid.ui.screen.account.login

/**
 * @author DokiWei
 * @date 2023/7/22 23:08
 */
sealed class LoginIntent {
    data class Login(val username: String, val password: String) : LoginIntent()
    data class UpdateRememberPasswordChecked(val checked: Boolean) : LoginIntent()
    data class SaveUserData(
        val isLoggedIn: Boolean,
        val isChecked: Boolean,
        val username: String,
        val password: String
    ) : LoginIntent()

    data class CheckText(val text: String, val field: String) : LoginIntent()
}
