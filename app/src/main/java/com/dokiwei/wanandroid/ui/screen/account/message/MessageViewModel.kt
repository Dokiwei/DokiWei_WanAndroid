package com.dokiwei.wanandroid.ui.screen.account.message

import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dokiwei.wanandroid.model.apidata.MessageData
import com.dokiwei.wanandroid.model.util.ToastAndLogcatUtil
import com.dokiwei.wanandroid.network.impl.AccountApiImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

/**
 * @author DokiWei
 * @date 2023/8/6 17:00
 */
data class MessageState(
    val data: MessageViewModelData = MessageViewModelData(
        readList = SnapshotStateList(),
        unReadList = SnapshotStateList()
    ),
    val selectedTabIndex: Int = 0,
)

data class MessageViewModelData(
    val readList: SnapshotStateList<MessageData>,
    val unReadList: SnapshotStateList<MessageData>,
)

sealed class MessageIntent {
    data class OnTabSelected(val index: Int) : MessageIntent()
}


class MessageViewModel : ViewModel() {
    private val messageApiImpl = AccountApiImpl()

    private val _state = MutableStateFlow(MessageState())
    val state = _state

    init {
        viewModelScope.launch {
            getUnRead()
        }
    }

    fun dispatch(intent: MessageIntent) {
        when (intent) {
            is MessageIntent.OnTabSelected -> {
                _state.value = _state.value.copy(selectedTabIndex = intent.index)
                when (intent.index){
                    0->viewModelScope.launch { getUnRead() }
                    1->viewModelScope.launch { getRead() }
                }
            }

            else -> {}
        }
    }

    private suspend fun getRead(page: Int = 1) {
        val result = messageApiImpl.getRead(page)
        val list:SnapshotStateList<MessageData> =SnapshotStateList()
        result.getOrNull()?.forEach {
            list.add(it)
        }
        _state.value=_state.value.copy(data = _state.value.data.copy(readList = list))
        result.exceptionOrNull()?.let { ToastAndLogcatUtil.log(msg = it.toString()) }
    }

    private suspend fun getUnRead(page: Int = 1) {
        val result = messageApiImpl.getUnread(page)
        val list:SnapshotStateList<MessageData> = SnapshotStateList()
        result.getOrNull()?.forEach {
            list.add(it)
        }
        _state.value=_state.value.copy(data = _state.value.data.copy(unReadList = list))
        result.exceptionOrNull()?.let { ToastAndLogcatUtil.log(msg = it.toString()) }
    }
}