package com.dokiwei.wanandroid.ui.screen.home.content.home

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.dokiwei.wanandroid.model.apidata.BannerData
import com.dokiwei.wanandroid.model.entity.home.HomeEntity
import com.dokiwei.wanandroid.model.util.OtherScreen
import com.dokiwei.wanandroid.model.util.TextUtil
import com.dokiwei.wanandroid.model.util.TimeDiffString
import com.dokiwei.wanandroid.model.util.ToastAndLogcatUtil
import com.dokiwei.wanandroid.model.util.myCustomNavigate
import com.dokiwei.wanandroid.model.util.publicViewModel
import com.dokiwei.wanandroid.model.util.randomAvatar
import com.dokiwei.wanandroid.ui.main.PublicIntent
import com.dokiwei.wanandroid.ui.main.PublicViewModel
import com.dokiwei.wanandroid.ui.widgets.Banner
import com.dokiwei.wanandroid.ui.widgets.CardContent
import com.dokiwei.wanandroid.ui.widgets.LikeIcon
import com.dokiwei.wanandroid.ui.widgets.Loading
import com.dokiwei.wanandroid.ui.widgets.SwipeLayout
import java.net.URLEncoder

/**
 * @author DokiWei
 * @date 2023/7/14 22:35
 */
@Composable
fun HomeContent(
    navController: NavController, scrollToTop: Boolean
) {
    val vm = hiltViewModel<HomeViewModel>()
    val vmP = publicViewModel()

    val state by vm.state.collectAsState()
    val bannerData = state.bannerData.collectAsLazyPagingItems()
    val articleData = state.articleData.collectAsLazyPagingItems()

    val lazyListState = rememberLazyListState()

    when (articleData.loadState.refresh) {
        is LoadState.Loading -> Loading {
            bannerData.retry()
            articleData.retry()
        }

        is LoadState.Error -> (articleData.loadState.refresh as LoadState.Error).error.message?.let {
            ToastAndLogcatUtil.log(
                "HomePaging", msg = it
            )
        }

        is LoadState.NotLoading -> {}
    }

    LaunchedEffect(scrollToTop) {
        lazyListState.animateScrollToItem(0)
    }

    SwipeLayout(onRefresh = {
        bannerData.refresh()
        articleData.refresh()
    }) {
        HomeItem(lazyListState, bannerData, navController, articleData, vmP)
    }


}

@SuppressLint("UnrememberedMutableState")
@Composable
private fun HomeItem(
    lazyListState: LazyListState,
    bannerData: LazyPagingItems<BannerData>,
    navController: NavController,
    articleData: LazyPagingItems<HomeEntity>,
    vmP: PublicViewModel
) {
    LazyColumn(state = lazyListState) {
        item {
            AnimatedVisibility(
                visible = bannerData.loadState.refresh is LoadState.NotLoading, enter = fadeIn()
            ) {
                Banner(pageCount = bannerData.itemCount,
                    bannerData = bannerData.itemSnapshotList,
                    onClick = {
                        val link = URLEncoder.encode(it.url, "UTF-8")
                        navController.navigate("${OtherScreen.WebView.route}/$link")
                    })
            }
        }
        items(articleData.itemCount) { index ->
            val painterID = remember { randomAvatar() }
            val item = articleData[index] ?: return@items
            var like by mutableStateOf(item.collect)
            CardContent(onClick = {
                val link = URLEncoder.encode(item.link, "UTF-8")
                navController.myCustomNavigate("${OtherScreen.WebView.route}/$link")
            }) {
                ListItem(leadingContent = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Image(
                            painter = painterResource(painterID), contentDescription = null
                        )
                        Text(
                            text = TextUtil.getArticleText(
                                item.author, item.shareUser
                            )
                        )
                    }
                }, headlineContent = {
                    Text(
                        text = item.title, maxLines = 2, overflow = TextOverflow.Ellipsis
                    )
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
                            if (like) vmP.dispatch(PublicIntent.UnCollect(item.id))
                            else vmP.dispatch(PublicIntent.Collect(item.id))
                            like = !like
                        }
                    }
                })
            }

        }
    }
}
