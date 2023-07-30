package com.dokiwei.wanandroid.ui.screens.project

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.dokiwei.wanandroid.ui.component.Loading
import com.dokiwei.wanandroid.ui.component.MyScrollableTabRow
import com.dokiwei.wanandroid.ui.component.SwipeProjectItemsLayout
import com.dokiwei.wanandroid.ui.main.PublicIntent
import com.dokiwei.wanandroid.ui.main.PublicViewModel
import com.dokiwei.wanandroid.util.ToastAndLogcatUtil

/**
 * @author DokiWei
 * @date 2023/7/15 16:05
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectScreen(navController: NavHostController) {
    val vm: ProjectViewModel = viewModel()
    val vmP: PublicViewModel = viewModel()
    val state by vm.state.collectAsState()
    var titleList by remember {
        mutableStateOf(listOf(""))
    }
    if (state.projectTabs.isNotEmpty()) {
        val list = mutableListOf<String>()
        state.projectTabs.forEach {
            list.add(it.name)
        }
        titleList = list
    }

    LaunchedEffect(state.msg) {
        if (state.msg.isNotEmpty()) ToastAndLogcatUtil.showMsg(state.msg)
    }

    Scaffold(topBar = {
        if (!state.isTabExpand) {
            TopAppBar(title = {
                MyScrollableTabRow(titleList = titleList,
                    selectedTabIndex = state.selectedTabIndex,
                    onClick = {
                        vm.dispatch(ProjectIntent.SelectedTab(it))
                    })
            }, actions = {
                IconButton(onClick = { vm.dispatch(ProjectIntent.UpdateTabExpanded(true)) }) {
                    Icon(
                        imageVector = Icons.Rounded.List,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            })
        } else {
            TopAppBar(title = {
                Text(
                    text = "全部项目分类", fontWeight = FontWeight.Bold, fontSize = 16.sp
                )
            }, navigationIcon = {
                IconButton(onClick = { vm.dispatch(ProjectIntent.UpdateTabExpanded(true)) }) {
                    Icon(imageVector = Icons.Rounded.ArrowBack, contentDescription = null)
                }
            })
        }
    }, floatingActionButton = {
        SmallFloatingActionButton(
            shape = FloatingActionButtonDefaults.largeShape,
            onClick = {
                vm.dispatch(ProjectIntent.UpdateScrollToTop(!state.scrollToTop))
            }) {
            Icon(
                imageVector = Icons.Default.KeyboardArrowUp, contentDescription = "Scroll to top"
            )
        }
    }) { innerPadding ->
        //全部项目分类
        AnimatedVisibility(
            visible = state.isTabExpand,
            enter = slideInHorizontally(
                initialOffsetX = { it }, animationSpec = tween(500, easing = LinearOutSlowInEasing)
            ) + fadeIn(animationSpec = tween(500)),
            exit = slideOutHorizontally(
                targetOffsetX = { -it }, animationSpec = tween(500, easing = FastOutLinearInEasing)
            ) + fadeOut(animationSpec = tween(500))
        ) {
            LazyVerticalGrid(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                columns = GridCells.Fixed(3),
            ) {
                items(state.projectTabs.size) {
                    TextButton(
                        onClick = { vm.dispatch(ProjectIntent.SelectedTab(it)) },
                        border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary),
                        modifier = Modifier.padding(horizontal = 5.dp)
                    ) {
                        Text(text = state.projectTabs[it].name)
                    }
                }
            }
        }
        if (state.projectList.isEmpty()) Loading(onClick = { vm.dispatch(ProjectIntent.Refresh) })

        SwipeProjectItemsLayout(
            modifier = Modifier.padding(innerPadding),
            navController = navController,
            isToTop = state.scrollToTop,
            isRefreshing = state.isRefreshing,
            isLoadingMore = state.isLoadingMore,
            items = state.projectList,
            onRefresh = { vm.dispatch(ProjectIntent.Refresh) },
            onLoadMore = { vm.dispatch(ProjectIntent.LoadMore) },
            onCollectClick = { item, like ->
                if (like) vmP.dispatch(PublicIntent.UnCollect(item.id))
                else vmP.dispatch(PublicIntent.Collect(item.id))
            }
        )

    }

}