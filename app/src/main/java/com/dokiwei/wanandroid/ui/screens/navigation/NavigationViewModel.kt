package com.dokiwei.wanandroid.ui.screens.navigation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow

/**
 * @author DokiWei
 * @date 2023/7/7 17:17
 */
class NavigationViewModel :ViewModel(){
    private val _navBottomBar = MutableStateFlow(false)
    val navBottomBar = _navBottomBar
    fun changeBottomBarSelectedItem(route: String) {
        _navBottomBar.value = when (route) {
            "主页" -> true // 在此屏幕上，底部栏应显示
            "项目" -> true // 这里也是
            "体系" -> true // 这里也是
            "我的" -> true // 这里也是
            else -> false // 在所有其他情况下隐藏底部栏
        }
    }
}