package com.dokiwei.wanandroid.ui.screens.person

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dokiwei.wanandroid.bean.UserInfoBean
import com.dokiwei.wanandroid.network.repository.LogoutRepo
import com.dokiwei.wanandroid.network.repository.UserInfoRepo
import com.dokiwei.wanandroid.util.LoginStateHelper
import com.dokiwei.wanandroid.util.ToastAndLogcatUtil
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

/**
 * @author DokiWei
 * @date 2023/7/13 15:59
 */
class PersonViewModel : ViewModel() {
    private val logoutRepository = LogoutRepo()
    private val userInfoRepo = UserInfoRepo()

    private val _userInfoList = MutableStateFlow<UserInfoBean?>(null)
    val userInfoList = _userInfoList

    //初始化
    init {
        getUserInfo()
    }

    fun dispatch(intent: PersonIntent) {
        when (intent) {
            is PersonIntent.Logout -> logout()
        }
    }

    private fun handleAction(action: PersonAction) {
        when (action) {
            is PersonAction.OutputLogcat -> ToastAndLogcatUtil.log("PersonAction", action.msg, 0)
            is PersonAction.SetUserInfo -> _userInfoList.value = action.data
        }
    }

    //获取用户信息
    private fun getUserInfo() {
        viewModelScope.launch {
            val result = userInfoRepo.getUserInfo()
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
            val result = logoutRepository.logout()
            if (result.isSuccess) {
                LoginStateHelper.saveLogoutState()
            } else {
                handleAction(PersonAction.OutputLogcat("退出登录失败:${result.exceptionOrNull()}"))
            }
        }
    }
}