package com.dokiwei.wanandroid.ui.screen.project

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.List
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.dokiwei.wanandroid.R
import com.dokiwei.wanandroid.model.util.OtherScreen
import com.dokiwei.wanandroid.model.util.ToastAndLogcatUtil
import com.dokiwei.wanandroid.model.util.myCustomNavigate
import com.dokiwei.wanandroid.model.util.publicViewModel
import com.dokiwei.wanandroid.ui.main.PublicIntent
import com.dokiwei.wanandroid.ui.widgets.CardContent
import com.dokiwei.wanandroid.ui.widgets.LikeIcon
import com.dokiwei.wanandroid.ui.widgets.Loading
import com.dokiwei.wanandroid.ui.widgets.MyScrollableTabRow
import com.dokiwei.wanandroid.ui.widgets.SwipeLayout
import java.net.URLEncoder

/**
 * @author DokiWei
 * @date 2023/7/15 16:05
 */
@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun ProjectScreen(navController: NavHostController) {
    val vm: ProjectViewModel = hiltViewModel()
    val vmP = publicViewModel()
    val state by vm.state.collectAsState()
    val title = state.title.collectAsLazyPagingItems()
    val data = state.data.collectAsLazyPagingItems()

    val lazyListState = rememberLazyListState()

    BackHandler(onBack = {
        if (state.isTabExpand) {
            vm.dispatch(ProjectIntent.UpdateTabExpanded(false))
        } else {
            navController.navigateUp()
        }
    })

    LaunchedEffect(state.scrollToTop) {
        lazyListState.animateScrollToItem(0)
    }

    when (data.loadState.refresh) {
        is LoadState.Loading -> Loading {
            data.retry()
        }

        is LoadState.Error -> (data.loadState.refresh as LoadState.Error).error.message?.let {
            ToastAndLogcatUtil.log(
                "ProjectPaging",
                msg = it
            )
        }

        is LoadState.NotLoading -> {}
    }

    Scaffold(
        topBar = {
            if (!state.isTabExpand && title.itemCount > 0) {
                TopAppBar(
                    title = {
                        MyScrollableTabRow(titleList = title.itemSnapshotList,
                            selectedTabIndex = state.selectedTabIndex,
                            onClick = {
                                vm.dispatch(ProjectIntent.SelectedTab(it))
                                title[it]?.id?.let {
                                    cid-> vm.dispatch(ProjectIntent.UpdateCid(cid))
                                    data.refresh()
                                }
                            })
                    },
                    actions = {
                        IconButton(onClick = { vm.dispatch(ProjectIntent.UpdateTabExpanded(true)) }) {
                            Icon(
                                imageVector = Icons.Rounded.List,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    })
            } else {
                TopAppBar(
                    title = {
                        Text(
                            text = "全部项目分类", fontWeight = FontWeight.Bold, fontSize = 16.sp
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { vm.dispatch(ProjectIntent.UpdateTabExpanded(false)) }) {
                            Icon(imageVector = Icons.Rounded.ArrowBack, contentDescription = null)
                        }
                    })
            }
        },
        floatingActionButton = {
            SmallFloatingActionButton(
                shape = FloatingActionButtonDefaults.largeShape,
                onClick = {
                    vm.dispatch(ProjectIntent.UpdateScrollToTop(!state.scrollToTop))
                }) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowUp,
                    contentDescription = "Scroll to top"
                )
            }
        }) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)){
            //全部项目分类
            AnimatedVisibility(
                visible = state.isTabExpand,
                enter = slideInHorizontally(
                    initialOffsetX = { it }, animationSpec = tween(500, easing = LinearOutSlowInEasing)
                ) + fadeIn(animationSpec = tween(500)),
                exit = slideOutHorizontally(
                    targetOffsetX = { it }, animationSpec = tween(500, easing = FastOutLinearInEasing)
                ) + fadeOut(animationSpec = tween(500))
            ) {
                FlowRow(
                    modifier = Modifier.padding(horizontal = 10.dp),
                ) {
                    title.itemSnapshotList.forEachIndexed { index, tabTitle ->
                        tabTitle?.let {
                            TextButton(
                                modifier = Modifier.padding(horizontal = 5.dp),
                                onClick = {
                                    vm.dispatch(ProjectIntent.SelectedTab(index))
                                    title[index]?.id?.let {
                                            cid-> vm.dispatch(ProjectIntent.UpdateCid(cid))
                                        data.refresh()
                                    }
                                    vm.dispatch(ProjectIntent.UpdateTabExpanded(false))
                                },
                                colors = ButtonDefaults.textButtonColors(
                                    containerColor = colorResource(id = R.color.button),
                                    contentColor = MaterialTheme.colorScheme.onSurface
                                )
                            ) {
                                Text(text = it.name)
                            }
                        }
                    }
                }
            }

            AnimatedVisibility(visible = !state.isTabExpand, enter = fadeIn(), exit = fadeOut()) {
                SwipeLayout(onRefresh = { data.refresh() }) {
                    LazyColumn(state = lazyListState) {
                        items(data.itemCount) {
                            val project = data[it]!!
                            var like by mutableStateOf(project.collect)
                            CardContent(onClick = {
                                val link = URLEncoder.encode(project.link, "UTF-8")
                                navController.myCustomNavigate("${OtherScreen.WebView.route}/$link")
                            }) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(10.dp)
                                ) {
                                    AsyncImage(
                                        model = project.envelopePic,
                                        contentDescription = null,
                                        modifier = Modifier
                                            .size(96.dp, 148.dp)
                                            .clip(
                                                RoundedCornerShape(20.dp)
                                            ),
                                        contentScale = ContentScale.Crop
                                    )
                                    Box(modifier = Modifier.fillMaxWidth()) {
                                        Column(
                                            Modifier
                                                .height(148.dp)
                                                .padding(horizontal = 5.dp),
                                            verticalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Text(
                                                text = project.title,
                                                fontSize = 16.sp,
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis,
                                                fontWeight = FontWeight.Bold,
                                            )
                                            Text(
                                                text = project.desc,
                                                fontSize = 14.sp,
                                                maxLines = 3,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                            Text(
                                                text = project.niceDate + "  " + project.author,
                                                fontSize = 12.sp,
                                            )
                                        }
                                        LikeIcon(
                                            modifier = Modifier.align(Alignment.BottomEnd),
                                            size = 30.dp,
                                            liked = like
                                        ) {
                                            if (like) vmP.dispatch(PublicIntent.UnCollect(project.id))
                                            else vmP.dispatch(PublicIntent.Collect(project.id))
                                            like = !like
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }




    }

}