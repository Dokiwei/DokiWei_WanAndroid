package com.dokiwei.wanandroid.ui.screens.home

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.dokiwei.wanandroid.ui.component.MyScrollableTabRow
import com.dokiwei.wanandroid.ui.component.mainBody
import com.dokiwei.wanandroid.ui.screens.home.content.HomeContent
import com.dokiwei.wanandroid.ui.screens.home.content.QAContent
import com.dokiwei.wanandroid.ui.screens.home.content.SquareContent
import kotlin.system.exitProcess

/**
 * @author DokiWei
 * @date 2023/7/8 15:36
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavHostController) {
    val vm: HomeViewModel = viewModel()
    val context = LocalContext.current
    val selectedTabIndex by vm.selectedTabIndex

    //返回监听
    val backPressed = remember { mutableLongStateOf(0L) }
    BackHandler(onBack = {
        if (backPressed.longValue + 2000 > System.currentTimeMillis()) {
            exitProcess(0)
        } else {
            Toast.makeText(context, "再按一次退出", Toast.LENGTH_SHORT).show()
            backPressed.longValue = System.currentTimeMillis()
        }
    })

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    MyScrollableTabRow(
                        titleList = listOf("首页", "广场", "问答"),
                        selectedTabIndex = selectedTabIndex,
                        onClick = {
                            vm.onTabSelected(it)
                        })
                },
                actions = {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(
                            imageVector = Icons.Filled.Search,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            )
        }
    ) {
        Box(
            Modifier
                .mainBody()
                .padding(it)
        ) {
            when (selectedTabIndex) {
                0 -> HomeContent(navController = navController)
                1 -> SquareContent(navController = navController)
                2 -> QAContent(navController = navController)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun Pre() {
    HomeScreen(navController = rememberNavController())
}