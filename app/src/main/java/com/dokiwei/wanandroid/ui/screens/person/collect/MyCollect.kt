package com.dokiwei.wanandroid.ui.screens.person.collect

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.dokiwei.wanandroid.ui.component.SwipeCollectsItemsLayout
import com.dokiwei.wanandroid.ui.main.PublicIntent
import com.dokiwei.wanandroid.ui.main.PublicViewModel

/**
 * @author DokiWei
 * @date 2023/7/19 21:08
 */
@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyLike(navController: NavController) {
    val vm: MyCollectViewModel = viewModel()
    val vmP: PublicViewModel = viewModel()

    val state by vm.state.collectAsState()

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
                    vm.dispatch(MyCollectIntent.UpdateScrollToTop(!state.scrollToTop))
                }
            ) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowUp,
                    contentDescription = "Scroll to top"
                )
            }
        }
    ) { innerPadding ->

        Box(modifier = Modifier.padding(innerPadding)) {
            SwipeCollectsItemsLayout(
                navController = navController,
                isRefreshing = state.isRefreshing,
                isLoadingMore = state.isLoadingMore,
                isToTop = state.scrollToTop,
                items = state.myCollectList,
                onRefresh = { vm.dispatch(MyCollectIntent.Refresh) },
                onLoadMore = { vm.dispatch(MyCollectIntent.LoadMore) },
                onCollectClick = { item, like ->
                    if (like) vmP.dispatch(PublicIntent.UnCollect(item.originId))
                    else vmP.dispatch(PublicIntent.Collect(item.originId))
                }
            )
        }
    }

}
