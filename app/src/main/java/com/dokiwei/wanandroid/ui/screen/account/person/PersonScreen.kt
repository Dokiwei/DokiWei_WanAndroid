package com.dokiwei.wanandroid.ui.screen.account.person

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.compose.rememberImagePainter
import com.dokiwei.wanandroid.model.util.AccountScreen
import com.dokiwei.wanandroid.model.util.MyBlurTransformation
import com.dokiwei.wanandroid.model.util.mainBody
import com.dokiwei.wanandroid.model.util.publicViewModel
import com.dokiwei.wanandroid.ui.main.PublicIntent
import com.dokiwei.wanandroid.ui.widgets.SettingButton

/**
 * @author DokiWei
 * @date 2023/7/9 17:30
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonScreen(navController: NavController) {
    val vm: PersonViewModel = viewModel()
    val vmP = publicViewModel()
    val notificationNum by vmP.unreadQuantity.collectAsState()
    val userInfoData by vm.userInfoList.collectAsState()
    var showLogoutDialog by remember { mutableStateOf(false) }

    val painter = rememberImagePainter(
        data = "https://p.qqan.com/up/2020-12/16070652276806379.jpg",
        builder = {
            transformations(
                MyBlurTransformation(
                    radius = 15f, sampling = 2f, context = LocalContext.current
                )
            )
        })
    val imageBoxColor = listOf(
        MaterialTheme.colorScheme.background,
        MaterialTheme.colorScheme.background.copy(alpha = 0.4f),
        Color.Black.copy(alpha = 0.2f),
        Color.Black.copy(alpha = 0.4f),
        Color.Black.copy(alpha = 0.6f),
        Color.Black.copy(alpha = 0.8f),
        Color.Black
    )

    AnimatedVisibility(visible = showLogoutDialog) {
        AlertDialog(icon = {
            Icon(
                imageVector = Icons.Default.Warning, contentDescription = null, tint = Color.Yellow
            )
        }, onDismissRequest = { showLogoutDialog = false }, confirmButton = {
            TextButton(onClick = {
                vm.dispatch(PersonIntent.Logout)
                showLogoutDialog = false
            }) {
                Text(text = "退出登录")
            }
        }, dismissButton = {
            TextButton(onClick = { showLogoutDialog = false }) {
                Text(text = "取消")
            }
        }, title = { Text(text = "提示") }, text = {
            Text(
                text = """
                    |点击退出登录继续
                    |建议您在退出账号后登录其他或此账号已获得完整的体验😉
                    |如果您希望继续以此账号操作请点击取消😊
                """.trimMargin()
            )
        })
    }

    LazyColumn(Modifier.mainBody()) {
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(2f)
                    .clickable { if (userInfoData == null) navController.navigate(AccountScreen.Login.route) },
            ) {
                Image(
                    painter = painter,
                    contentDescription = null,
                    Modifier
                        .fillMaxWidth()
                        .aspectRatio(2f),
                    contentScale = ContentScale.Crop
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(2f)
                        .background(Brush.verticalGradient(imageBoxColor)),
                )
                Column(
                    Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    AsyncImage(
                        model = "https://p.qqan.com/up/2020-12/16070652276806379.jpg",
                        contentDescription = null,
                        Modifier
                            .size(80.dp)
                            .clip(RoundedCornerShape(80.dp))
                    )
                    Text(
                        text = userInfoData?.userInfo?.username ?: "点击登录",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFE5E1E6)
                    )
                    userInfoData?.userInfo?.id?.let {
                        Text(
                            text = "ID:$it", color = Color(0xFFE5E1E6)
                        )
                    }
                    userInfoData?.coinInfo?.let { info ->
                        Row {
                            Text(text = "等级:${info.level}", color = Color(0xFFE5E1E6))
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(text = "排名${info.rank}", color = Color(0xFFE5E1E6))
                        }
                    }
                }
                IconButton(
                    modifier = Modifier.align(Alignment.TopEnd),
                    onClick = { navController.navigate(AccountScreen.Rank.route) }) {
                    Icon(
                        imageVector = Icons.Default.Menu,
                        contentDescription = null
                    )
                }
            }
        }
        item {
            SettingButton(imageVector = Icons.Default.Notifications, title = "我的消息", badge = {
                if (notificationNum > 0) {
                    BadgedBox(badge = {
                        Badge {
                            Text(notificationNum.toString())
                        }
                    }) {
                        Text(text = "我的消息")
                    }
                } else Text(text = "我的消息")
            }) {
                vmP.dispatch(PublicIntent.GetUnreadQuantity)
                navController.navigate(AccountScreen.Message.route)
            }
            Divider()
            SettingButton(imageVector = Icons.Default.Star, title = "我的积分", trailingContent = {
                userInfoData?.let { Text(text = it.coinInfo.coinCount.toString()) }
            }) {
                navController.navigate(AccountScreen.Coin.route)
            }
            Divider()
            SettingButton(imageVector = Icons.Default.Favorite, title = "我的收藏") {
                navController.navigate(AccountScreen.Collect.route)
            }
            Divider()
            SettingButton(imageVector = Icons.Default.Info, title = "关于") {}
            if (userInfoData != null) {
                Divider()
                SettingButton(
                    imageVector = Icons.Default.Close, title = "退出账号"
                ) {
                    showLogoutDialog = true
                }
            }

        }
    }
}