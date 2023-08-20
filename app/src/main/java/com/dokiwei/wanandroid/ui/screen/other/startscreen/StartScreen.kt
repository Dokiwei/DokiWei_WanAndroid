package com.dokiwei.wanandroid.ui.screen.other.startscreen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.dokiwei.wanandroid.model.util.HomeScreen
import com.dokiwei.wanandroid.model.util.mainBody
import kotlinx.coroutines.delay

/**
 * @author DokiWei
 * @date 2023/7/7 17:07
 */
@Composable
fun StartScreen(navController: NavHostController) {
    var titleVisible by remember {
        mutableStateOf(false)
    }
    LaunchedEffect(Unit) {
        delay(1000)
        titleVisible = true
        delay(500)
        navController.navigate(HomeScreen.Main.route)
    }
    Column(
        Modifier.mainBody(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AnimatedVisibility(
            visible = titleVisible,
            enter = expandVertically(expandFrom = Alignment.Bottom) + fadeIn(
                initialAlpha = 0.1f
            )
        ) {
            Text(
                text = "WanAndroid",
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
