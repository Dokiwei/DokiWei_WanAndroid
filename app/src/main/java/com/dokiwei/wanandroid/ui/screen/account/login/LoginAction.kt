package com.dokiwei.wanandroid.ui.screen.account.login

import com.dokiwei.wanandroid.model.apidata.LoginData

/**
 * @author DokiWei
 * @date 2023/7/23 0:30
 */
sealed class LoginAction {
    object LoginStarted : LoginAction()
    object LoginSuccess : LoginAction()
    data class LoginFailed(val error: Throwable?) : LoginAction()
    data class RememberPasswordCheckedChanged(val checked: Boolean) : LoginAction()
    data class GetLoginState(val loginData: LoginData) : LoginAction()
    data class CheckText(val text: String, val field: String) : LoginAction()
    data class ShowTextFailed(val msg: String) : LoginAction()
}
