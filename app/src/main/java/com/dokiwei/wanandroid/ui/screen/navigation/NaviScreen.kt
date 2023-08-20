package com.dokiwei.wanandroid.ui.screen.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.PermanentDrawerSheet
import androidx.compose.material3.PermanentNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.dokiwei.wanandroid.R
import com.dokiwei.wanandroid.model.util.myCustomNavigate
import com.dokiwei.wanandroid.model.util.publicViewModel
import com.dokiwei.wanandroid.ui.main.PublicIntent
import com.dokiwei.wanandroid.ui.main.PublicViewModel
import com.dokiwei.wanandroid.ui.widgets.MyScrollableTabRow
import com.dokiwei.wanandroid.ui.widgets.SwipeHomeItemsLayout
import java.net.URLEncoder

/**
 * @author DokiWei
 * @date 2023/7/31 18:34
 */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun TreeScreen(navController: NavController) {
    val vm: TreeViewModel = viewModel()
    val vmP = publicViewModel()
    val state by vm.state.collectAsState()
    var title by remember { mutableStateOf("") }
    var searchKey by remember { mutableStateOf("") }
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    var showSearchBar by remember { mutableStateOf(false) }
    var showSearchResult by remember { mutableStateOf(false) }
    var showChildren by remember { mutableStateOf(false) }
    //返回监听
    BackHandler {
        if (showChildren || showSearchResult) {
            if (showChildren) showChildren = false
            if (showSearchResult) showSearchResult = false
        } else {
            navController.navigateUp()
        }
    }

    Scaffold(topBar = {
        CenterAlignedTopAppBar(navigationIcon = {
            AnimatedVisibility(visible = showChildren || showSearchResult || showSearchBar,
                enter = slideInHorizontally { -it },
                exit = slideOutHorizontally { -it }) {
                IconButton(onClick = {
                    if (showChildren) showChildren = false
                    if (showSearchResult) showSearchResult = false
                    if (showSearchBar) showSearchBar = false
                }) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "返回")
                }
            }
        }, title = {
            if (!showSearchBar && !showChildren) {
                MyScrollableTabRow(titleList = listOf("体系", "导航"),
                    selectedTabIndex = selectedTabIndex,
                    onClick = {
                        selectedTabIndex = it
                    })
            }
            if (showChildren) {
                Text(text = title)
            }
            AnimatedVisibility(visible = showSearchBar,
                enter = slideInHorizontally { it + 240 },
                exit = slideOutHorizontally { it + 240 }) {
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    maxLines = 1,
                    placeholder = {
                        Text(text = "请输入作者昵称进行搜索", maxLines = 1)
                    },
                    value = searchKey,
                    textStyle = TextStyle.Default,
                    onValueChange = {
                        searchKey = it
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        errorBorderColor = Color.Transparent,
                        disabledBorderColor = Color.Transparent,
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent
                    )
                )
            }
        }, actions = {
            IconButton(onClick = {
                if (!showSearchBar) {
                    showSearchBar = true
                } else {
                    vm.dispatch(TreeIntent.UpdateAuthor(searchKey.trim()))
                    vm.dispatch(TreeIntent.GetSearchResult)
                    showSearchResult = true
                }
            }) {
                Icon(
                    imageVector = Icons.Default.Search, contentDescription = "搜索图标"
                )
            }
        })
    }, floatingActionButton = {
        if (showChildren || showSearchResult) SmallFloatingActionButton(shape = FloatingActionButtonDefaults.largeShape,
            onClick = {
                vm.dispatch(TreeIntent.ChangeScrollToTop)
            }) {
            Icon(
                imageVector = Icons.Default.KeyboardArrowUp, contentDescription = null
            )
        }
    }) { innerPadding ->
        Box(
            modifier = Modifier.padding(
                top = innerPadding.calculateTopPadding(),
                bottom = innerPadding.calculateBottomPadding(),
                start = 10.dp,
                end = 10.dp
            )
        ) {
            when (selectedTabIndex) {
                0 -> Tree(state,
                    vm,
                    showChildren,
                    navController,
                    vmP,
                    showSearchResult,
                    { title = it },
                    { showChildren = it })

                1 -> Navigation(state, navController)
            }
        }
    }

}

