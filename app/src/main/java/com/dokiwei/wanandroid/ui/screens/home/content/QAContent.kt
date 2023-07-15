package com.dokiwei.wanandroid.ui.screens.home.content

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.dokiwei.wanandroid.ui.component.Loading
import com.dokiwei.wanandroid.util.TextUtil
import com.dokiwei.wanandroid.util.TimeDiffString
import java.net.URLEncoder

/**
 * @author DokiWei
 * @date 2023/7/14 22:36
 */
@Composable
fun QAContent(navController: NavController) {
    val vm: QAContentViewModel = viewModel()
    val qaList by vm.qaList.collectAsState()
    if (qaList.isEmpty()) Loading()
    AnimatedVisibility(
        visible = qaList.isNotEmpty(),
        enter = slideInVertically(animationSpec = tween(800))
    ) {
        LazyColumn {
            items(qaList.size) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp, 5.dp)
                        .clickable {
                            val link = URLEncoder.encode(
                                qaList[it].link,
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
                                text = qaList[it].title,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                        },
                        supportingContent = {
                            Text(text = qaList[it].superChapterName + "/" + qaList[it].chapterName)
                        },
                        overlineContent = {
                            Text(
                                text = TextUtil.getArticleText(
                                    qaList[it].author,
                                    qaList[it].shareUser
                                )
                            )
                        },
                        trailingContent = {
                            Text(
                                text =
                                if (TimeDiffString.isDateString(qaList[it].niceShareDate))
                                    TimeDiffString.getTimeDiffString(qaList[it].niceShareDate)
                                else
                                    qaList[it].niceShareDate
                            )
                        }
                    )
                }
            }
        }
    }
}