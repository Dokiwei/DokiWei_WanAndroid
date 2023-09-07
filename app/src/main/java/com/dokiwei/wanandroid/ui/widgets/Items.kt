package com.dokiwei.wanandroid.ui.widgets

import android.annotation.SuppressLint
import android.text.Html
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.dokiwei.wanandroid.model.apidata.ArticleData
import com.dokiwei.wanandroid.model.apidata.BannerData
import com.dokiwei.wanandroid.model.entity.home.HomeEntity
import com.dokiwei.wanandroid.model.entity.home.QaEntity
import com.dokiwei.wanandroid.model.entity.home.SquareEntity
import com.dokiwei.wanandroid.model.util.OtherScreen
import com.dokiwei.wanandroid.model.util.TextUtil
import com.dokiwei.wanandroid.model.util.TimeDiffString
import com.dokiwei.wanandroid.model.util.converter.ArticleConverter
import com.dokiwei.wanandroid.model.util.myCustomNavigate
import com.dokiwei.wanandroid.model.util.randomAvatar
import com.dokiwei.wanandroid.ui.main.PublicIntent
import com.dokiwei.wanandroid.ui.main.PublicViewModel
import java.net.URLEncoder

/**
 * @author DokiWei
 * @date 2023/9/4 16:31
 */
@Composable
inline fun <reified T : Any> Items(
    lazyListState: LazyListState,
    bannerData: LazyPagingItems<BannerData>? = null,
    navController: NavController,
    articleData: LazyPagingItems<T>,
    vmP: PublicViewModel,
    searchKey: String = ""
) {
    val searchKeyList = searchKey.split(" ")
    LazyColumn(state = lazyListState) {
        val articleConverter = ArticleConverter()
        item {
            AnimatedVisibility(
                visible = bannerData?.loadState?.refresh is LoadState.NotLoading, enter = fadeIn()
            ) {
                bannerData?.let { dataList ->
                    Banner(pageCount = dataList.itemCount,
                        bannerData = dataList.itemSnapshotList,
                        onClick = {
                            val link = URLEncoder.encode(it.url, "UTF-8")
                            navController.navigate("${OtherScreen.WebView.route}/$link")
                        })
                }
            }
        }
        items(articleData.itemCount) { index ->
            val painterID = remember { randomAvatar() }
            val item = articleData[index] ?: return@items
            when (T::class.java) {
                QaEntity::class.java -> {
                    val data = with(item) {
                        articleConverter.fromEntity(this as QaEntity)
                    }
                    ItemContent(data, navController, painterID, vmP)
                }

                SquareEntity::class.java -> {
                    val data = with(item) {
                        articleConverter.fromEntity(this as SquareEntity)
                    }
                    ItemContent(data, navController, painterID, vmP)
                }

                HomeEntity::class.java -> {
                    val data = with(item) {
                        articleConverter.fromEntity(this as HomeEntity)
                    }
                    ItemContent(data, navController, painterID, vmP)
                }

                else -> ItemContent(
                    item as ArticleData, navController, painterID, vmP, searchKey, searchKeyList
                )
            }
        }
        articleData.itemCount.takeIf { it == 0 }.let {
            item {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = "文章为空"
                    )
                }
            }
        }
    }
}

@SuppressLint("UnrememberedMutableState")
@Composable
fun ItemContent(
    data: ArticleData,
    navController: NavController,
    painterID: Int,
    vmP: PublicViewModel,
    searchKey: String? = null,
    searchKeyList: List<String>? = null
) {
    var like by mutableStateOf(data.collect)
    CardContent(onClick = {
        val link = URLEncoder.encode(data.link, "UTF-8")
        navController.myCustomNavigate("${OtherScreen.WebView.route}/$link")
    }) {
        ListItem(leadingContent = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                IconButton(onClick = {
                    navController.navigate(
                        "${OtherScreen.UserArticles.route}/${data.userId}/${
                            TextUtil.getAuthor(
                                data.author, data.shareUser
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
                        data.author, data.shareUser
                    )
                )
            }
        }, headlineContent = {
            searchKey?.takeIf { it.isNotEmpty() }?.let {
                searchKeyList?.let {
                    val text = Html.fromHtml(data.title, Html.FROM_HTML_MODE_LEGACY).toString()
                    val annotatedText = buildAnnotatedString {
                        var index1 = 0
                        it.forEach { heightLight ->
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
                }
            }
            Text(
                text = data.title, maxLines = 2, overflow = TextOverflow.Ellipsis
            )
        }, overlineContent = {
            Row {
                if (data.fresh) {
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
                data.tags.forEach {
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
                    text = data.superChapterName + "/" + data.chapterName,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }, trailingContent = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = if (TimeDiffString.isDateString(data.niceDate)) TimeDiffString.getTimeDiffString(
                        data.niceDate
                    )
                    else data.niceDate
                )
                LikeIcon(
                    size = 30.dp, liked = like
                ) {
                    if (like) vmP.dispatch(PublicIntent.UnCollect(data.id))
                    else vmP.dispatch(PublicIntent.Collect(data.id))
                    like = !like
                }
            }
        })
    }
}