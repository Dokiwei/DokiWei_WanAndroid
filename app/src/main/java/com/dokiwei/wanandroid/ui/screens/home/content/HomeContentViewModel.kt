package com.dokiwei.wanandroid.ui.screens.home.content

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dokiwei.wanandroid.data.ArticleListData
import com.dokiwei.wanandroid.data.BannerData
import com.dokiwei.wanandroid.network.repository.ArticleListRepo
import com.dokiwei.wanandroid.network.repository.BannerRepo
import com.dokiwei.wanandroid.network.repository.CollectArticleRepo
import com.dokiwei.wanandroid.util.ToastUtil
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

/**
 * @author DokiWei
 * @date 2023/7/14 22:37
 */
class HomeContentViewModel : ViewModel() {

    private val bannerRepo = BannerRepo()
    private val articleListRepo = ArticleListRepo()
    private val collectArticleRepo = CollectArticleRepo()

    //初始化应用数据
    init {
        viewModelScope.launch {
            getBanner()
            getArticleList()
        }
    }

    //轮播图
    private val _bannerList = MutableStateFlow<List<BannerData>>(emptyList())
    val bannerList = _bannerList

    //文章列表
    private val _articleList = MutableStateFlow<List<ArticleListData>>(emptyList())
    val articleList = _articleList

    //获取轮播图
    private suspend fun getBanner() {
        val bannerResult = bannerRepo.banner()
        if (bannerResult.isSuccess) {
            _bannerList.value = bannerResult.getOrNull() ?: emptyList()
        } else {
            Log.e("获取banner失败", bannerResult.exceptionOrNull().toString())
        }
    }

    //获取首页文章
    private suspend fun getArticleList() {
        val articleResult = articleListRepo.getArticleList(0)
        if (articleResult.isSuccess) {
            _articleList.value = articleResult.getOrNull() ?: emptyList()
        } else {
            Log.e("获取首页文章失败", articleResult.exceptionOrNull().toString())
        }
    }

    //收藏
    fun likeArticle(id: Int, context: Context, callBack: (Boolean) -> Unit) {
        viewModelScope.launch {
            val result = collectArticleRepo.likeArticle(id)
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
            val result = collectArticleRepo.unLikeArticle(id)
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