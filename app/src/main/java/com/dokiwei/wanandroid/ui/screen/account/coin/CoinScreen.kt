package com.dokiwei.wanandroid.ui.screen.account.coin

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.dokiwei.wanandroid.model.util.AccountScreen
import com.dokiwei.wanandroid.model.util.cardContent

/**
 * @author DokiWei
 * @date 2023/8/20 17:34
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CoinScreen(navController: NavController) {
    val vm: CoinViewModel = viewModel()

    val coinList by vm.coinList.collectAsState()

    Scaffold(topBar = {
        CenterAlignedTopAppBar(title = { Text(text = AccountScreen.Coin.route) }, navigationIcon = {
            IconButton(onClick = {
                navController.navigateUp()
            }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack, contentDescription = "返回上一页"
                )
            }
        })
    }) { innerPadding ->
        coinList?.let {
            AnimatedVisibility(visible = it.isNotEmpty(), enter = fadeIn(), exit = fadeOut()) {
                LazyColumn(Modifier.padding(innerPadding)) {
                    items(it.size) {index->
                        val item = it[index]
                        Card(
                            modifier = Modifier.cardContent(),
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.cardColors(),
                            elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
                        ) {
                            ListItem(headlineContent = { Text(text = item.reason) }, trailingContent = {
                                Text(
                                    text = "+${item.coinCount}",
                                    fontWeight = FontWeight.Bold
                                )
                            }, overlineContent = { Text(text = item.desc) })
                        }
                    }
                }
            }
        }
    }
}