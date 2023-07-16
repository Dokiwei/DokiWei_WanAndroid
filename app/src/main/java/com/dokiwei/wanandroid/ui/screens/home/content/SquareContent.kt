package com.dokiwei.wanandroid.ui.screens.home.content

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.dokiwei.wanandroid.ui.component.ArticleListView
import com.dokiwei.wanandroid.ui.component.Loading
import com.dokiwei.wanandroid.ui.component.MySwipeRefresh
import com.dokiwei.wanandroid.util.ToastUtil
import java.net.URLEncoder

/**
 * @author DokiWei
 * @date 2023/7/14 22:35
 */
@Composable
fun SquareContent(
    navController: NavController,
    scrollToTop: Boolean
) {
    val vm: SquareContentViewModel = viewModel()
    val context = LocalContext.current
    val articleList by vm.userArticleList.collectAsState()
    val isRefreshing = vm.isRefreshing

    if (articleList.isEmpty()) Loading()

    MySwipeRefresh(
        scrollToTop = scrollToTop,
        isRefreshing = isRefreshing,
        onRefresh = { vm.onRefresh() },
        onLoadMore = {
            if (it > (articleList.size - 10))
                if (vm.loadMore()) ToastUtil.showMsg(context, "加载新内容...")
                else ToastUtil.showMsg(context, "到底啦!!")
        }
    ) {
        AnimatedVisibility(
            visible = articleList.isNotEmpty(),
            enter = slideInVertically(animationSpec = tween(800))
        ) {
            ArticleListView(
                lazyListState = it,
                articles = articleList,
                onArticleClick = {
                    val link = URLEncoder.encode(it.link, "UTF-8")
                    navController.navigate("网页/$link")
                },
                onLikeClick = { data, like ->
                    var callBack = false
                    if (like) {
                        vm.unlikeArticle(
                            data.id,
                            context
                        ) { success ->
                            callBack = success
                        }
                    } else {
                        vm.likeArticle(
                            data.id,
                            context
                        ) { success ->
                            callBack = success
                        }
                    }
                    callBack
                },
                banner = {}
            )
        }
    }

}