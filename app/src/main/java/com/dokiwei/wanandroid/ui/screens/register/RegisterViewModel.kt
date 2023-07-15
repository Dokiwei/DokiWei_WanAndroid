package com.dokiwei.wanandroid.ui.screens.register

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dokiwei.wanandroid.network.repository.LoginRepo
import com.dokiwei.wanandroid.network.repository.RegisterRepo
import com.dokiwei.wanandroid.util.LoginStateHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

/**
 * @author DokiWei
 * @date 2023/7/8 21:11
 */
class RegisterViewModel : ViewModel() {
    private val registerRepository = RegisterRepo()
    private val loginRepository = LoginRepo()

    //状态
    private val _registerState = MutableStateFlow(RegisterState())
    val registerState = _registerState
    private val _registerIsError = MutableStateFlow(RegisterIsError())
    val registerIsError = _registerIsError

    //检查文字格式
    fun checkText(text: String, confirmPsd: String? = null, field: String? = null) {
        confirmPsd?.takeIf { text.trim() == confirmPsd.trim() }
            ?.let { registerIsError.value = registerIsError.value.copy(rePsd = true) }
        val isEmpty = text.isEmpty()
        when (field) {
            "name" -> registerIsError.value = registerIsError.value.copy(name = isEmpty)
            "psd" -> registerIsError.value = registerIsError.value.copy(psd = isEmpty)
        }
    }

    //保存用户数据
    fun saveUserData(
        context: Context,
        isLoggedIn: Boolean,
        isChecked: Boolean,
        username: String,
        password: String
    ) {
        LoginStateHelper.saveLoginState(context, isLoggedIn, isChecked, username, password)
    }

    //注册
    fun register(context: Context, name: String, psd: String, rePsd: String) {
        _registerState.value = _registerState.value.copy(isLoading = true)
        val message = when {
            _registerIsError.value.name -> "用户名不能为空!!!"
            _registerIsError.value.psd -> "密码不能为空!!!"
            _registerIsError.value.rePsd -> "两次密码不相同!!!"
            else -> null
        }
        if (message != null) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        } else {
            viewModelScope.launch {
                registerRepository.register(name, psd, rePsd) {
                    _registerState.value = _registerState.value.copy(isLoading = false)
                    if (it) viewModelScope.launch {
                        loginRepository.login(name, psd) { loginSuccess ->
                            _registerState.value =
                                _registerState.value.copy(isSuccess = loginSuccess)
                        }
                    }
                }
            }
        }

    }

}