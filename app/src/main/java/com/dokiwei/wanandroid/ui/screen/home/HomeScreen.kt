package com.dokiwei.wanandroid.ui.screen.home

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.dokiwei.wanandroid.model.util.HomeScreen
import com.dokiwei.wanandroid.model.util.ToastAndLogcatUtil
import com.dokiwei.wanandroid.model.util.mainBody
import com.dokiwei.wanandroid.ui.screen.home.content.home.HomeContent
import com.dokiwei.wanandroid.ui.screen.home.content.qa.QAContent
import com.dokiwei.wanandroid.ui.screen.home.content.square.SquareContent
import com.dokiwei.wanandroid.ui.widgets.MyScrollableTabRow
import kotlin.system.exitProcess

/**
 * @author DokiWei
 * @date 2023/7/8 15:36
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavHostController) {
    val vm: HomeViewModel = viewModel()
    val selectedTabIndex by vm.selectedTabIndex
    var scrollToTop by remember { mutableStateOf(false) }

    //返回监听
    val backPressed = remember { mutableLongStateOf(0L) }
    BackHandler(onBack = {
        if (backPressed.longValue + 2000 > System.currentTimeMillis()) {
            exitProcess(0)
        } else {
            ToastAndLogcatUtil.showMsg("再按一次退出")
            backPressed.longValue = System.currentTimeMillis()
        }
    })

    Scaffold(topBar = {
        TopAppBar(title = {
            MyScrollableTabRow(titleList = listOf(HomeScreen.Square.route, HomeScreen.Home.route, HomeScreen.Qa.route),
                selectedTabIndex = selectedTabIndex,
                onClick = {
                    vm.onTabSelected(it)
                })
        }, actions = {
            IconButton(onClick = { navController.navigate(HomeScreen.Search.route) }) {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        })
    }, floatingActionButton = {
        SmallFloatingActionButton(
            contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
            shape = FloatingActionButtonDefaults.largeShape,
            onClick = {
                scrollToTop = !scrollToTop
            }) {
            Icon(
                imageVector = Icons.Default.KeyboardArrowUp,
                contentDescription = "回到顶部"
            )
        }
    }) {
        Box(
            Modifier
                .mainBody()
                .padding(it)
        ) {
            when (selectedTabIndex) {
                0 -> SquareContent(navController = navController, scrollToTop)
                1 -> HomeContent(navController = navController, scrollToTop)
                2 -> QAContent(navController = navController, scrollToTop)
            }
        }
    }
}