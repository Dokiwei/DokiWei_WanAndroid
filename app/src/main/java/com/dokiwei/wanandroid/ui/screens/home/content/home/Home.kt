package com.dokiwei.wanandroid.ui.screens.home.content.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.dokiwei.wanandroid.ui.component.Banner
import com.dokiwei.wanandroid.ui.component.Loading
import com.dokiwei.wanandroid.ui.component.SwipeHomeItemsLayout
import com.dokiwei.wanandroid.ui.main.PublicIntent
import com.dokiwei.wanandroid.ui.main.PublicViewModel
import com.dokiwei.wanandroid.util.ToastAndLogcatUtil
import java.net.URLEncoder

/**
 * @author DokiWei
 * @date 2023/7/14 22:35
 */
@Composable
fun HomeContent(
    navController: NavController, scrollToTop: Boolean
) {
    val vm: HomeViewModel = viewModel()
    val vmP: PublicViewModel = viewModel()
    val state by vm.state.collectAsState()
    LaunchedEffect(state.msg) {
        if (state.msg.isNotEmpty()) ToastAndLogcatUtil.showMsg(state.msg)
    }
    if (state.bannerList.isEmpty() && state.articleList.isEmpty()) Loading(onClick = {
        vm.dispatch(
            HomeIntent.Refresh
        )
    })


    SwipeHomeItemsLayout(
        navController = navController,
        isRefreshing = state.isRefreshing,
        isLoadingMore = state.isLoadingMore,
        isToTop = scrollToTop,
        items = state.articleList,
        onRefresh = { vm.dispatch(HomeIntent.Refresh) },
        onLoadMore = { vm.dispatch(HomeIntent.LoadMore) },
        onCollectClick = { item, like ->
            if (like) vmP.dispatch(PublicIntent.UnCollect(item.id))
            else vmP.dispatch(PublicIntent.Collect(item.id))
        }
    ) {
        Banner(
            pageCount = state.bannerList.size,
            bannerData = state.bannerList,
            onClick = {
                val link = URLEncoder.encode(it.url, "UTF-8")
                navController.navigate("网页/$link")
            })
    }


}
