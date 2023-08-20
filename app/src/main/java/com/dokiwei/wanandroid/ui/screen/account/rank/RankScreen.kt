package com.dokiwei.wanandroid.ui.screen.account.rank

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.dokiwei.wanandroid.model.util.AccountScreen
import com.dokiwei.wanandroid.model.util.randomAvatar

/**
 * @author DokiWei
 * @date 2023/8/20 19:18
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RankScreen(navController: NavController) {
    val vm: RankViewModel = viewModel()

    val data by vm.rankData.collectAsState()

    Scaffold(topBar = {
        CenterAlignedTopAppBar(title = { Text(text = AccountScreen.Rank.route) }, navigationIcon = {
            IconButton(onClick = {
                navController.navigateUp()
            }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack, contentDescription = "返回上一页"
                )
            }
        })
    }, bottomBar = {
        data.userInfo?.let {
            BottomAppBar {
                ListItem(leadingContent = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = it.rank)
                        Spacer(modifier = Modifier.width(5.dp))
                        AsyncImage(
                            model = "https://p.qqan.com/up/2020-12/16070652276806379.jpg",
                            contentDescription = null,
                            Modifier.clip(RoundedCornerShape(80.dp))
                        )
                    }
                },
                    overlineContent = { Text(text = "Lv:${it.level}") },
                    headlineContent = { Text(text = "${it.username}(您)") },
                    trailingContent = {
                        Text(text = it.coinCount.toString(), fontSize = 18.sp)
                    })
            }
        }
    }) { innerPadding ->
        AnimatedVisibility(
            visible = !data.rank.isNullOrEmpty(), enter = fadeIn(), exit = fadeOut()
        ) {
            LazyColumn(Modifier.padding(innerPadding)) {
                data.rank?.let {
                    item {
                        ListItem(leadingContent = { Text(text = "排名", fontSize = 16.sp) },
                            headlineContent = {
                                Text(
                                    text = "仅显示排名前30",
                                    fontSize = 16.sp,
                                    color = DividerDefaults.color
                                )
                            },
                            trailingContent = {
                                Text(text = "积分", fontSize = 16.sp)
                            })
                    }
                    items(it.size) { index ->
                        val item = it[index]
                        ListItem(leadingContent = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(text = item.rank)
                                Spacer(modifier = Modifier.width(5.dp))
                                Image(
                                    painter = painterResource(randomAvatar()),
                                    contentDescription = null
                                )
                            }
                        }, overlineContent = {
                            Text(text = "Lv:${item.level}")

                        }, headlineContent = {
                            Text(text = item.username)
                        }, trailingContent = {
                            Text(text = item.coinCount.toString(), fontSize = 18.sp)
                        })
                        if (index != it.size - 1) Divider()
                    }
                }
            }

        }
    }
}