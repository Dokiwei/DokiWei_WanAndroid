package com.dokiwei.wanandroid.ui.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.dokiwei.wanandroid.network.repository.CollectRepo
import com.dokiwei.wanandroid.util.ToastAndLogcatUtil
import kotlinx.coroutines.launch

/**
 * @author DokiWei
 * @date 2023/7/30 19:57
 */

sealed class PublicIntent {
    data class Collect(val originId: Int) : PublicIntent()
    data class CollectCustom(val id: Int) : PublicIntent()
    data class UnCollect(val originId: Int) : PublicIntent()
    data class UnCollectCustom(val id: Int) : PublicIntent()
}

class PublicViewModel(application: Application) : AndroidViewModel(application = application) {
    private val collectRepo = CollectRepo()

    fun dispatch(intent: PublicIntent) {
        when (intent) {
            is PublicIntent.Collect -> collect(intent.originId)
            is PublicIntent.CollectCustom -> collectCustom(intent.id)
            is PublicIntent.UnCollect -> unCollect(intent.originId)
            is PublicIntent.UnCollectCustom -> unCollectCustom(intent.id)
        }
    }


    //收藏站内文章
    private fun collect(originId: Int) {
        viewModelScope.launch {
            val result = collectRepo.collect(originId)
            if (result.isFailure) {
                ToastAndLogcatUtil.showMsg("收藏失败")
                ToastAndLogcatUtil.log(
                    tag = "全局收藏", msg = "收藏操作异常:${
                        result.exceptionOrNull().toString()
                    }", level = 0
                )
            } else ToastAndLogcatUtil.showMsg("收藏成功")
        }
    }

    //取消收藏站内文章
    private fun unCollect(originId: Int) {
        viewModelScope.launch {
            val result = collectRepo.unCollect(originId)
            if (result.isFailure) {
                ToastAndLogcatUtil.showMsg("取消收藏失败")
                ToastAndLogcatUtil.log(
                    tag = "全局收藏", msg = "取消收藏操作异常:${
                        result.exceptionOrNull().toString()
                    }", level = 0
                )
            } else ToastAndLogcatUtil.showMsg("取消收藏成功")
        }
    }

    //取消收藏自定义文章
    private fun unCollectCustom(id: Int) {
        viewModelScope.launch {
            val result = collectRepo.unCollectCustom(id)
            if (result.isFailure) {
                ToastAndLogcatUtil.showMsg("取消收藏失败")
                ToastAndLogcatUtil.log(
                    tag = "全局收藏", msg = "取消收藏操作异常:${
                        result.exceptionOrNull().toString()
                    }", level = 0
                )
            } else ToastAndLogcatUtil.showMsg("取消收藏成功")
        }
    }

    //收藏自定义文章
    private fun collectCustom(id: Int) {
        viewModelScope.launch {
            val result = collectRepo.collectCustom(id)
            if (result.isFailure) {
                ToastAndLogcatUtil.showMsg("收藏失败")
                ToastAndLogcatUtil.log(
                    tag = "全局收藏", msg = "收藏操作异常:${
                        result.exceptionOrNull().toString()
                    }", level = 0
                )
            } else ToastAndLogcatUtil.showMsg("收藏成功")
        }
    }

}