package com.dokiwei.wanandroid.ui.screens.person

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.dokiwei.wanandroid.R
import com.dokiwei.wanandroid.ui.component.cardContent
import com.dokiwei.wanandroid.ui.component.mainBody

/**
 * @author DokiWei
 * @date 2023/7/9 17:30
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonScreen() {
    val vm: PersonViewModel = viewModel()
    val context = LocalContext.current
    val userInfoData by vm.userInfoList.collectAsState()
    val isGetUserInfoSuccess = userInfoData != null
//    MySwipeRefresh(isRefreshing = , onRefresh = { /*TODO*/ }, onLoadMore = {}) {
//
//    }
    Column(Modifier.mainBody()) {
        ListItem(headlineContent = {
            Text(
                text = if (isGetUserInfoSuccess) userInfoData!!.userInfo.username else "name",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold
            )
        }, supportingContent = {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Filled.Email,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = if (isGetUserInfoSuccess && userInfoData!!.userInfo.email.isNotEmpty()) userInfoData!!.userInfo.email else "请先绑定邮箱"
                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Filled.Info,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Text(text = if (isGetUserInfoSuccess) "积分:" + userInfoData!!.coinInfo.coinCount + " 排名:" + userInfoData!!.coinInfo.rank + "\n等级:" + userInfoData!!.coinInfo.level else "积分:??? 排名:???\n等级:???")
                }
            }
        }, leadingContent = {
            AsyncImage(
                model = "https://p.qqan.com/up/2020-12/16070652276806379.jpg",
                contentDescription = null,
                Modifier
                    .size(120.dp)
                    .clip(RoundedCornerShape(20.dp))
            )
        })
        Card(Modifier.cardContent()) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                IconButton(
                    modifier = Modifier.size(64.dp),
                    onClick = { /*TODO*/ },
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            modifier = Modifier.size(36.dp),
                            painter = painterResource(id = R.drawable.ic_sms),
                            contentDescription = null,
                        )
                        Text(text = "我的消息", fontSize = 11.sp)
                    }
                }
                IconButton(
                    modifier = Modifier.size(64.dp),
                    onClick = { /*TODO*/ },
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            modifier = Modifier.size(36.dp),
                            painter = painterResource(id = R.drawable.ic_like),
                            contentDescription = null,
                        )
                        Text(text = "我的收藏", fontSize = 11.sp)
                    }
                }
                IconButton(
                    modifier = Modifier.size(64.dp),
                    onClick = { /*TODO*/ }
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            modifier = Modifier.size(36.dp),
                            painter = painterResource(id = R.drawable.ic_share),
                            contentDescription = null,
                        )
                        Text(text = "我的分享", fontSize = 11.sp)
                    }
                }
            }
        }
        Card(Modifier.cardContent()) {
            //todo:积分排行
        }
        Card(onClick = { vm.logout(context) }, Modifier.cardContent()) {
            Row(
                Modifier
                    .height(64.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    modifier = Modifier.size(36.dp),
                    painter = painterResource(id = R.drawable.ic_logout),
                    contentDescription = null
                )
                Text(text = "退出登录")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun Pre() {
    PersonScreen()
}