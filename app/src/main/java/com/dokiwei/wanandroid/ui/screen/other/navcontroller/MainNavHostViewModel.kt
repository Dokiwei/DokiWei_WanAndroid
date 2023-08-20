package com.dokiwei.wanandroid.ui.screen.other.navcontroller

import androidx.lifecycle.ViewModel
import com.dokiwei.wanandroid.model.util.AccountScreen
import com.dokiwei.wanandroid.model.util.HomeScreen
import com.dokiwei.wanandroid.model.util.NavigationScreen
import com.dokiwei.wanandroid.model.util.ProjectScreen
import kotlinx.coroutines.flow.MutableStateFlow

/**
 * @author DokiWei
 * @date 2023/7/7 17:17
 */
class MainNavHostViewModel :ViewModel(){
    private val _navBottomBar = MutableStateFlow(false)
    val navBottomBar = _navBottomBar
    fun changeBottomBarSelectedItem(route: String) {
        _navBottomBar.value = when (route) {
            HomeScreen.Home.route -> true // 在此屏幕上，底部栏应显示
            ProjectScreen.Project.route -> true // 这里也是
            NavigationScreen.Tree.route -> true // 这里也是
            AccountScreen.Account.route -> true // 这里也是
            else -> false // 在所有其他情况下隐藏底部栏
        }
    }
}