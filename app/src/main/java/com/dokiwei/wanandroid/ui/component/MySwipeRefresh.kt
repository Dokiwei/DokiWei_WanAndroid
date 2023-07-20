package com.dokiwei.wanandroid.ui.component

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

/**
 * @author DokiWei
 * @date 2023/7/16 22:41
 *
 * 自定义刷新框架
 *
 * @param isRefreshing:刷新状态
 * @param onRefresh:下拉刷新事件
 * @param onLoadMore:上拉加载事件
 * @param content:主体ui
 */
@Composable
fun MySwipeRefresh(
    modifier: Modifier = Modifier,
    scrollToTop: Boolean = false,
    isRefreshing: State<Boolean>,
    onRefresh: () -> Unit,
    onLoadMore: (Int) -> Unit,
    content: @Composable (LazyListState) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val lazyListState = rememberLazyListState()
    LaunchedEffect(scrollToTop) {
        if (scrollToTop) coroutineScope.launch { lazyListState.animateScrollToItem(0) }
    }
    LaunchedEffect(lazyListState) {
        snapshotFlow { lazyListState.isScrollInProgress }
            .filter { it }
            .map { lazyListState.firstVisibleItemIndex }
            .collectLatest { firstVisibleItemIndex ->
                onLoadMore(firstVisibleItemIndex)
            }
    }
    SwipeRefresh(
        modifier = modifier,
        state = rememberSwipeRefreshState(isRefreshing.value),
        onRefresh = onRefresh
    ) {
        content(lazyListState)
    }
}
