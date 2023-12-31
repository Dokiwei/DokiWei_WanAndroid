package com.dokiwei.wanandroid.ui.widgets

import android.annotation.SuppressLint
import android.text.Html
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.IconButton
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
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.dokiwei.wanandroid.model.apidata.ArticleData
import com.dokiwei.wanandroid.model.apidata.CollectData
import com.dokiwei.wanandroid.model.util.OtherScreen
import com.dokiwei.wanandroid.model.util.TextUtil
import com.dokiwei.wanandroid.model.util.TimeDiffString
import com.dokiwei.wanandroid.model.util.myCustomNavigate
import com.dokiwei.wanandroid.model.util.randomAvatar
import kotlinx.coroutines.launch
import java.net.URLEncoder

/**
 * @author DokiWei
 * @date 2023/7/30 22:28
 */

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SwipeLayout(
    modifier: Modifier = Modifier, onRefresh: () -> Unit, content: @Composable () -> Unit
) {
    val refresh by remember { mutableStateOf(false) }
    val pullRefreshState = rememberPullRefreshState(refresh, {
        onRefresh()
    })
    Box(modifier = modifier.pullRefresh(pullRefreshState)) {
        content()
        PullRefreshIndicator(
            refresh, pullRefreshState, Modifier.align(Alignment.TopCenter), scale = true
        )
    }
}

/**
 * 主页文章列表
 *
 * @param navController:用于跳转其他页面
 * @param isRefreshing:控制页面刷新状态
 * @param isLoadingMore:控制加载更多状态
 * @param isToTop:当值发生变化时回到顶部,默认为false
 * @param heightLight:高亮显示关键词
 * @param items:[ArticleData]数据集合
 * @param onRefresh:刷新事件
 * @param onLoadMore:加载更多事件
 * @param onCollectClick:搜藏事件,方法会回传两个参数 -> [ArticleData]:当前文章数据 [Boolean]:文章收藏状态
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
    items: List<ArticleData>,
    painterId: Int? = null,
    onRefresh: () -> Unit,
    onLoadMore: () -> Unit,
    onCollectClick: (ArticleData, Boolean) -> Unit,
    noDataContent: @Composable (() -> Unit)? = null,
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
            noDataContent,
            items,
            onLoadMore,
            navController,
            heightLight,
            heightLightList,
            onCollectClick,
            isLoadingMore,
            painterId
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
private fun HomeItems(
    modifier: Modifier = Modifier,
    lazyListState: LazyListState,
    headContent: @Composable (() -> Unit)?,
    noDataContent: @Composable (() -> Unit)? = null,
    items: List<ArticleData>,
    onLoadMore: () -> Unit,
    navController: NavController,
    heightLight: String,
    heightLightList: List<String>,
    onCollectClick: (ArticleData, Boolean) -> Unit,
    isLoadingMore: Boolean,
    painterId: Int?
) {
    LazyColumn(state = lazyListState, modifier = modifier) {
        headContent?.let { item { it.invoke() } }
        items(items.size) { index ->
            val painterID = painterId ?: remember { randomAvatar() }
            val item = items[index]
            var like by mutableStateOf(item.collect)
            LaunchedEffect(index) {
                if (items.size - index == 10) onLoadMore()
            }

            CardContent(onClick = {
                val link = URLEncoder.encode(item.link, "UTF-8")
                navController.myCustomNavigate("${OtherScreen.WebView.route}/$link")
            }) {
                ListItem(leadingContent = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        IconButton(onClick = {
                            navController.navigate(
                                "${OtherScreen.UserArticles.route}/${item.userId}/${
                                    TextUtil.getAuthor(
                                        item.author, item.shareUser
                                    )
                                }"
                            )
                        }) {
                            Image(
                                painter = painterResource(painterID), contentDescription = null
                            )
                        }

                        Text(
                            text = TextUtil.getAuthorOrShareUser(
                                item.author, item.shareUser
                            )
                        )
                    }
                }, overlineContent = {
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
                                            "https://wanandroid.com/${it.url}", "UTF-8"
                                        )
                                        navController.myCustomNavigate("${OtherScreen.WebView.route}/$link")
                                    }, text = it.name, color = MaterialTheme.colorScheme.onTertiary
                            )
                            Box(modifier = Modifier.width(10.dp))
                        }
                        Text(
                            text = item.superChapterName + "/" + item.chapterName,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }, headlineContent = {
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
                                                startIndex, startIndex + heightLight.length
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
                            text = annotatedText, maxLines = 2, overflow = TextOverflow.Ellipsis
                        )
                    } else Text(
                        text = item.title, maxLines = 2, overflow = TextOverflow.Ellipsis
                    )
                }, trailingContent = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = if (TimeDiffString.isDateString(item.niceDate)) TimeDiffString.getTimeDiffString(
                                item.niceDate
                            )
                            else item.niceDate
                        )
                        LikeIcon(
                            size = 30.dp, liked = like
                        ) {
                            onCollectClick(item, like)
                            like = !like
                        }
                    }
                })
            }

            if (items.size - 1 == index) Spacer(modifier = Modifier.height(20.dp))

        }
        if (items.isEmpty()) item {
            noDataContent?.invoke()
            if (noDataContent == null) Text(text = "数据为空")
        }
        item {
            AnimatedVisibility(
                visible = isLoadingMore, enter = scaleIn(), exit = scaleOut()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
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

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SwipeCollectsItemsLayout(
    navController: NavController,
    isRefreshing: Boolean,
    isLoadingMore: Boolean,
    isToTop: Boolean,
    items: List<CollectData>,
    onRefresh: () -> Unit,
    onLoadMore: () -> Unit,
    onCollectClick: (CollectData, Boolean) -> Unit,
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


@SuppressLint("UnrememberedMutableState")
@Composable
private fun CollectItems(
    lazyListState: LazyListState,
    items: List<CollectData>,
    onLoadMore: () -> Unit,
    navController: NavController,
    onCollectClick: (CollectData, Boolean) -> Unit,
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
            val painterID = remember { randomAvatar() }
            val item = items[index]
            var like by mutableStateOf(true)
            LaunchedEffect(index) {
                if (items.size - index == 1) onLoadMore()
            }

            CardContent(onClick = {
                val link = URLEncoder.encode(item.link, "UTF-8")
                navController.navigate("${OtherScreen.WebView.route}/$link")
            }) {
                ListItem(leadingContent = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        IconButton(onClick = {}) {
                            Image(
                                painter = painterResource(painterID), contentDescription = null
                            )
                        }
                        Text(text = item.author)
                    }
                }, headlineContent = {
                    Text(
                        text = item.title, maxLines = 2, overflow = TextOverflow.Ellipsis
                    )
                }, overlineContent = {
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

                    }
                }, trailingContent = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = if (TimeDiffString.isDateString(item.niceDate)) TimeDiffString.getTimeDiffString(
                                item.niceDate
                            ) else item.niceDate
                        )
                        LikeIcon(
                            size = 30.dp, liked = like
                        ) {
                            onCollectClick(item, like)
                            like = !like
                        }
                    }

                })
            }
            if (items.size - 1 == index) Spacer(modifier = Modifier.height(20.dp))
        }

        item {
            AnimatedVisibility(
                visible = isLoadingMore, enter = scaleIn(), exit = scaleOut()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
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