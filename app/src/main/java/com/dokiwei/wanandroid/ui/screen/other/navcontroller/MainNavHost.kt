package com.dokiwei.wanandroid.ui.screen.other.navcontroller

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
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.dokiwei.wanandroid.R
import com.dokiwei.wanandroid.model.util.AccountScreen
import com.dokiwei.wanandroid.model.util.Constants
import com.dokiwei.wanandroid.model.util.HomeScreen
import com.dokiwei.wanandroid.model.util.NavigationScreen
import com.dokiwei.wanandroid.model.util.OtherScreen
import com.dokiwei.wanandroid.model.util.ProjectScreen
import com.dokiwei.wanandroid.model.util.myCustomNavigate
import com.dokiwei.wanandroid.model.util.publicViewModel
import com.dokiwei.wanandroid.ui.animation.ScreenAnim
import com.dokiwei.wanandroid.ui.main.MyApplication
import com.dokiwei.wanandroid.ui.main.PublicIntent
import com.dokiwei.wanandroid.ui.screen.account.coin.CoinScreen
import com.dokiwei.wanandroid.ui.screen.account.collect.MyLike
import com.dokiwei.wanandroid.ui.screen.account.login.LoginScreen
import com.dokiwei.wanandroid.ui.screen.account.message.MessageScreen
import com.dokiwei.wanandroid.ui.screen.account.person.PersonScreen
import com.dokiwei.wanandroid.ui.screen.account.rank.RankScreen
import com.dokiwei.wanandroid.ui.screen.account.register.RegisterScreen
import com.dokiwei.wanandroid.ui.screen.home.HomeScreen
import com.dokiwei.wanandroid.ui.screen.home.search.SearchScreen
import com.dokiwei.wanandroid.ui.screen.navigation.TreeScreen
import com.dokiwei.wanandroid.ui.screen.other.sharearticles.ShareArticlesScreen
import com.dokiwei.wanandroid.ui.screen.other.startscreen.StartScreen
import com.dokiwei.wanandroid.ui.screen.other.webview.WebViewScreen
import com.dokiwei.wanandroid.ui.screen.project.ProjectScreen

