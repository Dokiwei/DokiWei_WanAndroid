package com.dokiwei.wanandroid.ui.screens.home.content

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dokiwei.wanandroid.data.ArticleListData
import com.dokiwei.wanandroid.network.repository.QaRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

/**
 * @author DokiWei
 * @date 2023/7/14 23:15
 */
class QAContentViewModel : ViewModel() {
    private val qaRepo = QaRepo()

    //问答列表篇
    private val _qaList = MutableStateFlow(SnapshotStateList<ArticleListData>())
    val qaList = _qaList

    //刷新状态
    private val _isRefreshing = mutableStateOf(false)
    val isRefreshing = _isRefreshing

    //当前页面
    private val _nowPageIndex = MutableStateFlow(1)

    //初始化问答列表
    init {
        getQaList()
    }

    //刷新列表
    fun onRefresh() {
        _isRefreshing.value = true
        getQaList()
        _isRefreshing.value = false
    }

    //加载更多问答
    fun loadMore(): Boolean {
        if (_nowPageIndex.value < 40) {
            _nowPageIndex.value += 1
            getQaList(_nowPageIndex.value)
            return true
        }
        return false
    }

    //获取问答列表
    private fun getQaList(page: Int = 1) {
        viewModelScope.launch {
            val qaResult = qaRepo.getQa(page)
            if (qaResult.isSuccess) {
                if (page == 1) _qaList.value.clear()
                qaResult.getOrNull()?.let { list ->
                    list.forEach {
                        _qaList.value.add(it)
                    }
                }
            } else {
                Log.e("获取问答列表失败", qaResult.exceptionOrNull().toString())
            }
        }

    }
}