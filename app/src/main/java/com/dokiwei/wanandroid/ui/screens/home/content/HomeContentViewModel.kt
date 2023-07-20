package com.dokiwei.wanandroid.ui.screens.home.content

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dokiwei.wanandroid.data.ArticleListData
import com.dokiwei.wanandroid.data.BannerData
import com.dokiwei.wanandroid.network.repository.ArticleRepo
import com.dokiwei.wanandroid.network.repository.BannerRepo
import com.dokiwei.wanandroid.network.repository.CollectRepo
import com.dokiwei.wanandroid.util.ToastUtil
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

/**
 * @author DokiWei
 * @date 2023/7/14 22:37
 */
class HomeContentViewModel : ViewModel() {

    private val bannerRepo = BannerRepo()
    private val articleRepo = ArticleRepo()
    private val collectRepo = CollectRepo()

    //初始化应用数据
    init {
        getBanner()
        getArticleList()
    }

    //轮播图
    private val _bannerList = MutableStateFlow<List<BannerData>>(emptyList())
    val bannerList = _bannerList

    //文章列表
    private val _articleList = MutableStateFlow(SnapshotStateList<ArticleListData>())
    val articleList = _articleList

    //当前页
    private val _nowPageIndex = MutableStateFlow(0)
    private val _isRefreshing = mutableStateOf(false)
    val isRefreshing = _isRefreshing

    //刷新数据
    fun onRefresh() {
        _isRefreshing.value = true
        getBanner()
        getArticleList()
        _isRefreshing.value = false
    }

    fun loadMore(): Boolean {
        if (_nowPageIndex.value < 40) {
            _nowPageIndex.value += 1
            getArticleList(_nowPageIndex.value)
            return true
        }
        return false
    }


    //获取轮播图
    private fun getBanner() {
        viewModelScope.launch {
            val bannerResult = bannerRepo.banner()
            if (bannerResult.isSuccess) {
                _bannerList.value = bannerResult.getOrNull() ?: emptyList()
            } else {
                Log.e("获取banner失败", bannerResult.exceptionOrNull().toString())
            }
        }
    }

    //获取首页文章
    private fun getArticleList(page: Int = 0) {
        viewModelScope.launch {
            val articleResult = articleRepo.getArticleList(page)
            if (articleResult.isSuccess) {
                if (page == 0) _articleList.value.clear()
                articleResult.getOrNull()?.let { list ->
                    list.forEach {
                        _articleList.value.add(it)
                    }
                }
            } else {
                Log.e("获取首页文章失败", articleResult.exceptionOrNull().toString())
            }
        }
    }

    //收藏
    fun likeArticle(id: Int, context: Context, callBack: (Boolean) -> Unit) {
        viewModelScope.launch {
            val result = collectRepo.like(id)
            if (result.isFailure) {
                ToastUtil.showMsg(context, "收藏失败")
                callBack(false)
                Log.e("收藏操作异常", result.exceptionOrNull().toString())
            } else {
                ToastUtil.showMsg(context, "收藏成功")
                callBack(true)
            }
        }
    }

    //取消收藏
    fun unlikeArticle(id: Int, context: Context, callBack: (Boolean) -> Unit) {
        viewModelScope.launch {
            val result = collectRepo.unlike(id)
            if (result.isFailure) {
                ToastUtil.showMsg(context, "取消收藏失败")
                callBack(true)
                Log.e("取消收藏操作异常", result.exceptionOrNull().toString())
            } else {
                ToastUtil.showMsg(context, "取消收藏成功")
                callBack(false)
            }
        }
    }
}