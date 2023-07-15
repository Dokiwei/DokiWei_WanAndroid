package com.dokiwei.wanandroid.ui.screens.home.content

import android.util.Log
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

    private val _qaList = MutableStateFlow<List<ArticleListData>>(emptyList())
    val qaList=_qaList

    init {
        viewModelScope.launch {
            getQaList()
        }
    }
    private suspend fun getQaList(){
        val qaResult = qaRepo.getQa(1)
        if (qaResult.isSuccess) {
            _qaList.value = qaResult.getOrNull() ?: emptyList()
        } else {
            Log.e("获取问答列表失败", qaResult.exceptionOrNull().toString())
        }
    }
}