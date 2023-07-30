package com.dokiwei.wanandroid.ui.screens.home.content.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dokiwei.wanandroid.network.repository.ArticleRepo
import com.dokiwei.wanandroid.network.repository.BannerRepo
import com.dokiwei.wanandroid.util.ToastAndLogcatUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

/**
 * @author DokiWei
 * @date 2023/7/14 22:37
 */
class HomeViewModel : ViewModel() {

    private val bannerRepo = BannerRepo()
    private val articleRepo = ArticleRepo()

    //初始化应用数据
    init {
        viewModelScope.launch {
            getBanner()
            getArticleList()
        }
    }

    private val _state = MutableStateFlow(HomeState())
    val state = _state

    fun dispatch(intent: HomeIntent) {
        when (intent) {
            is HomeIntent.Refresh -> onRefresh()
            is HomeIntent.LoadMore -> loadMore()
            is HomeIntent.GetArticleData -> viewModelScope.launch { getArticleList(intent.page) }
        }
    }

    private fun handleAction(action: HomeAction) {
        when (action) {
            is HomeAction.OutputLogcat -> ToastAndLogcatUtil.log(
                action.tag, action.msg, action.level
            )

            is HomeAction.ShowToast -> _state.value = _state.value.copy(msg = action.msg)

            is HomeAction.SetArticleData -> {
                if (action.page == 0) _state.value.articleList.clear()
                action.dataList?.let { list ->
                    list.forEach {
                        _state.value.articleList.add(it)
                    }
                }
            }

            is HomeAction.SetBannerData -> _state.value =
                _state.value.copy(bannerList = action.dataList)

            is HomeAction.LoadMore -> {
                _state.value = _state.value.copy(nowPageIndex = _state.value.nowPageIndex + 1)
                viewModelScope.launch(Dispatchers.IO) {
                    _state.value = _state.value.copy(isLoadingMore = true)
                    runBlocking(Dispatchers.IO) {
                        getArticleList(_state.value.nowPageIndex)
                    }
                    _state.value = _state.value.copy(isLoadingMore = false)
                }
            }

            is HomeAction.Refresh -> {
                viewModelScope.launch(Dispatchers.IO) {
                    _state.value = _state.value.copy(isRefreshing = true)
                    runBlocking(Dispatchers.IO) {
                        getBanner()
                        getArticleList()
                    }
                    _state.value = _state.value.copy(isRefreshing = false)
                }
            }
        }
    }

    //刷新数据
    private fun onRefresh() {
        handleAction(HomeAction.Refresh)
    }

    private fun loadMore() {
        handleAction(HomeAction.LoadMore)
//        handleAction(HomeAction.ShowToast("加载新内容"))
    }


    //获取轮播图
    private suspend fun getBanner() {
        viewModelScope.launch {
            val result = bannerRepo.banner()
            if (result.isSuccess) handleAction(
                HomeAction.SetBannerData(
                    result.getOrNull() ?: emptyList()
                )
            )
            else handleAction(
                HomeAction.OutputLogcat(
                    msg = "获取banner失败:${
                        result.exceptionOrNull().toString()
                    }"
                )
            )
        }
    }

    //获取首页文章
    private suspend fun getArticleList(page: Int = 0) {
        viewModelScope.launch {
            val result = articleRepo.getArticleList(page)
            if (result.isSuccess) handleAction(
                HomeAction.SetArticleData(
                    page, result.getOrNull()
                )
            )
            else handleAction(
                HomeAction.OutputLogcat(
                    msg = result.exceptionOrNull().toString()
                )
            )
        }
    }

}