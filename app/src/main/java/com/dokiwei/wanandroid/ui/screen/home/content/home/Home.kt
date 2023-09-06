package com.dokiwei.wanandroid.ui.screen.home.content.home

import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.dokiwei.wanandroid.model.util.ToastAndLogcatUtil
import com.dokiwei.wanandroid.model.util.publicViewModel
import com.dokiwei.wanandroid.ui.widgets.Items
import com.dokiwei.wanandroid.ui.widgets.Loading
import com.dokiwei.wanandroid.ui.widgets.SwipeLayout

/**
 * @author DokiWei
 * @date 2023/7/14 22:35
 */
@Composable
fun HomeContent(
    navController: NavController, scrollToTop: Boolean
) {
    val vm = hiltViewModel<HomeViewModel>()
    val vmP = publicViewModel()

    val state by vm.state.collectAsState()
    val bannerData = state.bannerData.collectAsLazyPagingItems()
    val articleData = state.articleData.collectAsLazyPagingItems()

    val lazyListState = rememberLazyListState()

    when (articleData.loadState.refresh) {
        is LoadState.Loading -> Loading {
            bannerData.retry()
            articleData.retry()
        }

        is LoadState.Error -> (articleData.loadState.refresh as LoadState.Error).error.message?.let {
            ToastAndLogcatUtil.log(
                "HomePaging", msg = it
            )
        }

        is LoadState.NotLoading -> {}
    }

    LaunchedEffect(scrollToTop) {
        lazyListState.animateScrollToItem(0)
    }

    SwipeLayout(onRefresh = {
        bannerData.refresh()
        articleData.refresh()
    }) {
        Items(lazyListState, bannerData, navController, articleData, vmP)
    }


}


