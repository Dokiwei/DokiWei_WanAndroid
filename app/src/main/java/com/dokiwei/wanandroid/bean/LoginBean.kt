package com.dokiwei.wanandroid.bean

/**
 * @author DokiWei
 * @date 2023/7/10 0:11
 */
data class LoginBean(
    val isLoggedIn: Boolean,
    val isRememberPassword: Boolean,
    val username: String?,
    val password: String?
)
