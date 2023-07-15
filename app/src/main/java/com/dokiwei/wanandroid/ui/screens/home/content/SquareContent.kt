package com.dokiwei.wanandroid.ui.screens.home.content

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.dokiwei.wanandroid.ui.component.LikeIcon
import com.dokiwei.wanandroid.ui.component.Loading
import com.dokiwei.wanandroid.util.TextUtil
import com.dokiwei.wanandroid.util.TimeDiffString
import java.net.URLEncoder

/**
 * @author DokiWei
 * @date 2023/7/14 22:35
 */
@Composable
fun SquareContent(navController: NavController) {
    val vm: SquareContentViewModel = viewModel()
    val context = LocalContext.current
    val articleList by vm.userArticleList.collectAsState()
    if (articleList.isEmpty()) Loading()
    AnimatedVisibility(
        visible = articleList.isNotEmpty(),
        enter = slideInVertically(animationSpec = tween(800))
    ) {
        LazyColumn {
            items(articleList.size) {
                var like by remember {
                    mutableStateOf(articleList[it].collect)
                }
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp, 5.dp)
                        .clickable {
                            val link = URLEncoder.encode(
                                articleList[it].link,
                                "UTF-8"
                            )
                            navController.navigate("网页/$link")
                        },
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
                ) {
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