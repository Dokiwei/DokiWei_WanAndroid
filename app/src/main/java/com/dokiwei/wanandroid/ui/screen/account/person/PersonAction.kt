package com.dokiwei.wanandroid.ui.screen.account.person

import com.dokiwei.wanandroid.model.apidata.UserInfoData

/**
 * @author DokiWei
 * @date 2023/7/27 17:26
 */
sealed class PersonAction {
    data class OutputLogcat(val msg: String) : PersonAction()
    data class SetUserInfo(val data: UserInfoData?) : PersonAction()
}