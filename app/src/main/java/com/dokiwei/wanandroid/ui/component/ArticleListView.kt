package com.dokiwei.wanandroid.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.dokiwei.wanandroid.data.ArticleListData
import com.dokiwei.wanandroid.util.TextUtil
import com.dokiwei.wanandroid.util.TimeDiffString

/**
 * @author DokiWei
 * @date 2023/7/16 22:49
 *
 * 文章列表
 *
 * @param articles:文章列表数据
 * @param onArticleClick:点击文章事件
 * @param onLikeClick:点击收藏事件(如果不需要请将visibleLike传入false)
 * @param lazyListState:懒加载列表状态
 * @param banner:轮播图(不使用传入空方法体)
 */
@Composable
fun ArticleListView(
    articles: List<ArticleListData>,
    onArticleClick: (ArticleListData) -> Unit,
    onTagClick: (String) -> Unit,
    onLikeClick: (ArticleListData, Boolean) -> Boolean,
    lazyListState: LazyListState,
    banner: @Composable () -> Unit
) {
    LazyColumn(state = lazyListState) {
        item { banner() }
        items(articles.size) { index ->
            val article = articles[index]
            var like by remember { mutableStateOf(article.collect) }
            CardContent(
                onClick = { onArticleClick(article) }
            ) {
                ListItem(
                    headlineContent = {
                        Text(
                            text = article.title,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    },
                    supportingContent = {
                        Text(text = article.superChapterName + "/" + article.chapterName)
                    },
                    overlineContent = {
                        Row {
                            if (article.fresh) {
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
                            article.tags.forEach {
                                Text(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(5.dp))
                                        .background(MaterialTheme.colorScheme.tertiary)
                                        .padding(4.dp, 2.dp)
                                        .clickable { onTagClick("https://wanandroid.com/${it.url}") },
                                    text = it.name,
                                    color = MaterialTheme.colorScheme.onTertiary
                                )
                                Box(modifier = Modifier.width(10.dp))
                            }
                            Text(
                                modifier = Modifier.padding(4.dp, 2.dp),
                                text = TextUtil.getArticleText(
                                    article.author,
                                    article.shareUser
                                ),
                                fontWeight = FontWeight.Bold
                            )
                        }
                    },
                    trailingContent = {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text =
                                if (TimeDiffString.isDateString(article.niceShareDate))
                                    TimeDiffString.getTimeDiffString(article.niceShareDate)
                                else
                                    article.niceShareDate
                            )
                            LikeIcon(
                                size = 30.dp,
                                liked = like
                            ) {
                                onLikeClick(article, like)
                                like = !like
                            }
                        }

                    }
                )
            }
        }
    }
}