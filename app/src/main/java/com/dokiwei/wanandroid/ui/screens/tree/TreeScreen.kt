package com.dokiwei.wanandroid.ui.screens.tree

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.dokiwei.wanandroid.ui.component.SwipeHomeItemsLayout
import com.dokiwei.wanandroid.ui.main.PublicIntent
import com.dokiwei.wanandroid.ui.main.PublicViewModel

/**
 * @author DokiWei
 * @date 2023/7/31 18:34
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TreeScreen(navController: NavController) {
    val vm: TreeViewModel = viewModel()
    val vmP: PublicViewModel = viewModel()
    val state by vm.state.collectAsState()
    var searchKey by remember {
        mutableStateOf("")
    }

    //返回监听
    BackHandler(onBack = {
        if (state.isShowChildren || state.isShowSearchResult) {
            if (state.isShowChildren) vm.dispatch(TreeIntent.ChangeShowChildren)
            if (state.isShowSearchResult) vm.dispatch(TreeIntent.ChangeShowSearchResult)
        } else {
            navController.navigateUp()
        }
    })

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(navigationIcon = {
                IconButton(onClick = {
                    if (state.isShowChildren || state.isShowSearchResult) {
                        if (state.isShowChildren) vm.dispatch(TreeIntent.ChangeShowChildren)
                        if (state.isShowSearchResult) vm.dispatch(TreeIntent.ChangeShowSearchResult)
                    } else {
                        navController.navigateUp()
                    }
                }) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "返回")
                }
            }, title = {
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
            }, actions = {
                IconButton(onClick = {
                    vm.dispatch(TreeIntent.UpdateAuthor(searchKey.trim()))
                    vm.dispatch(TreeIntent.GetSearchResult)
                    vm.dispatch(TreeIntent.ChangeShowSearchResult)
                }) {
                    Icon(
                        imageVector = Icons.Default.Search, contentDescription = "搜索图标"
                    )
                }
            })
        },
        floatingActionButton = {
            if (state.isShowChildren || state.isShowSearchResult)
                SmallFloatingActionButton(
                    shape = FloatingActionButtonDefaults.largeShape,
                    onClick = {
                        vm.dispatch(TreeIntent.ChangeScrollToTop)
                    }) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowUp,
                        contentDescription = "Scroll to top"
                    )
                }
        }
    ) { innerPadding ->
        AnimatedVisibility(
            state.data.isNotEmpty(),
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            PermanentNavigationDrawer(
                modifier = Modifier.padding(innerPadding),
                drawerContent = {
                    PermanentDrawerSheet(
                        Modifier.width(120.dp),
                        drawerShape = RoundedCornerShape(topEnd = 10.dp, bottomEnd = 10.dp),
                        drawerContainerColor = MaterialTheme.colorScheme.surface
                    ) {
                        LazyColumn {
                            items(state.data.size) {
                                val data = state.data[it]
                                Row(
                                    Modifier
                                        .fillMaxWidth()
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
                }
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    val childrenDataList = state.data[state.nowTreeIndex].children

                    LazyVerticalGrid(GridCells.FixedSize(108.dp)) {
                        items(childrenDataList.size) {
                            TextButton(
                                onClick = {
                                    vm.dispatch(TreeIntent.UpdateCid(childrenDataList[it].id))
                                    vm.dispatch(TreeIntent.GetTreeChildren)
                                    vm.dispatch(TreeIntent.ChangeShowChildren)
                                },
                                colors = ButtonDefaults.textButtonColors(
                                    containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(
                                        alpha = 0.75f
                                    ),
                                    contentColor = MaterialTheme.colorScheme.onSurface
                                )
                            ) {
                                Text(
                                    text = childrenDataList[it].name
                                )
                            }
                        }
                    }
                }
            }
        }
        //子体系文章
        AnimatedVisibility(
            visible = state.isShowChildren,
            enter = slideInVertically() + fadeIn(),
            exit = fadeOut()
        ) {
            SwipeHomeItemsLayout(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
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
                }
            )
        }
        //搜索结果
        AnimatedVisibility(
            visible = state.isShowSearchResult,
            enter = slideInVertically() + fadeIn(),
            exit = fadeOut()
        ) {
            if (state.searchResult.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .background(MaterialTheme.colorScheme.background)
                ) {
                    Text(
                        text = "搜索结果为空",
                        fontSize = 16.sp,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            } else {
                SwipeHomeItemsLayout(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
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
                    }
                )
            }

        }


    }

}
