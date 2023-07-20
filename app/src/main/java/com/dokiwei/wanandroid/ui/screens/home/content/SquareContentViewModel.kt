package com.dokiwei.wanandroid.ui.screens.home.content

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dokiwei.wanandroid.data.ArticleListData
import com.dokiwei.wanandroid.network.repository.CollectRepo
import com.dokiwei.wanandroid.network.repository.UserArticleRepo
import com.dokiwei.wanandroid.util.ToastUtil
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

/**
 * @author DokiWei
 * @date 2023/7/14 22:50
 */
class SquareContentViewModel : ViewModel() {
    private val userArticleRepo = UserArticleRepo()
    private val collectRepo = CollectRepo()

    //初始化
    init {
        getUserArticleList()
    }

    //文章列表
    private val _userArticleList = MutableStateFlow(SnapshotStateList<ArticleListData>())
    val userArticleList = _userArticleList

    //刷新状态
    private val _isRefreshing = mutableStateOf(false)
    val isRefreshing = _isRefreshing

    //当前页面
    private val _nowPageIndex = MutableStateFlow(0)

    //刷新列表
    fun onRefresh() {
        _isRefreshing.value = true
        getUserArticleList()
        _isRefreshing.value = false
    }

    //加载更多问答
    fun loadMore(): Boolean {
        if (_nowPageIndex.value < 40) {
            _nowPageIndex.value += 1
            getUserArticleList(_nowPageIndex.value)
            return true
        }
        return false
    }

    //获取广场文章
    private fun getUserArticleList(page: Int = 0) {
        viewModelScope.launch {
            val articleResult = userArticleRepo.getArticleList(page)
            if (articleResult.isSuccess) {
                if (page == 0) _userArticleList.value.clear()
                articleResult.getOrNull()?.let {list->
                    list.forEach{
                        _userArticleList.value.add(it)
                    }
                }
            } else {
                Log.e("获取广场文章失败", articleResult.exceptionOrNull().toString())
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