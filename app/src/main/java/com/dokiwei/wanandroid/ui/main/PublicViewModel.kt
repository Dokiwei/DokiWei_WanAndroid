package com.dokiwei.wanandroid.ui.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.dokiwei.wanandroid.network.impl.CollectApiImpl
import com.dokiwei.wanandroid.util.ToastAndLogcatUtil
import kotlinx.coroutines.launch

/**
 * @author DokiWei
 * @date 2023/7/30 19:57
 */

sealed class PublicIntent {
    data class Collect(val originId: Int) : PublicIntent()
    data class UnCollect(val originId: Int) : PublicIntent()
}

class PublicViewModel(application: Application) : AndroidViewModel(application = application) {
    private val collectApiImpl = CollectApiImpl()

    fun dispatch(intent: PublicIntent) {
        when (intent) {
            is PublicIntent.Collect -> collect(intent.originId)
            is PublicIntent.UnCollect -> unCollect(intent.originId)
        }
    }


    //收藏站内文章
    private fun collect(originId: Int) {
        viewModelScope.launch {
            val result = collectApiImpl.collect(originId)
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
            val result = collectApiImpl.unCollect(originId)
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

}