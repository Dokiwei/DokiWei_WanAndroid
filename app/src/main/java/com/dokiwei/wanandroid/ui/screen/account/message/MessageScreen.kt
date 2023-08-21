package com.dokiwei.wanandroid.ui.screen.account.message

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.dokiwei.wanandroid.model.apidata.MessageData
import com.dokiwei.wanandroid.model.util.OtherScreen
import com.dokiwei.wanandroid.model.util.TimeDiffString
import com.dokiwei.wanandroid.model.util.myCustomNavigate
import com.dokiwei.wanandroid.model.util.randomAvatar
import com.dokiwei.wanandroid.ui.widgets.CardContent
import com.dokiwei.wanandroid.ui.widgets.MyScrollableTabRow
import java.net.URLEncoder

/**
 * @author DokiWei
 * @date 2023/8/6 16:59
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessageScreen(navController: NavController) {
    val vm: MessageViewModel = viewModel()
    val state = vm.state.collectAsState()
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(title = {
                MyScrollableTabRow(titleList = listOf("未读", "已读"),
                    selectedTabIndex = state.value.selectedTabIndex,
                    onClick = {
                        vm.dispatch(MessageIntent.OnTabSelected(it))
                    })
            }, navigationIcon = {
                IconButton(onClick = {
                    navController.navigateUp()
                }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack, contentDescription = "返回上一页"
                    )
                }
            })
        },
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (state.value.selectedTabIndex) {
                0 -> {
                    AnimatedVisibility(
                        visible = state.value.data.unReadList.isNotEmpty(),
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {
                        LazyColumn {
                            items(state.value.data.unReadList.size) {
                                val item = state.value.data.unReadList[it]
                                Item(item, navController)
                            }
                        }
                    }
                    if (state.value.data.unReadList.isEmpty()) Text(
                        modifier = Modifier.align(
                            Alignment.Center
                        ), text = "所有的消息都已经读取过了😋"
                    )
                }

                1 -> {
                    AnimatedVisibility(
                        visible = state.value.data.readList.isNotEmpty(),
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {
                        LazyColumn {
                            items(state.value.data.readList.size) {
                                val item = state.value.data.readList[it]
                                Item(item, navController)
                            }
                        }
                    }
                    if (state.value.data.readList.isEmpty()) Text(
                        modifier = Modifier.align(
                            Alignment.Center
                        ), text = "您还没有收到任何消息,尝试去收藏一些文章吧🤩"
                    )
                }
            }
        }

    }
}

@Composable
private fun Item(
    item: MessageData, navController: NavController
) {
    CardContent(onClick = {
        val link = URLEncoder.encode(
            item.fullLink, "UTF-8"
        )
        navController.myCustomNavigate("网页/$link")
    }) {
        val painterID = remember { randomAvatar() }
        ListItem(leadingContent = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                IconButton(onClick = {
                    navController.navigate(
                        "${OtherScreen.UserArticles.route}/${item.fromUserId}/${if (item.fromUser == "") "默认名称" else item.fromUser}"
                    )
                }) {
                    Image(
                        painter = painterResource(painterID), contentDescription = null
                    )
                }
                Text(text = item.fromUser)
            }
        },
            overlineContent = { Text(text = item.title) },
            headlineContent = { Text(text = item.message) },
            trailingContent = {
                Text(
                    text = if (TimeDiffString.isDateString(item.niceDate)) TimeDiffString.getTimeDiffString(
                        item.niceDate
                    ) else item.niceDate
                )
            })
    }
}