package com.dokiwei.wanandroid.ui.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.dokiwei.wanandroid.model.util.ToastAndLogcatUtil
import com.dokiwei.wanandroid.network.impl.AccountApiImpl
import com.dokiwei.wanandroid.network.impl.CollectApiImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

/**
 * @author DokiWei
 * @date 2023/7/30 19:57
 */

sealed class PublicIntent {
    data class Collect(val originId: Int) : PublicIntent()
    data class UnCollect(val originId: Int) : PublicIntent()
    object AlwaysGetUnreadQuantity : PublicIntent()
    object GetUnreadQuantity : PublicIntent()
}

class PublicViewModel(application: Application) : AndroidViewModel(application = application) {
    private val collectApiImpl = CollectApiImpl()
    private val messageApiImpl = AccountApiImpl()

    private val _unreadQuantity = MutableStateFlow(-1)
    val unreadQuantity = _unreadQuantity

    fun dispatch(intent: PublicIntent) {
        when (intent) {
            is PublicIntent.Collect -> collect(intent.originId)
            is PublicIntent.UnCollect -> unCollect(intent.originId)
            is PublicIntent.AlwaysGetUnreadQuantity -> alwaysGetUnreadQuantity()
            is PublicIntent.GetUnreadQuantity -> viewModelScope.launch(Dispatchers.IO) { getUnreadQuantity() }
        }
    }

    private fun alwaysGetUnreadQuantity() {
        viewModelScope.launch {
            while (true) {
                getUnreadQuantity()
                delay(60000)
            }
        }
    }

    //获取未读消息数量
    private suspend fun getUnreadQuantity() {
        val result = messageApiImpl.getUnreadQuantity()
        result.getOrNull()?.let {
            _unreadQuantity.value = it
        }
        result.exceptionOrNull()?.let {
            ToastAndLogcatUtil.log(
                tag = "publicViewModel获取未读消息数量", msg = "获取异常:$it"
            )
        }
    }

    //收藏站内文章
    private fun collect(originId: Int) {
        viewModelScope.launch {
            val result = collectApiImpl.collect(originId)
            result.getOrNull()?.let {
                when (it) {
                    -1001 -> ToastAndLogcatUtil.showMsg("请先登录,再进行操作")
                    0 -> ToastAndLogcatUtil.showMsg("收藏成功")
                }
            }
            result.exceptionOrNull()?.let {
                ToastAndLogcatUtil.log(
                    msg = "收藏失败:${
                        result.exceptionOrNull().toString()
                    }"
                )
            }
        }
    }

    //取消收藏站内文章
    private fun unCollect(originId: Int) {
        viewModelScope.launch {
            val result = collectApiImpl.unCollect(originId)
            result.getOrNull()?.let {
                when (it) {
                    -1001 -> ToastAndLogcatUtil.showMsg("请先登录,再进行操作")
                    0 -> ToastAndLogcatUtil.showMsg("取消收藏成功")
                }
            }
            result.exceptionOrNull()?.let {
                ToastAndLogcatUtil.log(
                    msg = "取消收藏失败:${
                        result.exceptionOrNull().toString()
                    }"
                )
            }
        }
    }

}