package com.dokiwei.wanandroid.ui.component

import android.annotation.SuppressLint
import android.text.Html
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.dokiwei.wanandroid.bean.ArticleBean
import com.dokiwei.wanandroid.bean.CollectBean
import com.dokiwei.wanandroid.bean.ProjectBean
import com.dokiwei.wanandroid.util.TextUtil
import com.dokiwei.wanandroid.util.TimeDiffString
import com.dokiwei.wanandroid.util.myCustomNavigate
import kotlinx.coroutines.launch
import java.net.URLEncoder

/**
 * @author DokiWei
 * @date 2023/7/30 22:28
 */

/**
 * 主页文章列表
 *
 * @param navController:用于跳转其他页面
 * @param isRefreshing:控制页面刷新状态
 * @param isLoadingMore:控制加载更多状态
 * @param isToTop:当值发生变化时回到顶部,默认为false
 * @param heightLight:高亮显示关键词
 * @param items:[ArticleBean]数据集合
 * @param onRefresh:刷新事件
 * @param onLoadMore:加载更多事件
 * @param onCollectClick:搜藏事件,方法会回传两个参数 -> [ArticleBean]:当前文章数据 [Boolean]:文章收藏状态
 * @param headContent:顶部布局,可以为null
 */
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SwipeHomeItemsLayout(
    modifier: Modifier = Modifier,
    navController: NavController,
    isRefreshing: Boolean,
    isLoadingMore: Boolean,
    isToTop: Boolean = false,
    heightLight: String = "",
    items: List<ArticleBean>,
    onRefresh: () -> Unit,
    onLoadMore: () -> Unit,
    onCollectClick: (ArticleBean, Boolean) -> Unit,
    headContent: @Composable (() -> Unit)? = null
) {
    val pullRefreshState = rememberPullRefreshState(isRefreshing, { onRefresh() })
    val coroutineScope = rememberCoroutineScope()
    val lazyListState = rememberLazyListState()
    val heightLightList = heightLight.split(" ")

    LaunchedEffect(isToTop) {
        coroutineScope.launch { lazyListState.animateScrollToItem(0) }
    }
    Box(modifier = Modifier.pullRefresh(pullRefreshState)) {
        HomeItems(
            modifier,
            lazyListState,
            headContent,
            items,
            onLoadMore,
            navController,
            heightLight,
            heightLightList,
            onCollectClick,
            isLoadingMore
        )

        PullRefreshIndicator(
            isRefreshing,
            pullRefreshState,
            Modifier.align(Alignment.TopCenter),
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SwipeCollectsItemsLayout(
    navController: NavController,
    isRefreshing: Boolean,
    isLoadingMore: Boolean,
    isToTop: Boolean,
    items: List<CollectBean>,
    onRefresh: () -> Unit,
    onLoadMore: () -> Unit,
    onCollectClick: (CollectBean, Boolean) -> Unit,
) {
    val pullRefreshState = rememberPullRefreshState(isRefreshing, { onRefresh() })
    val coroutineScope = rememberCoroutineScope()
    val lazyListState = rememberLazyListState()

    LaunchedEffect(isToTop) {
        coroutineScope.launch { lazyListState.animateScrollToItem(0) }
    }
    Box(modifier = Modifier.pullRefresh(pullRefreshState)) {
        CollectItems(lazyListState, items, onLoadMore, navController, onCollectClick, isLoadingMore)

        PullRefreshIndicator(
            isRefreshing,
            pullRefreshState,
            Modifier.align(Alignment.TopCenter),
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SwipeProjectItemsLayout(
    modifier: Modifier,
    navController: NavController,
    isRefreshing: Boolean,
    isLoadingMore: Boolean,
    isToTop: Boolean = false,
    items: List<ProjectBean>,
    onRefresh: () -> Unit,
    onLoadMore: () -> Unit,
    onCollectClick: (ProjectBean, Boolean) -> Unit
) {
    val pullRefreshState = rememberPullRefreshState(isRefreshing, { onRefresh() })
    val coroutineScope = rememberCoroutineScope()
    val lazyListState = rememberLazyListState()

    LaunchedEffect(isToTop) {
        coroutineScope.launch { lazyListState.animateScrollToItem(0) }
    }
    Box(modifier = Modifier.pullRefresh(pullRefreshState)) {
        ProjectItems(
            modifier,
            lazyListState,
            items,
            onLoadMore,
            navController,
            onCollectClick,
            isLoadingMore
        )

        PullRefreshIndicator(
            isRefreshing,
            pullRefreshState,
            Modifier.align(Alignment.TopCenter),
        )
    }
}

@SuppressLint("UnrememberedMutableState")
@Composable
private fun ProjectItems(
    modifier: Modifier,
    lazyListState: LazyListState,
    items: List<ProjectBean>,
    onLoadMore: () -> Unit,
    navController: NavController,
    onCollectClick: (ProjectBean, Boolean) -> Unit,
    isLoadingMore: Boolean
) {
    LazyColumn(
        modifier,
        state = lazyListState
    ) {
        items(items.size) {
            val project = items[it]
            var like by mutableStateOf(project.collect)
            LaunchedEffect(it) {
                if (items.size - it == 1) onLoadMore()
            }
            CardContent(onClick = {
                val link = URLEncoder.encode(project.link, "UTF-8")
                navController.myCustomNavigate("网页/$link")
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
                            onCollectClick(project, like)
                            like = !like
                        }
                    }
                }
            }
        }

        item {
            AnimatedVisibility(
                visible = isLoadingMore,
                enter = scaleIn(),
                exit = scaleOut()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "加载中...")
                    CircularProgressIndicator()
                }
            }
        }

    }
}


@SuppressLint("UnrememberedMutableState")
@Composable
private fun CollectItems(
    lazyListState: LazyListState,
    items: List<CollectBean>,
    onLoadMore: () -> Unit,
    navController: NavController,
    onCollectClick: (CollectBean, Boolean) -> Unit,
    isLoadingMore: Boolean
) {
    LazyColumn(
        state = lazyListState
    ) {
        if (items.isEmpty()) {
            item {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) { Text(text = "还没有收藏,快去首页看看吧!!") }
            }
        }
        items(items.size) { index ->
            val item = items[index]
            var like by mutableStateOf(true)
            LaunchedEffect(index) {
                if (items.size - index == 1) onLoadMore()
            }

            CardContent(
                onClick = {
                    val link = URLEncoder.encode(item.link, "UTF-8")
                    navController.navigate("网页/$link")
                }
            ) {
                ListItem(
                    headlineContent = {
                        Text(
                            text = item.title,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    },
                    overlineContent = {
                        Row {
                            Text(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(5.dp))
                                    .background(MaterialTheme.colorScheme.tertiary)
                                    .padding(4.dp, 2.dp),
                                text = if (item.originId == -1) "站外" else "站内",
                                color = MaterialTheme.colorScheme.onTertiary
                            )
                            Box(modifier = Modifier.width(10.dp))
                            if (item.desc.isNotEmpty()) {
                                Text(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(5.dp))
                                        .background(MaterialTheme.colorScheme.tertiary)
                                        .padding(4.dp, 2.dp),
                                    text = if (item.envelopePic.isNotEmpty()) "项目" else "问答",
                                    color = MaterialTheme.colorScheme.onTertiary
                                )
                                Box(modifier = Modifier.width(10.dp))
                            }
                            Text(
                                modifier = Modifier.padding(4.dp, 2.dp),
                                text = item.author,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    },
                    trailingContent = {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = item.niceDate
                            )
                            LikeIcon(
                                size = 30.dp,
                                liked = like
                            ) {
                                onCollectClick(item, like)
                                like = !like
                            }
                        }

                    }
                )
            }
        }

        item {
            AnimatedVisibility(
                visible = isLoadingMore,
                enter = scaleIn(),
                exit = scaleOut()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "加载中...")
                    CircularProgressIndicator()
                }
            }
        }
    }
}


