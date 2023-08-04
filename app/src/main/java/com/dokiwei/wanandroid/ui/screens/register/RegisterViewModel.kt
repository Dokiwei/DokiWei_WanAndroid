package com.dokiwei.wanandroid.ui.screens.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dokiwei.wanandroid.network.impl.AccountApiImpl
import com.dokiwei.wanandroid.util.LoginStateHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

/**
 * @author DokiWei
 * @date 2023/7/8 21:11
 */
class RegisterViewModel : ViewModel() {
    private val accountApiImpl = AccountApiImpl()

    //状态
    private val _registerState = MutableStateFlow(RegisterState())
    val registerState = _registerState


    fun dispatch(intent: RegisterIntent) {
        when (intent) {
            is RegisterIntent.Register -> register(
                intent.username, intent.password, intent.rePassword
            )

            is RegisterIntent.SaveUserData -> LoginStateHelper.saveLoginState(
                intent.isLoggedIn, intent.isChecked, intent.username, intent.password
            )

            is RegisterIntent.CheckText -> checkText(intent.text, intent.confirmPsd, intent.field)
        }
    }

    private fun handleAction(action: RegisterAction) {
        when (action) {
            is RegisterAction.LoginSuccess -> _registerState.value =
                _registerState.value.copy(isLoading = false)

            is RegisterAction.LoginFailed -> _registerState.value =
                _registerState.value.copy(isLoading = false, msg = action.error.toString())

            is RegisterAction.RegisterStarted -> _registerState.value =
                _registerState.value.copy(isLoading = true)

            is RegisterAction.RegisterFailed -> _registerState.value =
                _registerState.value.copy(isLoading = false, msg = action.error.toString())

            is RegisterAction.CheckText -> {
                action.confirmPsd?.takeIf { action.text.trim() == action.confirmPsd.trim() }?.let {
                    _registerState.value =
                        _registerState.value.copy(check = _registerState.value.check.copy(rePsd = true))
                }
                val isEmpty = action.text.isEmpty()
                when (action.field) {
                    "name" -> _registerState.value =
                        _registerState.value.copy(check = _registerState.value.check.copy(name = isEmpty))

                    "psd" -> _registerState.value =
                        _registerState.value.copy(check = _registerState.value.check.copy(psd = isEmpty))
                }
            }

            is RegisterAction.ShowToast -> _registerState.value =
                _registerState.value.copy(msg = action.msg)

            is RegisterAction.ShowLoadingProgress -> {}
        }
    }

    //检查文字格式
    private fun checkText(text: String, confirmPsd: String? = null, field: String? = null) {
        handleAction(RegisterAction.CheckText(text, confirmPsd, field))
    }

    //注册
    private fun register(name: String, psd: String, rePsd: String) {
        when {
            name.isEmpty() -> handleAction(RegisterAction.ShowToast("用户名不能为空!!!"))
            psd.isEmpty() -> handleAction(RegisterAction.ShowToast("密码不能为空!!!"))
            rePsd.isEmpty() -> handleAction(RegisterAction.ShowToast("确认密码不能为空!!!"))
            rePsd != psd -> handleAction(RegisterAction.ShowToast("两次密码输入不相同!!!"))
            else -> viewModelScope.launch {
                handleAction(RegisterAction.RegisterStarted)
                val registerResult = accountApiImpl.register(name, psd, rePsd)
                if (registerResult.isSuccess) {
                    val loginResult = accountApiImpl.login(name, psd)
                    if (loginResult.isSuccess) handleAction(RegisterAction.LoginSuccess)
                    else handleAction(RegisterAction.LoginFailed(loginResult.exceptionOrNull()))
                } else handleAction(RegisterAction.RegisterFailed(registerResult.exceptionOrNull()))
            }
        }
    }

}