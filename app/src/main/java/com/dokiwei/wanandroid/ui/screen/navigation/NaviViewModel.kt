package com.dokiwei.wanandroid.ui.screen.navigation

import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.dokiwei.wanandroid.data.base.BasePagingSource
import com.dokiwei.wanandroid.model.apidata.NaviData
import com.dokiwei.wanandroid.model.apidata.TreeData
import com.dokiwei.wanandroid.model.util.ToastAndLogcatUtil
import com.dokiwei.wanandroid.network.impl.NavigationApiImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

/**
 * @author DokiWei
 * @date 2023/7/31 18:39
 */
data class TreeState(
    val tree: SnapshotStateList<TreeData> = SnapshotStateList(),
    val navi: SnapshotStateList<NaviData> = SnapshotStateList(),
    val cid: Int = 0,
    val author: String = "",
    val nowTreeIndex: Int = 0
)

sealed class TreeIntent {
    data class UpdateTreeIndex(val index: Int) : TreeIntent()
    data class UpdateCid(val cid: Int) : TreeIntent()
    data class UpdateAuthor(val author: String) : TreeIntent()
}
class TreeViewModel : ViewModel() {
    private val navigationApiImpl = NavigationApiImpl()

    private val _state = MutableStateFlow(TreeState())
    val state = _state

    fun treeChildren(cid: Int) = Pager(config = PagingConfig(
        pageSize = 20, initialLoadSize = 40
    ), pagingSourceFactory = {
        BasePagingSource { navigationApiImpl.getTreeChildren(it, cid) }
    }).flow.cachedIn(viewModelScope)
    fun searchResult(author: String) = Pager(config = PagingConfig(
        pageSize = 20, initialLoadSize = 40
    ), pagingSourceFactory = {
        BasePagingSource { navigationApiImpl.searchAuthor(it, author) }
    }).flow.cachedIn(viewModelScope)

    init {
        viewModelScope.launch(Dispatchers.IO) {
            getTree()
            getNavi()
        }
    }

    fun dispatch(intent: TreeIntent) {
        when (intent) {
            is TreeIntent.UpdateTreeIndex -> _state.value =
                _state.value.copy(nowTreeIndex = intent.index)

            is TreeIntent.UpdateCid -> _state.value =
                _state.value.copy(cid = intent.cid)

            is TreeIntent.UpdateAuthor -> _state.value =
                _state.value.copy(author = intent.author)
        }
    }

    private suspend fun getTree() {
        val result = navigationApiImpl.getTree()
        result.getOrNull()?.let { list ->
            list.forEach {
                _state.value.tree.add(it)
            }
        }
        result.exceptionOrNull()?.let { ToastAndLogcatUtil.log("Navi-Tree","获取体系结构失败:$it") }
    }

    private suspend fun getNavi() {
        val result = navigationApiImpl.getNavigation()
        result.getOrNull()?.let { list ->
            list.forEach {
                _state.value.navi.add(it)
            }
        }
        result.exceptionOrNull()?.let { ToastAndLogcatUtil.log("Navi","获取导航失败:$it") }
    }

}