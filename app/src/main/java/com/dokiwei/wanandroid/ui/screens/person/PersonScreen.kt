package com.dokiwei.wanandroid.ui.screens.person

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Info
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.dokiwei.wanandroid.ui.component.mainBody

/**
 * @author DokiWei
 * @date 2023/7/9 17:30
 */
@Composable
fun PersonScreen() {
    val vm: PersonViewModel = viewModel()
    val context = LocalContext.current
    val userInfoData by vm.userInfoList.collectAsState()
    val isGetUserInfoSuccess = userInfoData != null
    Column(Modifier.mainBody()) {
        ListItem(
            headlineContent = {
                Text(
                    text = if (isGetUserInfoSuccess) userInfoData!!.userInfo.username else "name",
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold
                )
            },
            supportingContent = {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Filled.Email,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = if (isGetUserInfoSuccess && userInfoData!!.userInfo.email.isNotEmpty())
                                userInfoData!!.userInfo.email else "27546436887@qq.com"
                        )
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Filled.Info,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Text(text = if (isGetUserInfoSuccess) "积分:"+userInfoData!!.coinInfo.coinCount+" 排名:"+userInfoData!!.coinInfo.rank+"\n等级:"+userInfoData!!.coinInfo.level else "积分:333 排名:22\n等级:111")
                    }
                }

            },
            leadingContent = {
                AsyncImage(
                    model = "https://p.qqan.com/up/2020-12/16070652276806379.jpg",
                    contentDescription = null,
                    Modifier
                        .size(120.dp)
                        .clip(RoundedCornerShape(20.dp))
                )
            }
        )
        IconButton(onClick = { vm.logout(context) }) {
            Icon(
                imageVector = Icons.Filled.Close,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun Pre() {
    PersonScreen()
}