package com.dokiwei.wanandroid.ui.screens.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
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
import androidx.compose.runtime.mutableStateOf
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
import com.dokiwei.wanandroid.ui.screens.person.collect.MyLike
import com.dokiwei.wanandroid.ui.screens.project.ProjectScreen
import com.dokiwei.wanandroid.ui.screens.register.RegisterScreen
import com.dokiwei.wanandroid.ui.screens.search.SearchScreen
import com.dokiwei.wanandroid.ui.screens.startscreen.StartScreen
import com.dokiwei.wanandroid.ui.screens.tree.TreeScreen
import com.dokiwei.wanandroid.ui.screens.webview.WebViewScreen
import com.dokiwei.wanandroid.util.myCustomNavigate

/**
 * @author DokiWei
 * @date 2023/7/7 17:08
 */
@Composable
fun MainNavHost() {
    val viewModel: NavigationViewModel = viewModel()
    val navController = rememberNavController()
    var selectedIndex by remember { mutableIntStateOf(0) }
    val showBottomBar by viewModel.navBottomBar.collectAsState()
    val navBackStackEntry by navController.currentBackStackEntryAsState()//获取当前的页面状态
    val currentRoute = navBackStackEntry?.destination?.route
    currentRoute?.let { viewModel.changeBottomBarSelectedItem(it) }
    navController.addOnDestinationChangedListener { _, destination, _ ->
        when (destination.route) {
            "主页" -> {
                selectedIndex = 0
            }

            "项目" -> {
                selectedIndex = 1
            }

            "体系" -> {
                selectedIndex = 2
            }

            "我的" -> {
                selectedIndex = 3
            }
        }
    }
    Scaffold(
        bottomBar = {
            val items = listOf(
                "主页",
                "项目",
                "体系",
                "我的"
            )
            AnimatedVisibility(
                visible = showBottomBar,
                enter = slideInVertically {
                    it
                },
                exit = slideOutVertically {
                    it
                }
            ) {
                Box {
                    NavigationBar {
                        items.forEachIndexed { index, item ->
                            var iconSize by remember {
                                mutableStateOf(24.dp)
                            }
                            val iconAnimSize by animateDpAsState(
                                targetValue = iconSize, label = "", animationSpec = spring(
                                    Spring.DampingRatioHighBouncy
                                )
                            )
                            iconSize = if (selectedIndex == index)
                                30.dp
                            else
                                24.dp
                            NavigationBarItem(
                                icon = {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Icon(
                                            imageVector = when (index) {
                                                0 -> Icons.Rounded.Home
                                                1 -> ImageVector.vectorResource(
                                                    R.drawable.ic_project
                                                )
                                                2-> ImageVector.vectorResource(
                                                    R.drawable.ic_tree
                                                )
                                                else -> Icons.Rounded.Person
                                            },
                                            contentDescription = null,
                                            modifier = Modifier.size(iconAnimSize),
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
                                alwaysShowLabel = false,
                                selected = selectedIndex == index,
                                onClick = {
                                    selectedIndex = index
                                    navController.myCustomNavigate(item)
                                }
                            )
                        }
                    }
                }
            }
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
                route = "项目",
                enterTransition = ScreenAnim.enterScreenAnim(),
                exitTransition = ScreenAnim.exitScreenAnim(),
                popEnterTransition = ScreenAnim.popEnterScreenAnim(),
                popExitTransition = ScreenAnim.popExitScreenAnim()
            ) {
                ProjectScreen(navController)
            }
            composable(
                route = "体系",
                enterTransition = ScreenAnim.enterScreenAnim(),
                exitTransition = ScreenAnim.exitScreenAnim(),
                popEnterTransition = ScreenAnim.popEnterScreenAnim(),
                popExitTransition = ScreenAnim.popExitScreenAnim()
            ) {
                TreeScreen(navController)
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
                route = "网页/{link}",
                enterTransition = ScreenAnim.enterScreenAnim(),
                exitTransition = ScreenAnim.exitScreenAnim(),
                popEnterTransition = ScreenAnim.popEnterScreenAnim(),
                popExitTransition = ScreenAnim.popExitScreenAnim(),
                arguments = listOf(navArgument("link") { type = NavType.StringType })
            ) { navBackStackEntry ->
                navBackStackEntry.arguments?.getString("link")?.let { string ->
                    WebViewScreen(string, navController)
                }
            }
            composable(
                route = "搜索页",
                enterTransition = ScreenAnim.enterScreenAnim(),
                exitTransition = ScreenAnim.exitScreenAnim(),
                popEnterTransition = ScreenAnim.popEnterScreenAnim(),
                popExitTransition = ScreenAnim.popExitScreenAnim()
            ) {
                SearchScreen(navController)
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