@SuppressLint("UnrememberedMutableState")
@Composable
private fun HomeItems(
    modifier: Modifier = Modifier,
    lazyListState: LazyListState,
    headContent: @Composable (() -> Unit)?,
    items: List<ArticleBean>,
    onLoadMore: () -> Unit,
    navController: NavController,
    heightLight: String,
    heightLightList: List<String>,
    onCollectClick: (ArticleBean, Boolean) -> Unit,
    isLoadingMore: Boolean
) {
    LazyColumn(state = lazyListState, modifier = modifier) {
        item { headContent?.let { it() } }
        items(items.size) { index ->
            val item = items[index]
            var like by mutableStateOf(item.collect)
            LaunchedEffect(index) {
                if (items.size - index == 1) onLoadMore()
            }

            CardContent(
                onClick = {
                    val link = URLEncoder.encode(item.link, "UTF-8")
                    navController.myCustomNavigate("网页/$link")
                }
            ) {
                ListItem(
                    headlineContent = {
                        if (heightLight.isNotEmpty()) {
                            val text = Html.fromHtml(item.title).toString()
                            val annotatedText = buildAnnotatedString {
                                var index1 = 0
                                heightLightList.forEach { heightLight ->
                                    val startIndex = text.indexOf(heightLight, index1, true)
                                    if (startIndex != -1) {
                                        append(text.substring(index1, startIndex))
                                        withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.primary)) {
                                            append(
                                                text.substring(
                                                    startIndex,
                                                    startIndex + heightLight.length
                                                )
                                            )
                                        }
                                        index1 = startIndex + heightLight.length
                                    }
                                }
                                if (index1 < text.length) {
                                    append(text.substring(index1))
                                }
                            }
                            Text(
                                text = annotatedText,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                        } else
                            Text(
                                text = item.title,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                    },
                    supportingContent = {
                        Text(text = item.superChapterName + "/" + item.chapterName)
                    },
                    overlineContent = {
                        Row {
                            if (item.fresh) {
                                Text(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(5.dp))
                                        .background(MaterialTheme.colorScheme.secondary)
                                        .padding(4.dp, 2.dp),
                                    text = "新",
                                    color = MaterialTheme.colorScheme.onSecondary
                                )
                                Box(modifier = Modifier.width(10.dp))
                            }
                            item.tags.forEach {
                                Text(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(5.dp))
                                        .background(MaterialTheme.colorScheme.tertiary)
                                        .padding(4.dp, 2.dp)
                                        .clickable {
                                            val link = URLEncoder.encode(
                                                "https://wanandroid.com/${it.url}",
                                                "UTF-8"
                                            )
                                            navController.myCustomNavigate("网页/$link")
                                        },
                                    text = it.name,
                                    color = MaterialTheme.colorScheme.onTertiary
                                )
                                Box(modifier = Modifier.width(10.dp))
                            }
                            Text(
                                modifier = Modifier.padding(4.dp, 2.dp),
                                text = TextUtil.getArticleText(
                                    item.author,
                                    item.shareUser
                                ),
                                fontWeight = FontWeight.Bold
                            )
                        }
                    },
                    trailingContent = {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text =
                                if (TimeDiffString.isDateString(item.niceDate))
                                    TimeDiffString.getTimeDiffString(item.niceDate)
                                else
                                    item.niceDate
                            )
                            LikeIcon(
                                size = 30.dp,
                                liked = like
                            ) {
                                onCollectClick(item, like)
                                like = !like
                            }
                        }
                    }
                )
            }

        }

        item {
            AnimatedVisibility(
                visible = isLoadingMore,
                enter = scaleIn(),
                exit = scaleOut()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "加载中...")
                    CircularProgressIndicator()
                }
            }
        }
    }
}