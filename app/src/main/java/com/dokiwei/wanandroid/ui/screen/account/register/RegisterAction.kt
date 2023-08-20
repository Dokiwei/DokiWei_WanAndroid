package com.dokiwei.wanandroid.ui.screen.account.register


/**
 * @author DokiWei
 * @date 2023/7/26 0:49
 */
sealed class RegisterAction {
    object LoginSuccess : RegisterAction()
    data class LoginFailed(val error: Throwable?) : RegisterAction()
    object RegisterStarted : RegisterAction()
    data class RegisterFailed(val error: Throwable?) : RegisterAction()
    data class CheckText(
        val text: String,
        val confirmPsd: String? = null,
        val field: String? = null
    ) : RegisterAction()

    data class ShowToast(val msg: String) : RegisterAction()
    data class ShowLoadingProgress(val msg: String) : RegisterAction()
}