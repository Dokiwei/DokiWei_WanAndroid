package com.dokiwei.wanandroid.ui.screen.home.content.qa

import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
 * @date 2023/7/14 22:36
 */
@Composable
fun QAContent(
    navController: NavController, scrollToTop: Boolean
) {
    val vm: QaViewModel = hiltViewModel()
    val vmP = publicViewModel()

    val data = vm.data.collectAsLazyPagingItems()

    val lazyListState = rememberLazyListState()

    LaunchedEffect(scrollToTop) {
        lazyListState.animateScrollToItem(0)
    }

    when (data.loadState.refresh) {
        is LoadState.Loading -> Loading {
            data.retry()
        }

        is LoadState.Error -> (data.loadState.refresh as LoadState.Error).error.message?.let {
            ToastAndLogcatUtil.log(
                "QaPaging", msg = it
            )
        }

        is LoadState.NotLoading -> {}
    }

    SwipeLayout(onRefresh = { data.refresh() }) {
        Items(
            lazyListState = lazyListState,
            navController = navController,
            articleData = data,
            vmP = vmP
        )
    }

}