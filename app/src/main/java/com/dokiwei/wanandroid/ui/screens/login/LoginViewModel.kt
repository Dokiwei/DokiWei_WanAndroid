package com.dokiwei.wanandroid.ui.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dokiwei.wanandroid.network.impl.AccountApiImpl
import com.dokiwei.wanandroid.util.LoginStateHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * @author DokiWei
 * @date 2023/7/7 18:00
 */

class LoginViewModel : ViewModel() {
    private val loginRepository = AccountApiImpl()


    private val _loginState = MutableStateFlow(LoginState())
    val loginState: StateFlow<LoginState> = _loginState


    init {
        handleAction(LoginAction.GetLoginState(LoginStateHelper.getLoginState()))
    }

    fun dispatch(intent: LoginIntent) {
        when (intent) {
            is LoginIntent.Login -> login(intent.username, intent.password)
            is LoginIntent.UpdateRememberPasswordChecked -> updateRememberPasswordChecked(intent.checked)
            is LoginIntent.SaveUserData -> LoginStateHelper.saveLoginState(
                intent.isLoggedIn,
                intent.isChecked,
                intent.username,
                intent.password
            )

            is LoginIntent.CheckText -> checkText(intent.text, intent.field)
        }
    }

    private fun handleAction(action: LoginAction) {
        when (action) {
            is LoginAction.LoginStarted -> {
                _loginState.value = _loginState.value.copy(isLoading = true)
            }

            is LoginAction.LoginSuccess -> {
                _loginState.value = _loginState.value.copy(isLoading = false, isLoggedIn = true)
            }

            is LoginAction.LoginFailed -> {
                _loginState.value =
                    _loginState.value.copy(isLoading = false, errorMsg = action.error.toString())
            }

            is LoginAction.RememberPasswordCheckedChanged -> {
                _loginState.value =
                    _loginState.value.copy(isRememberPasswordChecked = action.checked)
            }

            is LoginAction.GetLoginState -> _loginState.value =
                _loginState.value.copy(rememberLoginSate = action.loginBean)

            is LoginAction.CheckText -> {
                val isEmpty = action.text.isEmpty()
                when (action.field) {
                    "name" -> _loginState.value =
                        _loginState.value.copy(check = _loginState.value.check.copy(name = isEmpty))

                    "psd" -> _loginState.value =
                        _loginState.value.copy(check = _loginState.value.check.copy(psd = isEmpty))
                }
            }

            is LoginAction.ShowTextFailed -> _loginState.value =
                _loginState.value.copy(errorMsg = action.msg)
        }
    }

    private fun updateRememberPasswordChecked(checked: Boolean) {
        handleAction(LoginAction.RememberPasswordCheckedChanged(checked))
    }

    //检查文字格式
    private fun checkText(text: String, field: String) {
        handleAction(LoginAction.CheckText(text, field))
    }

    //登录
    private fun login(username: String, password: String) {
        when {
            username.isEmpty() -> handleAction(LoginAction.ShowTextFailed("用户名不能为空!!!"))
            password.isEmpty() -> handleAction(LoginAction.ShowTextFailed("密码不能为空!!!"))
            else -> viewModelScope.launch {
                handleAction(LoginAction.LoginStarted)
                val result = loginRepository.login(username, password)
                if (result.isSuccess) handleAction(LoginAction.LoginSuccess)
                else handleAction(LoginAction.LoginFailed(result.exceptionOrNull()))
            }
        }
    }

}