@Composable
@OptIn(ExperimentalLayoutApi::class)
private fun Navigation(
    state: TreeState, navController: NavController
) {
    AnimatedVisibility(
        visible = state.navi.isNotEmpty(), enter = fadeIn(), exit = fadeOut()
    ) {
        LazyColumn {
            items(state.navi.size) { index ->
                val item = state.navi[index]
                Text(
                    text = item.name,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(horizontal = 10.dp)
                )
                FlowRow {
                    item.articles.forEach {
                        TextButton(
                            modifier = Modifier.padding(horizontal = 5.dp), onClick = {
                                val link = URLEncoder.encode(
                                    it.link, "UTF-8"
                                )
                                navController.myCustomNavigate("网页/$link")
                            }, colors = ButtonDefaults.textButtonColors(
                                containerColor = colorResource(id = R.color.button),
                                contentColor = MaterialTheme.colorScheme.onSurface
                            )
                        ) {
                            Text(
                                text = it.title
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}

@Composable
@OptIn(ExperimentalLayoutApi::class)
private fun Tree(
    state: TreeState,
    vm: TreeViewModel,
    showChildren: Boolean,
    navController: NavController,
    vmP: PublicViewModel,
    showSearchResult: Boolean,
    changeTitle: (String) -> Unit,
    changeShowChildren: (Boolean) -> Unit
) {
    AnimatedVisibility(
        state.data.isNotEmpty(), enter = fadeIn(), exit = fadeOut()
    ) {
        PermanentNavigationDrawer(drawerContent = {
            PermanentDrawerSheet(
                Modifier.width(120.dp),
                drawerShape = RoundedCornerShape(topEnd = 10.dp, bottomEnd = 10.dp),
                drawerContainerColor = MaterialTheme.colorScheme.surface
            ) {
                LazyColumn {
                    items(state.data.size) {
                        val data = state.data[it]
                        Row(
                            Modifier.fillMaxWidth()
                        ) {
                            if (state.nowTreeIndex == it) {
                                Box(
                                    modifier = Modifier
                                        .width(5.dp)
                                        .height(50.dp)
                                        .clip(RoundedCornerShape(5.dp))
                                        .background(MaterialTheme.colorScheme.primary)
                                )
                            }
                            TextButton(
                                modifier = Modifier
                                    .align(Alignment.CenterVertically)
                                    .weight(1f),
                                onClick = {
                                    vm.dispatch(TreeIntent.UpdateTreeIndex(it))
                                },
                                colors = ButtonDefaults.textButtonColors(
                                    contentColor = if (state.nowTreeIndex == it) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                                ),
                            ) {
                                Text(
                                    data.name,
                                    maxLines = if (state.nowTreeIndex == it) Int.MAX_VALUE else 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }
                }
            }
        }) {
            Row {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(1.dp)
                        .background(DividerDefaults.color)
                )
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    val childrenDataList = state.data[state.nowTreeIndex].children

                    LazyColumn {
                        item {
                            FlowRow {
                                childrenDataList.forEach {
                                    TextButton(
                                        modifier = Modifier.padding(horizontal = 5.dp), onClick = {
                                            changeTitle(it.name)
                                            vm.dispatch(TreeIntent.UpdateCid(it.id))
                                            vm.dispatch(TreeIntent.GetTreeChildren)
                                            changeShowChildren(true)
                                        }, colors = ButtonDefaults.textButtonColors(
                                            containerColor = colorResource(id = R.color.button),
                                            contentColor = MaterialTheme.colorScheme.onSurface
                                        )
                                    ) {
                                        Text(
                                            text = it.name
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    //子体系文章
    AnimatedVisibility(
        visible = showChildren, enter = slideInVertically() + fadeIn(), exit = fadeOut()
    ) {
        SwipeHomeItemsLayout(modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
            isToTop = state.scrollToTop,
            navController = navController,
            isRefreshing = state.isRefreshing,
            isLoadingMore = state.isLoadingMore,
            items = state.children,
            onRefresh = {
                vm.dispatch(TreeIntent.RefreshTreeChildren)
            },
            onLoadMore = {
                vm.dispatch(TreeIntent.LoadMoreTreeChildren)
            },
            onCollectClick = { item, like ->
                if (like) vmP.dispatch(PublicIntent.UnCollect(item.id))
                else vmP.dispatch(PublicIntent.Collect(item.id))
            })
    }
    //搜索结果
    AnimatedVisibility(
        visible = showSearchResult, enter = slideInVertically() + fadeIn(), exit = fadeOut()
    ) {
        if (state.searchResult.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
            ) {
                Text(
                    text = "搜索结果为空",
                    fontSize = 16.sp,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        } else {
            SwipeHomeItemsLayout(modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
                isToTop = state.scrollToTop,
                navController = navController,
                isRefreshing = state.isRefreshing,
                isLoadingMore = state.isLoadingMore,
                items = state.searchResult,
                onRefresh = {
                    vm.dispatch(TreeIntent.RefreshSearchResult)
                },
                onLoadMore = {
                    vm.dispatch(TreeIntent.LoadMoreSearchResult)
                },
                onCollectClick = { item, like ->
                    if (like) vmP.dispatch(PublicIntent.UnCollect(item.id))
                    else vmP.dispatch(PublicIntent.Collect(item.id))
                })
        }

    }
}
