package com.dokiwei.wanandroid.ui.screens.login

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dokiwei.wanandroid.data.LoginState
import com.dokiwei.wanandroid.network.repository.LoginRepo
import com.dokiwei.wanandroid.util.LoginStateHelper
import com.dokiwei.wanandroid.util.ToastUtil
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

/**
 * @author DokiWei
 * @date 2023/7/7 18:00
 */
class LoginViewModel : ViewModel() {
    private val loginRepository = LoginRepo()

    //状态
    private val _loginState = MutableStateFlow(LoginViewState())
    val loginState = _loginState
    fun updateRememberPasswordChecked(checked: Boolean) {
        _loginState.value = _loginState.value.copy(isRememberPasswordChecked = checked)
    }

    //ui文字格式是否正确
    private val _loginIsError = MutableStateFlow(LoginIsError())
    val loginIsError = _loginIsError

    //检查文字格式
    fun checkText(text: String, field: String) {
        val isEmpty = text.isEmpty()
        when (field) {
            "name" -> _loginIsError.value = _loginIsError.value.copy(name = isEmpty)
            "psd" -> _loginIsError.value = _loginIsError.value.copy(psd = isEmpty)
        }
    }


    //获取已保存的用户数据
    fun getUserData(context: Context): LoginState {
        return LoginStateHelper.getLoginState(context)
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

    //登录
    fun login(context: Context, username: String, password: String) {
        _loginState.value = _loginState.value.copy(isLoading = true)
        val message = when {
            _loginIsError.value.name -> "用户名不能为空!!!"
            _loginIsError.value.psd -> "密码不能为空!!!"
            else -> null
        }
        if (message != null) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            _loginState.value = _loginState.value.copy(isLoading = false)
        } else {
            viewModelScope.launch {
                val result = loginRepository.login(username, password)
                _loginState.value = _loginState.value.copy(isLoading = false)
                if (result.isSuccess){
                    _loginState.value =_loginState.value.copy(isLoggedIn = true)
                }else{
                    loginState.value = _loginState.value.copy(isLoggedIn = false)
                    ToastUtil.showMsg(context,"登录失败:${result.exceptionOrNull()}")
                }
            }
        }
    }


}