package com.dokiwei.wanandroid.ui.screens.person.mylike

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dokiwei.wanandroid.data.MyLikeData
import com.dokiwei.wanandroid.network.repository.CollectRepo
import com.dokiwei.wanandroid.util.ToastUtil
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

/**
 * @author DokiWei
 * @date 2023/7/19 21:08
 */
class MyLikeViewModel : ViewModel() {
    private val collectRepo = CollectRepo()

    //初始化
    init {
        getMyLike()
    }

    private val _nowPageIndex = MutableStateFlow(0)

    private val _isRefresh = mutableStateOf(false)
    val isRefresh = _isRefresh

    //我的收藏
    private val _myLikeItems = MutableStateFlow(SnapshotStateList<MyLikeData>())
    val myLikeItems = _myLikeItems

    //刷新数据
    fun onRefresh(){
        _isRefresh.value=true
        getMyLike()
        _isRefresh.value=false
    }

    fun loadMore(){
        _nowPageIndex.value += 1
        getMyLike(_nowPageIndex.value)
    }

    //获取我的收藏
    private fun getMyLike(page: Int = 0) {
        viewModelScope.launch {
            val result = collectRepo.myLike(page)
            if (result.isSuccess) {
                if (page == 0) _myLikeItems.value.clear()
                result.getOrNull()?.let {
                    it.forEach { data ->
                        _myLikeItems.value.add(data)
                    }
                }
            } else {
                Log.e("获取我的收藏失败", result.exceptionOrNull().toString())
            }
        }
    }

    fun unlike(id:Int,context: Context){
        viewModelScope.launch {
            val result = collectRepo.unlike(id)
            if (result.isFailure) {
                ToastUtil.showMsg(context, "取消收藏失败")
                Log.e("收藏操作异常", result.exceptionOrNull().toString())
            } else {
                ToastUtil.showMsg(context, "取消收藏成功")
            }
        }
    }


    fun unlikeCustom(id:Int,context: Context){
        viewModelScope.launch {
            val result = collectRepo.unlikeCustom(id)
            if (result.isFailure) {
                ToastUtil.showMsg(context, "取消收藏失败")
                Log.e("收藏操作异常", result.exceptionOrNull().toString())
            } else {
                ToastUtil.showMsg(context, "取消收藏成功")
            }
        }
    }

    fun like(id: Int, context: Context) {
        viewModelScope.launch {
            val result = collectRepo.like(id)
            if (result.isFailure) {
                ToastUtil.showMsg(context, "收藏失败")
                Log.e("收藏操作异常", result.exceptionOrNull().toString())
            } else {
                ToastUtil.showMsg(context, "收藏成功")
            }
        }
    }

    fun likeCustom(id: Int, context: Context) {
        viewModelScope.launch {
            val result = collectRepo.likeCustom(id)
            if (result.isFailure) {
                ToastUtil.showMsg(context, "收藏失败")
                Log.e("收藏操作异常", result.exceptionOrNull().toString())
            } else {
                ToastUtil.showMsg(context, "收藏成功")
            }
        }
    }

}