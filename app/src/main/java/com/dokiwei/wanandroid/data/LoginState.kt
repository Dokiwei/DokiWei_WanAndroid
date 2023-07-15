package com.dokiwei.wanandroid.data

/**
 * @author DokiWei
 * @date 2023/7/10 0:11
 */
data class LoginState(
    val isLoggedIn: Boolean,
    val isRememberPassword: Boolean,
    val username: String?,
    val password: String?
)