/**
 * @author DokiWei
 * @date 2023/7/7 17:08
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainNavHost() {
    val viewModel: MainNavHostViewModel = viewModel()
    val navController = rememberNavController()
    var selectedIndex by remember { mutableIntStateOf(0) }
    val showBottomBar by viewModel.navBottomBar.collectAsState()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val vmP = publicViewModel()
    val notificationNum by vmP.unreadQuantity.collectAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    currentRoute?.let { viewModel.changeBottomBarSelectedItem(it) }
    val showLoginTip by MyApplication.isShowLoginTip.collectAsState()
    navController.addOnDestinationChangedListener { _, destination, _ ->
        when (destination.route) {
            HomeScreen.Home.route -> {
                selectedIndex = 0
            }

            ProjectScreen.Project.route -> {
                selectedIndex = 1
            }

            NavigationScreen.Tree.route -> {
                selectedIndex = 2
            }

            AccountScreen.Account.route -> {
                selectedIndex = 3
            }
        }
    }
    LaunchedEffect(Unit) {
        vmP.dispatch(PublicIntent.AlwaysGetUnreadQuantity)
    }
    Scaffold(bottomBar = {
        val items = Constants.mainRoute
        AnimatedVisibility(visible = showBottomBar, enter = slideInVertically {
            it
        }, exit = slideOutVertically {
            it
        }) {
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
                        iconSize = if (selectedIndex == index) 30.dp
                        else 24.dp
                        NavigationBarItem(icon = {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                if (index == 3 && notificationNum > 0) {
                                    BadgedBox(badge = {
                                        Badge {
                                            Text(notificationNum.toString(),
                                                modifier = Modifier.semantics {
                                                    contentDescription =
                                                        "${notificationNum}条新消息"
                                                })
                                        }
                                    }) {
                                        Icon(
                                            imageVector = Icons.Rounded.Person,
                                            contentDescription = null,
                                            modifier = Modifier.size(iconAnimSize),
                                            tint = if (selectedIndex == index) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline,
                                        )
                                    }
                                } else {
                                    Icon(
                                        imageVector = when (index) {
                                            0 -> Icons.Rounded.Home
                                            1 -> ImageVector.vectorResource(
                                                R.drawable.ic_project
                                            )

                                            2 -> ImageVector.vectorResource(
                                                R.drawable.ic_tree
                                            )

                                            else -> Icons.Rounded.Person
                                        },
                                        contentDescription = null,
                                        modifier = Modifier.size(iconAnimSize),
                                        tint = if (selectedIndex == index) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline,
                                    )
                                }
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
                            })
                    }
                }
            }
        }
    }) {
        NavHost(
            navController = navController,
            startDestination = OtherScreen.Start.route,
            modifier = Modifier.padding(it)
        ) {
            //动画启动页
            composable(
                route = OtherScreen.Start.route,
                enterTransition = ScreenAnim.enterScreenAnim(),
                exitTransition = ScreenAnim.exitScreenAnim(),
                popEnterTransition = ScreenAnim.popEnterScreenAnim(),
                popExitTransition = ScreenAnim.popExitScreenAnim()
            ) {
                StartScreen(navController)
            }
            //Home
            navigation(startDestination = HomeScreen.Home.route, route = HomeScreen.Main.route) {
                composable(
                    route = HomeScreen.Home.route,
                    enterTransition = ScreenAnim.enterScreenAnim(),
                    exitTransition = ScreenAnim.exitScreenAnim(),
                    popEnterTransition = ScreenAnim.popEnterScreenAnim(),
                    popExitTransition = ScreenAnim.popExitScreenAnim()
                ) {
                    HomeScreen(navController)
                }
                composable(
                    route = HomeScreen.Search.route,
                    enterTransition = ScreenAnim.enterScreenAnim(),
                    exitTransition = ScreenAnim.exitScreenAnim(),
                    popEnterTransition = ScreenAnim.popEnterScreenAnim(),
                    popExitTransition = ScreenAnim.popExitScreenAnim()
                ) {
                    SearchScreen(navController)
                }
            }
            //Account
            navigation(
                startDestination = AccountScreen.Account.route, route = AccountScreen.Main.route
            ) {
                composable(
                    route = AccountScreen.Account.route,
                    enterTransition = ScreenAnim.enterScreenAnim(),
                    exitTransition = ScreenAnim.exitScreenAnim(),
                    popEnterTransition = ScreenAnim.popEnterScreenAnim(),
                    popExitTransition = ScreenAnim.popExitScreenAnim()
                ) {
                    PersonScreen(navController)
                }
                composable(
                    route = AccountScreen.Collect.route,
                    enterTransition = ScreenAnim.enterScreenAnim(),
                    exitTransition = ScreenAnim.exitScreenAnim(),
                    popEnterTransition = ScreenAnim.popEnterScreenAnim(),
                    popExitTransition = ScreenAnim.popExitScreenAnim()
                ) {
                    MyLike(navController)
                }
                composable(
                    route = AccountScreen.Message.route,
                    enterTransition = ScreenAnim.enterScreenAnim(),
                    exitTransition = ScreenAnim.exitScreenAnim(),
                    popEnterTransition = ScreenAnim.popEnterScreenAnim(),
                    popExitTransition = ScreenAnim.popExitScreenAnim()
                ) {
                    MessageScreen(navController = navController)
                }
                composable(
                    route = AccountScreen.Coin.route,
                    enterTransition = ScreenAnim.enterScreenAnim(),
                    exitTransition = ScreenAnim.exitScreenAnim(),
                    popEnterTransition = ScreenAnim.popEnterScreenAnim(),
                    popExitTransition = ScreenAnim.popExitScreenAnim()
                ) {
                    CoinScreen(navController = navController)
                }
                composable(
                    route = AccountScreen.Rank.route,
                    enterTransition = ScreenAnim.enterScreenAnim(),
                    exitTransition = ScreenAnim.exitScreenAnim(),
                    popEnterTransition = ScreenAnim.popEnterScreenAnim(),
                    popExitTransition = ScreenAnim.popExitScreenAnim()
                ) {
                    RankScreen(navController = navController)
                }
                composable(
                    route = AccountScreen.Login.route,
                    enterTransition = ScreenAnim.enterScreenAnim(),
                    exitTransition = ScreenAnim.exitScreenAnim(),
                    popEnterTransition = ScreenAnim.popEnterScreenAnim(),
                    popExitTransition = ScreenAnim.popExitScreenAnim()
                ) {
                    LoginScreen(navController)
                }
                composable(
                    route = AccountScreen.Register.route,
                    enterTransition = ScreenAnim.enterScreenAnim(),
                    exitTransition = ScreenAnim.exitScreenAnim(),
                    popEnterTransition = ScreenAnim.popEnterScreenAnim(),
                    popExitTransition = ScreenAnim.popExitScreenAnim()
                ) {
                    RegisterScreen(navController)
                }
            }
            //Project
            navigation(
                startDestination = ProjectScreen.Project.route, route = ProjectScreen.Main.route
            ) {
                composable(
                    route = ProjectScreen.Project.route,
                    enterTransition = ScreenAnim.enterScreenAnim(),
                    exitTransition = ScreenAnim.exitScreenAnim(),
                    popEnterTransition = ScreenAnim.popEnterScreenAnim(),
                    popExitTransition = ScreenAnim.popExitScreenAnim()
                ) {
                    ProjectScreen(navController)
                }
            }
            //Navigation
            navigation(
                startDestination = NavigationScreen.Tree.route, route = NavigationScreen.Main.route
            ) {
                composable(
                    route = NavigationScreen.Tree.route,
                    enterTransition = ScreenAnim.enterScreenAnim(),
                    exitTransition = ScreenAnim.exitScreenAnim(),
                    popEnterTransition = ScreenAnim.popEnterScreenAnim(),
                    popExitTransition = ScreenAnim.popExitScreenAnim()
                ) {
                    TreeScreen(navController)
                }
            }
            composable(
                route = "${OtherScreen.WebView.route}/{link}",
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
                route = "${OtherScreen.UserArticles.route}/{userId}/{username}",
                enterTransition = ScreenAnim.enterScreenAnim(),
                exitTransition = ScreenAnim.exitScreenAnim(),
                popEnterTransition = ScreenAnim.popEnterScreenAnim(),
                popExitTransition = ScreenAnim.popExitScreenAnim(),
                arguments = listOf(
                    navArgument("userId") { type = NavType.IntType },
                    navArgument("username") { type = NavType.StringType },
                )
            ) { navBackStackEntry ->
                navBackStackEntry.arguments?.let { bundle ->
                    val userId = bundle.getInt("userId")
                    val username = bundle.getString("username")
                    username?.let { ShareArticlesScreen(navController, userId, username) }
                }
            }
        }

        AnimatedVisibility(visible = showLoginTip) {
            AlertDialog(icon = {
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = "警告:未登录",
                    tint = Color.Red
                )
            },
                onDismissRequest = { MyApplication.isShowLoginTip.value = false },
                confirmButton = {
                    TextButton(onClick = {
                        navController.navigate(AccountScreen.Login.route)
                        MyApplication.isShowLoginTip.value = false
                    }) {
                        Text(text = "登录")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { MyApplication.isShowLoginTip.value = false }) {
                        Text(text = "取消")
                    }
                },
                title = { Text(text = "提示") },
                text = {
                    Text(
                        text = """
                    |您需要登录才可以进行此操作😢
                    |您也可以选择取消,但您之后类似的操作仍会收到此提示😢
                    |所以建议您登录以得到完整的体验😊
                """.trimMargin()
                    )
                })
        }

    }
}
