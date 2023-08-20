package com.dokiwei.wanandroid.model.apidata

/**
 * @author DokiWei
 * @date 2023/7/10 0:11
 */
data class LoginData(
    val isLoggedIn: Boolean,
    val isRememberPassword: Boolean,
    val username: String?,
    val password: String?
)
