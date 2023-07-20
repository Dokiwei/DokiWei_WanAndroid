package com.dokiwei.wanandroid.ui.screens.person.mylike

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.dokiwei.wanandroid.ui.component.CardContent
import com.dokiwei.wanandroid.ui.component.LikeIcon
import com.dokiwei.wanandroid.ui.component.MySwipeRefresh
import com.dokiwei.wanandroid.util.ToastUtil
import java.net.URLEncoder

/**
 * @author DokiWei
 * @date 2023/7/19 21:08
 */
@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyLike(navController: NavController) {
    val vm: MyLikeViewModel = viewModel()
    val context = LocalContext.current

    val myLikeItems by vm.myLikeItems.collectAsState()
    val isRefresh = vm.isRefresh
    var scrollToTop by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(text = "我的收藏") })
        },
        floatingActionButton = {
            SmallFloatingActionButton(
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                shape = FloatingActionButtonDefaults.largeShape,
                onClick = {
                    scrollToTop = !scrollToTop
                }
            ) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowUp,
                    contentDescription = "Scroll to top"
                )
            }
        }
    ) { innerPadding ->
        MySwipeRefresh(
            modifier = Modifier.padding(innerPadding),
            scrollToTop = scrollToTop,
            isRefreshing = isRefresh,
            onRefresh = { vm.onRefresh() },
            onLoadMore = {
                if (myLikeItems.size > 10 && it > (myLikeItems.size - 5)) {
                    vm.loadMore()
                    ToastUtil.showMsg(context, "加载新内容...")
                }
            }
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                state = it
            ) {
                if (myLikeItems.size == 0) {
                    item {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) { Text(text = "还没有收藏,快去首页看看吧!!") }
                    }
                }
                items(myLikeItems.size) { index ->
                    val item = myLikeItems[index]
                    var like by mutableStateOf(true)

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
                                        if (like) {
                                            if (item.originId == -1) vm.unlikeCustom(
                                                item.id,
                                                context
                                            )
                                            else vm.unlike(item.originId, context)
                                        } else {
                                            if (item.originId == -1) vm.likeCustom(item.id, context)
                                            else vm.like(item.originId, context)
                                        }
                                        like = !like
                                    }
                                }

                            }
                        )
                    }
                }
            }
        }
    }

}
