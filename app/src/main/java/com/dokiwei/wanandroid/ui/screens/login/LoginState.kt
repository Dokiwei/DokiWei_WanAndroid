package com.dokiwei.wanandroid.ui.screens.login

import com.dokiwei.wanandroid.bean.LoginBean

/**
 * @author DokiWei
 * @date 2023/7/8 17:28
 */
data class LoginIsError(
    val name: Boolean = false,
    val psd: Boolean = false
)

data class LoginState(
    val isLoading: Boolean = false,
    val isLoggedIn: Boolean = false,
    val isRememberPasswordChecked: Boolean = false,
    val check: LoginIsError = LoginIsError(),
    val rememberLoginSate: LoginBean = LoginBean(
        isLoggedIn = false,
        isRememberPassword = false,
        null,
        null
    ),
    val errorMsg: String = ""
)
