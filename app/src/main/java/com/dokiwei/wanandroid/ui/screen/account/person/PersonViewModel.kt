package com.dokiwei.wanandroid.ui.screen.account.person

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dokiwei.wanandroid.model.apidata.UserInfoData
import com.dokiwei.wanandroid.model.util.LoginStateHelper
import com.dokiwei.wanandroid.model.util.ToastAndLogcatUtil
import com.dokiwei.wanandroid.network.impl.AccountApiImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

/**
 * @author DokiWei
 * @date 2023/7/13 15:59
 */
class PersonViewModel : ViewModel() {
    private val accountApiImpl = AccountApiImpl()

    private val _userInfoList = MutableStateFlow<UserInfoData?>(null)
    val userInfoList = _userInfoList

    //初始化
    init {
        getUserInfo()
    }

    fun dispatch(intent: PersonIntent) {
        when (intent) {
            is PersonIntent.Logout -> {
                logout()
                _userInfoList.value = null
            }

            else -> {}
        }
    }

    private fun handleAction(action: PersonAction) {
        when (action) {
            is PersonAction.OutputLogcat -> ToastAndLogcatUtil.log("PersonAction", action.msg)
            is PersonAction.SetUserInfo -> _userInfoList.value = action.data
            else -> {}
        }
    }

    //获取用户信息
    private fun getUserInfo() {
        viewModelScope.launch {
            val result = accountApiImpl.getUserInfo()
            if (result.isSuccess) handleAction(PersonAction.SetUserInfo(result.getOrNull()))
            else handleAction(
                PersonAction.OutputLogcat(
                    "获取用户信息异常:${
                        result.exceptionOrNull().toString()
                    }"
                )
            )
        }
    }

    //登出
    private fun logout() {
        viewModelScope.launch {
            val result = accountApiImpl.logout()
            if (result.isSuccess) {
                LoginStateHelper.saveLogoutState()
            } else {
                handleAction(PersonAction.OutputLogcat("退出登录失败:${result.exceptionOrNull()}"))
            }
        }
    }
}