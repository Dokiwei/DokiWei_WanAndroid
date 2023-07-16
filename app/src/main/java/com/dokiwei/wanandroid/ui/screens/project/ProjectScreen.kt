package com.dokiwei.wanandroid.ui.screens.project

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.List
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.dokiwei.wanandroid.ui.component.MyScrollableTabRow
import com.dokiwei.wanandroid.ui.component.MySwipeRefresh
import com.dokiwei.wanandroid.ui.component.ProjectListView
import com.dokiwei.wanandroid.ui.component.mainBody
import com.dokiwei.wanandroid.util.ToastUtil
import kotlinx.coroutines.delay
import java.net.URLEncoder

/**
 * @author DokiWei
 * @date 2023/7/15 16:05
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectScreen(navController: NavHostController) {
    val vm: ProjectViewModel = viewModel()
    val context = LocalContext.current
    val projectTitleList by vm.projectTitleList.collectAsState()
    val projectList by vm.projectList.collectAsState()
    val selectedTabIndex by vm.selectedTabIndex
    val isTabExpand by vm.isTabExpand
    val isRefreshing = vm.isRefreshing
    var titleList by remember {
        mutableStateOf(listOf("加载中"))
    }
    var scrollToTop by remember { mutableStateOf(false) }
    LaunchedEffect(scrollToTop) {
        delay(500)
        scrollToTop = false
    }
    if (projectTitleList.isNotEmpty()) {
        val list = mutableListOf<String>()
        projectTitleList.forEach {
            list.add(it.name)
        }
        titleList = list
    }
    Scaffold(
        topBar = {
            if (!isTabExpand) {
                TopAppBar(
                    title = {
                        MyScrollableTabRow(
                            titleList = titleList,
                            selectedTabIndex = selectedTabIndex,
                            onClick = {
                                vm.onTabSelected(it)
                            }
                        )
                    },
                    actions = {
                        IconButton(onClick = { vm.onTabExpanded(true) }) {
                            Icon(
                                imageVector = Icons.Rounded.List,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                )
            } else {
                TopAppBar(
                    title = {
                        Text(
                            text = "全部项目分类",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { vm.onTabExpanded(false) }) {
                            Icon(imageVector = Icons.Rounded.ArrowBack, contentDescription = null)
                        }
                    }
                )
            }
        },
        floatingActionButton = {
            SmallFloatingActionButton(
                containerColor = MaterialTheme.colorScheme.primary,
                shape = FloatingActionButtonDefaults.largeShape,
                onClick = {
                    scrollToTop = !scrollToTop
                }
            ) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowUp,
                    contentDescription = "Scroll to top"
                )
            }
        }
    ) { innerPadding ->
        //全部项目分类
        AnimatedVisibility(
            visible = isTabExpand,
            enter = slideInHorizontally(
                initialOffsetX = { it },
                animationSpec = tween(500, easing = LinearOutSlowInEasing)
            ) + fadeIn(animationSpec = tween(500)),
            exit = slideOutHorizontally(
                targetOffsetX = { -it },
                animationSpec = tween(500, easing = FastOutLinearInEasing)
            ) + fadeOut(animationSpec = tween(500))
        ) {
            LazyVerticalGrid(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                columns = GridCells.Fixed(3),
            ) {
                items(projectTitleList.size) {
                    TextButton(
                        onClick = { vm.onTabSelected(it) },
                        border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary),
                        modifier = Modifier.padding(horizontal = 5.dp)
                    ) {
                        Text(text = projectTitleList[it].name)
                    }
                }
            }
        }
        MySwipeRefresh(
            scrollToTop = scrollToTop,
            isRefreshing = isRefreshing,
            onRefresh = { vm.onRefresh() },
            onLoadMore = {
                if (it > (projectList.size - 10))
                    if (vm.loadMore()) ToastUtil.showMsg(context, "加载新内容...")
                    else ToastUtil.showMsg(context, "到底啦!!")
            }
        ) {
            AnimatedVisibility(
                visible = projectTitleList.isNotEmpty() && projectList.isNotEmpty() && !isTabExpand,
                enter = slideInVertically(animationSpec = tween(800))
            ) {
                ProjectListView(
                    modifier = Modifier
                        .mainBody()
                        .padding(innerPadding),
                    projectList = projectList,
                    onProjectClick = {
                        val link = URLEncoder.encode(it.link, "UTF-8")
                        navController.navigate("网页/$link")
                    },
                    lazyListState = it
                )
            }
        }
    }
}