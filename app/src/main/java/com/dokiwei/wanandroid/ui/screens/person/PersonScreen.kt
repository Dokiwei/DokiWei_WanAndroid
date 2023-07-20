package com.dokiwei.wanandroid.ui.screens.person

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.dokiwei.wanandroid.R
import com.dokiwei.wanandroid.ui.component.CardContent
import com.dokiwei.wanandroid.ui.component.mainBody

/**
 * @author DokiWei
 * @date 2023/7/9 17:30
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonScreen(navController: NavController) {
    val vm: PersonViewModel = viewModel()
    val context = LocalContext.current
    val userInfoData by vm.userInfoList.collectAsState()
    val isGetUserInfoSuccess = userInfoData != null
    Column(Modifier.mainBody()) {
        ListItem(headlineContent = {
            Text(
                text = if (isGetUserInfoSuccess) userInfoData!!.userInfo.username else "请先登录",
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
        Column {
            CardContent(
                onClick = {}
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        modifier = Modifier.size(48.dp),
                        painter = painterResource(id = R.drawable.ic_sms),
                        contentDescription = null,
                    )
                    Text(text = "我的消息")
                }
            }
            CardContent(
                onClick = { navController.navigate("我的收藏") }
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        modifier = Modifier.size(48.dp),
                        painter = painterResource(id = R.drawable.ic_like),
                        contentDescription = null,
                    )
                    Text(text = "我的收藏")
                }
            }
            CardContent(
                onClick = {}
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        modifier = Modifier.size(48.dp),
                        painter = painterResource(id = R.drawable.ic_like),
                        contentDescription = null,
                    )
                    Text(text = "关于")
                }
            }
            CardContent(
                onClick = {
                    if (userInfoData != null)
                        vm.logout(context)
                    navController.navigate("登录页")
                }
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        modifier = Modifier.size(48.dp),
                        painter = painterResource(id = R.drawable.ic_logout),
                        contentDescription = null
                    )
                    Text(text = if (userInfoData == null) "登录" else "退出登录")
                }
            }
        }

    }
}