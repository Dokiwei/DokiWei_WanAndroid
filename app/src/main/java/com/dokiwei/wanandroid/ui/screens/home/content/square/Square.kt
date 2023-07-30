package com.dokiwei.wanandroid.ui.screens.home.content.square

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.dokiwei.wanandroid.ui.component.Loading
import com.dokiwei.wanandroid.ui.component.SwipeHomeItemsLayout
import com.dokiwei.wanandroid.ui.main.PublicIntent
import com.dokiwei.wanandroid.ui.main.PublicViewModel
import com.dokiwei.wanandroid.util.ToastAndLogcatUtil

/**
 * @author DokiWei
 * @date 2023/7/14 22:35
 */
@Composable
fun SquareContent(
    navController: NavController, scrollToTop: Boolean
) {
    val vm: SquareViewModel = viewModel()
    val vmP: PublicViewModel = viewModel()
    val state by vm.state.collectAsState()
    LaunchedEffect(state.msg) {
        if (state.msg.isNotEmpty()) ToastAndLogcatUtil.showMsg(state.msg)
    }
    if (state.userArticleList.isEmpty()) Loading(onClick = { vm.dispatch(SquareIntent.Refresh) })

    SwipeHomeItemsLayout(
        navController = navController,
        isRefreshing = state.isRefreshing,
        isLoadingMore = state.isLoadingMore,
        isToTop = scrollToTop,
        items = state.userArticleList,
        onRefresh = { vm.dispatch(SquareIntent.Refresh) },
        onLoadMore = { vm.dispatch(SquareIntent.LoadMore) },
        onCollectClick = { item, like ->
            if (like) vmP.dispatch(PublicIntent.UnCollect(item.id))
            else vmP.dispatch(PublicIntent.Collect(item.id))
        }
    )

}