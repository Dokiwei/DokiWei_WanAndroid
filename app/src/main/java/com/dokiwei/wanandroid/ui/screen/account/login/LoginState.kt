package com.dokiwei.wanandroid.ui.screen.account.login

import com.dokiwei.wanandroid.model.apidata.LoginData

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
    val rememberLoginSate: LoginData = LoginData(
        isLoggedIn = false,
        isRememberPassword = false,
        null,
        null
    ),
    val errorMsg: String = ""
)
