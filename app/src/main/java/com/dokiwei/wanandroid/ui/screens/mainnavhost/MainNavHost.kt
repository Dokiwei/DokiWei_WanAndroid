package com.dokiwei.wanandroid.ui.screens.mainnavhost

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.dokiwei.wanandroid.R
import com.dokiwei.wanandroid.ui.animation.ScreenAnim
import com.dokiwei.wanandroid.ui.screens.home.HomeScreen
import com.dokiwei.wanandroid.ui.screens.login.LoginScreen
import com.dokiwei.wanandroid.ui.screens.person.PersonScreen
import com.dokiwei.wanandroid.ui.screens.person.mylike.MyLike
import com.dokiwei.wanandroid.ui.screens.project.ProjectScreen
import com.dokiwei.wanandroid.ui.screens.register.RegisterScreen
import com.dokiwei.wanandroid.ui.screens.startscreen.StartScreen
import com.dokiwei.wanandroid.ui.screens.webview.WebViewScreen

/**
 * @author DokiWei
 * @date 2023/7/7 17:08
 */
@Composable
fun MainNavHost() {
    val viewModel: MainNavHostViewModel = viewModel()
    val navController = rememberNavController()
    var selectedIndex by remember { mutableIntStateOf(0) }
    val showBottomBar by viewModel.navBottomBar.collectAsState()
    val navBackStackEntry by navController.currentBackStackEntryAsState()//获取当前的页面状态
    navBackStackEntry?.destination?.route?.let { viewModel.changeBottomBarSelectedItem(it) }
    Scaffold(
        bottomBar = {
            val items = listOf(
                "主页",
                "项目",
                "我的"
            )
            AnimatedVisibility(
                visible = showBottomBar,
                enter = slideInVertically { it },
                exit = slideOutVertically { it },
                content = {
                    Box {
                        navController.addOnDestinationChangedListener { _, destination, _ ->
                            when (destination.route) {
                                "主页" -> {
                                    selectedIndex = 0
                                }
                                "项目" ->{
                                    selectedIndex = 1
                                }
                                "我的" -> {
                                    selectedIndex = 2
                                }
                            }
                        }
                        NavigationBar {
                            items.forEachIndexed { index, item ->
                                NavigationBarItem(
                                    icon = {
                                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                            Icon(
                                                imageVector = if (index == 0) Icons.Rounded.Home else if (index==1)ImageVector.vectorResource(R.drawable.ic_project) else Icons.Rounded.Person,
                                                contentDescription = null,
                                                modifier = Modifier.size(24.dp),
                                                tint = if (selectedIndex == index) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline,
                                            )
                                        }
                                    },
                                    label = {
                                        Text(
                                            text = item,
                                            textAlign = TextAlign.Center,
                                        )
                                    },
                                    alwaysShowLabel = true,
                                    selected = selectedIndex == index,
                                    onClick = {
                                        selectedIndex = index
                                        navController.navigate(item) {
                                            // 弹出到图表的起始目的地
                                            // 避免建立大量目标
                                            // 在用户选择项目时的后退堆栈上
                                            popUpTo("主页") {
                                                saveState = true
                                            }
                                            // 避免在以下情况下使用同一目标的多个副本
                                            // 重新选择同一项目
                                            launchSingleTop = true
                                            // 重新选择以前选定的项目时恢复状态
                                            restoreState = true
                                        }
                                    }
                                )
                            }
                        }
                    }
                }
            )
        }
    ) {
        NavHost(
            navController = navController,
            startDestination = "启动页",
            modifier = Modifier.padding(it)
        ) {
            composable(//动画启动页
                route = "启动页",
                enterTransition = ScreenAnim.enterScreenAnim(),
                exitTransition = ScreenAnim.exitScreenAnim(),
                popEnterTransition = ScreenAnim.popEnterScreenAnim(),
                popExitTransition = ScreenAnim.popExitScreenAnim()
            ) {
                StartScreen(navController)
            }
            composable(
                route = "登录页",
                enterTransition = ScreenAnim.enterScreenAnim(),
                exitTransition = ScreenAnim.exitScreenAnim(),
                popEnterTransition = ScreenAnim.popEnterScreenAnim(),
                popExitTransition = ScreenAnim.popExitScreenAnim()
            ) {
                LoginScreen(navController)
            }
            composable(
                route = "注册页",
                enterTransition = ScreenAnim.enterScreenAnim(),
                exitTransition = ScreenAnim.exitScreenAnim(),
                popEnterTransition = ScreenAnim.popEnterScreenAnim(),
                popExitTransition = ScreenAnim.popExitScreenAnim()
            ) {
                RegisterScreen(navController)
            }
            composable(
                route = "主页",
                enterTransition = ScreenAnim.enterScreenAnim(),
                exitTransition = ScreenAnim.exitScreenAnim(),
                popEnterTransition = ScreenAnim.popEnterScreenAnim(),
                popExitTransition = ScreenAnim.popExitScreenAnim()
            ) {
                HomeScreen(navController)
            }
            composable(
                route = "我的",
                enterTransition = ScreenAnim.enterScreenAnim(),
                exitTransition = ScreenAnim.exitScreenAnim(),
                popEnterTransition = ScreenAnim.popEnterScreenAnim(),
                popExitTransition = ScreenAnim.popExitScreenAnim()
            ) {
                PersonScreen(navController)
            }
            composable(
                route = "项目",
                enterTransition = ScreenAnim.enterScreenAnim(),
                exitTransition = ScreenAnim.exitScreenAnim(),
                popEnterTransition = ScreenAnim.popEnterScreenAnim(),
                popExitTransition = ScreenAnim.popExitScreenAnim()
            ) {
                ProjectScreen(navController)
            }
            composable(
                route = "网页/{link}",
                enterTransition = ScreenAnim.enterScreenAnim(),
                exitTransition = ScreenAnim.exitScreenAnim(),
                popEnterTransition = ScreenAnim.popEnterScreenAnim(),
                popExitTransition = ScreenAnim.popExitScreenAnim(),
                arguments = listOf(navArgument("link") { type = NavType.StringType })
            ) { navBackStackEntry ->
                navBackStackEntry.arguments?.getString("link")?.let { string ->
                    WebViewScreen(link = string)
                }
            }
            composable(
                route = "我的收藏",
                enterTransition = ScreenAnim.enterScreenAnim(),
                exitTransition = ScreenAnim.exitScreenAnim(),
                popEnterTransition = ScreenAnim.popEnterScreenAnim(),
                popExitTransition = ScreenAnim.popExitScreenAnim()
            ) {
                MyLike(navController)
            }
        }
    }
}
