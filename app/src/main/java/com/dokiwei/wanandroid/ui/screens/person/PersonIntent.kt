package com.dokiwei.wanandroid.ui.screens.person

/**
 * @author DokiWei
 * @date 2023/7/27 17:26
 */
sealed class PersonIntent {
    object Logout : PersonIntent()
}