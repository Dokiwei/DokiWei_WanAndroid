package com.dokiwei.wanandroid.ui.screens.login

/**
 * @author DokiWei
 * @date 2023/7/8 17:28
 */
data class LoginViewState(
    val isLoggedIn: Boolean = false,
    val isLoading: Boolean = false,
    val isRememberPasswordChecked: Boolean = false
)
data class LoginIsError(
    val name:Boolean=false,
    val psd:Boolean=false
)