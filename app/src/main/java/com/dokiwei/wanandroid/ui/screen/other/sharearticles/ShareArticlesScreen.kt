package com.dokiwei.wanandroid.ui.screen.other.sharearticles

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.dokiwei.wanandroid.model.util.OtherScreen
import com.dokiwei.wanandroid.model.util.mainBody
import com.dokiwei.wanandroid.model.util.publicViewModel
import com.dokiwei.wanandroid.model.util.randomAvatar
import com.dokiwei.wanandroid.ui.main.PublicIntent
import com.dokiwei.wanandroid.ui.widgets.SwipeHomeItemsLayout

/**
 * @author DokiWei
 * @date 2023/8/21 19:49
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShareArticlesScreen(navController: NavController, userId: Int, username: String) {
    val vm: ShareArticlesViewModel = viewModel()
    val vmP = publicViewModel()

    val painterID = remember { randomAvatar() }
    val state by vm.state.collectAsState()

    LaunchedEffect(Unit) {
        vm.dispatch(ShareArticlesIntent.GetUserArticles(userId))
    }

    Scaffold(topBar = {
        CenterAlignedTopAppBar(title = { Text(text = OtherScreen.UserArticles.route) },
            navigationIcon = {
                IconButton(onClick = { navController.navigateUp() }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack, contentDescription = "返回上一页"
                    )
                }
            })
    }, floatingActionButton = {
        SmallFloatingActionButton(containerColor = MaterialTheme.colorScheme.secondaryContainer,
            contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
            shape = FloatingActionButtonDefaults.largeShape,
            onClick = {
                vm.dispatch(ShareArticlesIntent.UpdateScrollToTop(!state.scrollToTop))
            }) {
            Icon(
                imageVector = Icons.Default.KeyboardArrowUp, contentDescription = "Scroll to top"
            )
        }
    }) { innerPadding ->
        Box(
            modifier = Modifier
                .mainBody()
                .fillMaxSize()
        ) {
            SwipeHomeItemsLayout(modifier = Modifier.padding(innerPadding),
                navController = navController,
                isRefreshing = state.isRefreshing,
                isLoadingMore = state.isLoadingMore,
                isToTop = state.scrollToTop,
                items = state.userArticleData,
                onRefresh = { vm.dispatch(ShareArticlesIntent.Refresh(userId)) },
                onLoadMore = { vm.dispatch(ShareArticlesIntent.LoadMore(userId)) },
                painterId = painterID,
                onCollectClick = { item, like ->
                    if (like) vmP.dispatch(PublicIntent.UnCollect(item.id))
                    else vmP.dispatch(PublicIntent.Collect(item.id))
                },
                noDataContent = {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            modifier = Modifier.align(
                                Alignment.Center
                            ), text = """
                    未获取到数据😢
                    可能此用户还未分享过文章
                    或者该用户不是分享者而是站内创作者😊
                    WanAndroid未提供站内创作者文章检索的Api😢
                """.trimIndent()
                        )
                    }
                },
                headContent = {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(2f)
                    ) {
                        Column(
                            Modifier.align(Alignment.Center),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Image(
                                painter = painterResource(painterID),
                                contentDescription = null,
                                Modifier
                                    .size(80.dp)
                                    .clip(RoundedCornerShape(80.dp))
                            )
                            state.coinInfoData?.let {
                                Text(
                                    text = if (username == "默认名称") it.username else username,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                )
                                Text(text = "积分:${it.coinCount}")
                                Row {
                                    Text(text = "等级:${it.level}")
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Text(text = "排名${it.rank}")
                                }
                            }
                        }
                    }
                })
        }
    }

}