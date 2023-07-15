package com.dokiwei.wanandroid.ui.screens.home.content

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.ListItem
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.dokiwei.wanandroid.ui.component.CardContent
import com.dokiwei.wanandroid.ui.component.LikeIcon
import com.dokiwei.wanandroid.ui.component.Loading
import com.dokiwei.wanandroid.ui.component.PageIndicator
import com.dokiwei.wanandroid.util.TextUtil
import com.dokiwei.wanandroid.util.TimeDiffString
import kotlinx.coroutines.delay
import java.net.URLEncoder

/**
 * @author DokiWei
 * @date 2023/7/14 22:35
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeContent(navController: NavController) {
    val vm: HomeContentViewModel = viewModel()
    val context = LocalContext.current
    val bannerData by vm.bannerList.collectAsState()
    val articleList by vm.articleList.collectAsState()
    val pagerState = rememberPagerState(
        pageCount = { bannerData.size },
        initialPage = 0,
        initialPageOffsetFraction = 0f
    )
    LaunchedEffect(Unit) {
        //轮播图
        while (true) {
            delay(3000)
            val nextPage = (pagerState.currentPage + 1) % bannerData.size
            pagerState.animateScrollToPage(nextPage)
        }
    }
    if (bannerData.isEmpty() && articleList.isEmpty()) Loading()
    //如果banner数据不为空进行数据的展&&如果首页文章内容不为空则对首页文章进行懒加载
    AnimatedVisibility(
        visible = bannerData.isNotEmpty() && articleList.isNotEmpty(),
        enter = slideInVertically(animationSpec = tween(800))
    ) {
        LazyColumn {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp, 5.dp),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    HorizontalPager(
                        state = pagerState,
                    ) { index ->
                        Box {
                            AsyncImage(
                                model = bannerData[index].imagePath,
                                contentDescription = null,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .aspectRatio(7 / 3f),
                                contentScale = ContentScale.Crop
                            )
                            PageIndicator(
                                pagerState = pagerState,
                                pageCount = bannerData.size,
                                modifier = Modifier
                                    .align(Alignment.BottomCenter)
                                    .padding(8.dp)
                            )
                        }
                    }
                }
            }
            items(articleList.size) {
                var like by remember {
                    mutableStateOf(articleList[it].collect)
                }
                CardContent(onClick = {
                    val link = URLEncoder.encode(
                        articleList[it].link,
                        "UTF-8"
                    )
                    navController.navigate("网页/$link")
                }) {
                    ListItem(
                        headlineContent = {
                            Text(
                                text = articleList[it].title,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                        },
                        supportingContent = {
                            Text(text = articleList[it].superChapterName + "/" + articleList[it].chapterName)
                        },
                        overlineContent = {
                            Row {
                                Text(
                                    text = TextUtil.getArticleText(
                                        articleList[it].author,
                                        articleList[it].shareUser
                                    )
                                )
                                if (articleList[it].fresh) {
                                    Text(
                                        modifier = Modifier.padding(start = 10.dp),
                                        text = "新",
                                        color = Color.Red
                                    )
                                }
                            }
                        },
                        trailingContent = {
                            Box(modifier = Modifier.size(40.dp, 80.dp)) {
                                Text(
                                    modifier = Modifier.align(Alignment.TopCenter),
                                    text =
                                    if (TimeDiffString.isDateString(articleList[it].niceShareDate))
                                        TimeDiffString.getTimeDiffString(articleList[it].niceShareDate)
                                    else
                                        articleList[it].niceShareDate
                                )
                                LikeIcon(
                                    modifier = Modifier.align(Alignment.BottomCenter),
                                    size = 30.dp,
                                    liked = like
                                ) {
                                    if (like) {
                                        vm.unlikeArticle(
                                            articleList[it].id,
                                            context
                                        ) { success ->
                                            like = success
                                        }
                                    } else {
                                        vm.likeArticle(
                                            articleList[it].id,
                                            context
                                        ) { success ->
                                            like = success
                                        }
                                    }
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}