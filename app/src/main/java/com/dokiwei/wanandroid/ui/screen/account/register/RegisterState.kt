package com.dokiwei.wanandroid.ui.screen.account.register

/**
 * @author DokiWei
 * @date 2023/7/8 21:14
 */
data class RegisterState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val msg: String = "",
    val loadingProgress: String = "",
    val check: RegisterIsError = RegisterIsError()
)
data class RegisterIsError(
    val name:Boolean=false,
    val psd:Boolean=false,
    val rePsd:Boolean=false,
)
