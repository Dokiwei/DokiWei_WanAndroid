package com.dokiwei.wanandroid.ui.screens.person

import com.dokiwei.wanandroid.bean.UserInfoBean

/**
 * @author DokiWei
 * @date 2023/7/27 17:26
 */
sealed class PersonAction {
    data class OutputLogcat(val msg: String) : PersonAction()
    data class SetUserInfo(val data: UserInfoBean?) : PersonAction()
}