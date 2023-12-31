package com.dokiwei.wanandroid.ui.screen.home.search

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.paging.compose.collectAsLazyPagingItems
import com.dokiwei.wanandroid.R
import com.dokiwei.wanandroid.model.util.PagingState
import com.dokiwei.wanandroid.model.util.mainBody
import com.dokiwei.wanandroid.model.util.publicViewModel
import com.dokiwei.wanandroid.ui.widgets.Items
import com.dokiwei.wanandroid.ui.widgets.SwipeLayout
import kotlinx.coroutines.delay

/**
 * @author DokiWei
 * @date 2023/7/27 19:17
 */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun SearchScreen(navController: NavController) {
    val vm: SearchViewModel = viewModel()
    val vmP = publicViewModel()
    val state by vm.state.collectAsState()

    val lazyListState = rememberLazyListState()
    var searchKey by remember { mutableStateOf("") }
    var scrollToTop by remember { mutableStateOf(false) }
    //返回监听
    BackHandler(onBack = {
        if (state.isShowResult) {
            searchKey = ""
            vm.dispatch(SearchIntent.ChangeShowResult(false))
        } else {
            navController.navigateUp()
        }
    })

    LaunchedEffect(scrollToTop) {
        if (scrollToTop) {
            lazyListState.animateScrollToItem(0)
            delay(500)
            scrollToTop = false
        }
    }
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(navigationIcon = {
                IconButton(onClick = {
                    if (state.isShowResult) {
                        searchKey = ""
                        vm.dispatch(SearchIntent.ChangeShowResult(false))
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
                        Text(text = "请输入关键词", maxLines = 1, color = DividerDefaults.color)
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
                    vm.dispatch(SearchIntent.GetSearchResult(searchKey = searchKey.trim()))
                    vm.dispatch(SearchIntent.ChangeShowResult(true))
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
                        scrollToTop = true
                    }) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowUp,
                        contentDescription = "Scroll to top"
                    )
                }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {

            AnimatedVisibility(
                visible = !state.isShowResult,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Box(
                    Modifier
                        .mainBody()
                ) {
                    Column(
                        Modifier.padding(10.dp)
                    ) {
                        Text(
                            modifier = Modifier.padding(start = 2.dp, bottom = 5.dp),
                            text = "热门搜索",
                            fontSize = 18.sp
                        )
                        FlowRow {
                            state.hotKeys.forEach { hotKeyData ->
                                TextButton(
                                    modifier = Modifier.padding(horizontal = 5.dp),
                                    onClick = {
                                        searchKey = hotKeyData.name
                                        vm.dispatch(SearchIntent.GetSearchResult(searchKey = hotKeyData.name))
                                        vm.dispatch(SearchIntent.ChangeShowResult(true))
                                    },
                                    colors = ButtonDefaults.textButtonColors(
                                        containerColor = colorResource(id = R.color.button),
                                        contentColor = MaterialTheme.colorScheme.onSurface
                                    )
                                ) {
                                    Text(
                                        text = hotKeyData.name
                                    )
                                }
                            }
                        }
                    }

                }
            }
            AnimatedVisibility(
                visible = state.isShowResult,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                val data = state.data.collectAsLazyPagingItems()
                data.PagingState(tag = "Search-Paging 加载状态:")
                SwipeLayout(onRefresh = { data.refresh() }) {
                    Items(
                        searchKey = searchKey,
                        lazyListState = lazyListState,
                        navController = navController,
                        articleData = data,
                        vmP = vmP
                    )
                }
            }

        }
    }
}