package com.dokiwei.wanandroid.ui.screens.search

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.dokiwei.wanandroid.ui.component.SwipeHomeItemsLayout
import com.dokiwei.wanandroid.ui.main.PublicIntent
import com.dokiwei.wanandroid.ui.main.PublicViewModel
import com.dokiwei.wanandroid.util.mainBody
import kotlinx.coroutines.delay

/**
 * @author DokiWei
 * @date 2023/7/27 19:17
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(navController: NavController) {
    val vm: SearchViewModel = viewModel()
    val vmP: PublicViewModel = viewModel()
    val state by vm.state.collectAsState()
    var searchKey by remember {
        mutableStateOf("")
    }
    //返回监听
    BackHandler(onBack = {
        if (state.isShowResult) {
            vm.dispatch(SearchIntent.RevertToTheDefaultShowResult)
        } else {
            navController.navigateUp()
        }
    })
    LaunchedEffect(state.scrollToTop) {
        delay(500)
        vm.dispatch(SearchIntent.UpdateScrollToTop(false))
    }
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(navigationIcon = {
                IconButton(onClick = {
                    if (state.isShowResult) {
                        vm.dispatch(SearchIntent.RevertToTheDefaultShowResult)
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
                        Text(text = "请输入关键词", maxLines = 1)
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
                    vm.dispatch(SearchIntent.GetSearchResult(k = searchKey.trim()))
                    vm.dispatch(SearchIntent.UpdateScrollToTop(true))
                }) {
                    Icon(
                        imageVector = Icons.Default.Search, contentDescription = "搜索图标"
                    )
                }
            })
        },
        floatingActionButton = {
            if (state.isShowResult)
                SmallFloatingActionButton(
                    shape = FloatingActionButtonDefaults.largeShape,
                    onClick = {
                        vm.dispatch(SearchIntent.UpdateScrollToTop(true))
                    }) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowUp,
                        contentDescription = "Scroll to top"
                    )
                }
        }
    ) { innerPadding ->
        AnimatedVisibility(
            visible = !state.isShowResult,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Box(
                Modifier
                    .mainBody()
                    .padding(innerPadding)
            ) {
                Column(
                    Modifier.padding(10.dp)
                ) {
                    Text(
                        modifier = Modifier.padding(start = 2.dp, bottom = 5.dp),
                        text = "热门搜索",
                        fontSize = 18.sp
                    )
                    LazyVerticalGrid(
                        modifier = Modifier.padding(5.dp),
                        columns = GridCells.Fixed(3)
                    ) {
                        items(state.hotKeys.size) {
                            val hotKey = state.hotKeys[it]
                            TextButton(
                                modifier = Modifier.padding(horizontal = 5.dp),
                                contentPadding = PaddingValues(0.dp),
                                onClick = {
                                    searchKey = hotKey.name
                                    vm.dispatch(SearchIntent.GetSearchResult(k = hotKey.name))
                                    vm.dispatch(SearchIntent.UpdateScrollToTop(true))
                                },
                                colors = ButtonDefaults.textButtonColors(
                                    containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(
                                        alpha = 0.75f
                                    ),
                                    contentColor = MaterialTheme.colorScheme.onSurface
                                )
                            ) {
                                Text(
                                    text = hotKey.name
                                )
                            }
                        }
                    }
                }

            }
        }
        AnimatedVisibility(
            visible = state.isShowResult,
            enter = slideInVertically() + fadeIn(),
            exit = fadeOut()
        ) {
            SwipeHomeItemsLayout(
                modifier = Modifier.padding(innerPadding),
                navController = navController,
                isRefreshing = state.isRefreshing,
                isLoadingMore = state.isLoadingMore,
                isToTop = state.scrollToTop,
                items = state.searchResult,
                heightLight = searchKey,
                onRefresh = { vm.dispatch(SearchIntent.Refresh(searchKey.trim())) },
                onLoadMore = { vm.dispatch(SearchIntent.LoadMore(searchKey.trim())) },
                onCollectClick = { item, like ->
                    if (like) vmP.dispatch(PublicIntent.UnCollect(item.id))
                    else vmP.dispatch(PublicIntent.Collect(item.id))
                }
            )
        }

    }
}