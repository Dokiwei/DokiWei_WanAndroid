package com.dokiwei.wanandroid.ui.screens.person

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dokiwei.wanandroid.data.UserInfoData
import com.dokiwei.wanandroid.network.repository.LogoutRepo
import com.dokiwei.wanandroid.network.repository.UserInfoRepo
import com.dokiwei.wanandroid.util.LoginStateHelper
import com.dokiwei.wanandroid.util.ToastUtil
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

/**
 * @author DokiWei
 * @date 2023/7/13 15:59
 */
class PersonViewModel : ViewModel() {
    private val logoutRepository = LogoutRepo()
    private val userInfoRepo = UserInfoRepo()

    private val _userInfoList = MutableStateFlow<UserInfoData?>(null)
    val userInfoList = _userInfoList

    //初始化
    init {
        getUserInfo()
    }

    //获取用户信息
    private fun getUserInfo() {
        viewModelScope.launch {
            val result = userInfoRepo.getUserInfo()
            if (result.isSuccess) {
                _userInfoList.value = result.getOrNull()
            } else {
                Log.e("", result.exceptionOrNull().toString())
            }
        }
    }

    //登出
    fun logout(context: Context) {
        viewModelScope.launch {
            val result = logoutRepository.logout()
            if (result.isSuccess) {
                ToastUtil.showMsg(context, "退出登录成功")
                LoginStateHelper.saveLogoutState(context)
            } else {
                ToastUtil.showMsg(context, "退出登录失败:${result.exceptionOrNull()}")
            }
        }
    }
}