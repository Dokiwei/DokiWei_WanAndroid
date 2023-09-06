package com.dokiwei.wanandroid.ui.screen.navigation

import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dokiwei.wanandroid.model.apidata.ArticleData
import com.dokiwei.wanandroid.model.apidata.NaviData
import com.dokiwei.wanandroid.model.apidata.TreeData
import com.dokiwei.wanandroid.model.util.ToastAndLogcatUtil
import com.dokiwei.wanandroid.network.impl.NavigationApiImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

/**
 * @author DokiWei
 * @date 2023/7/31 18:39
 */
data class TreeState(
    val data: SnapshotStateList<TreeData> = SnapshotStateList(),
    val navi: SnapshotStateList<NaviData> = SnapshotStateList(),
    val children: SnapshotStateList<ArticleData> = SnapshotStateList(),
    val searchResult: SnapshotStateList<ArticleData> = SnapshotStateList(),
    val msg: String = "",
    val cid: Int = 0,
    val author: String = "",
    val isRefreshing: Boolean = false,
    val isLoadingMore: Boolean = false,
    val scrollToTop: Boolean = false,
    val nowTreeIndex: Int = 0,
    val nowSearchResultPageIndex: Int = 0,
    val nowTreeChildrenPageIndex: Int = 0
)

sealed class TreeIntent {
    object LoadMoreSearchResult : TreeIntent()
    object LoadMoreTreeChildren : TreeIntent()
    object RefreshSearchResult : TreeIntent()
    object RefreshTreeChildren : TreeIntent()
    data class UpdateTreeIndex(val index: Int) : TreeIntent()
    data class UpdateCid(val cid: Int) : TreeIntent()
    data class UpdateAuthor(val author: String) : TreeIntent()
    object GetTreeChildren : TreeIntent()
    object GetSearchResult : TreeIntent()
    object ChangeScrollToTop : TreeIntent()
}

sealed class TreeAction {
    data class ShowToast(val msg: String) : TreeAction()
    data class OutputLogcat(val tag: String = "TreeAction", val msg: String) :
        TreeAction()

    data class LoadMoreSearchResult(val author: String) : TreeAction()
    data class LoadMoreTreeChildren(val cid: Int) : TreeAction()
    data class RefreshSearchResult(val author: String) : TreeAction()
    data class RefreshTreeChildren(val cid: Int) : TreeAction()
    data class SetSearchResult(val page: Int, val data: List<ArticleData>?) : TreeAction()
    data class SetTreeChildren(val page: Int, val data: List<ArticleData>?) : TreeAction()
    data class SetTree(val data: List<TreeData>?) : TreeAction()
}


class TreeViewModel : ViewModel() {
    private val navigationApiImpl = NavigationApiImpl()

    private val _state = MutableStateFlow(TreeState())
    val state = _state

    init {
        viewModelScope.launch(Dispatchers.IO) {
            getTree()
            getNavi()
        }
    }

    fun dispatch(intent: TreeIntent) {
        when (intent) {
            is TreeIntent.RefreshSearchResult -> onRefresh(_state.value.author)
            is TreeIntent.RefreshTreeChildren -> onRefresh(_state.value.cid)
            is TreeIntent.LoadMoreSearchResult -> loadMore(_state.value.author)
            is TreeIntent.LoadMoreTreeChildren -> loadMore(_state.value.cid)
            is TreeIntent.UpdateTreeIndex -> _state.value =
                _state.value.copy(nowTreeIndex = intent.index)

            is TreeIntent.UpdateCid -> _state.value =
                _state.value.copy(cid = intent.cid)

            is TreeIntent.UpdateAuthor -> _state.value =
                _state.value.copy(author = intent.author)

            is TreeIntent.GetTreeChildren -> viewModelScope.launch {
                getTreeChildren(_state.value.nowTreeChildrenPageIndex, _state.value.cid)
                _state.value = _state.value.copy(nowSearchResultPageIndex = 0)
            }

            is TreeIntent.GetSearchResult -> viewModelScope.launch {
                getSearchResult(_state.value.nowSearchResultPageIndex, _state.value.author)
                _state.value = _state.value.copy(nowTreeChildrenPageIndex = 0)
            }

            is TreeIntent.ChangeScrollToTop -> _state.value =
                _state.value.copy(scrollToTop = !_state.value.scrollToTop)
        }
    }

    private fun handleAction(action: TreeAction) {
        when (action) {
            is TreeAction.RefreshSearchResult -> viewModelScope.launch {
                _state.value = _state.value.copy(isRefreshing = true)
                runBlocking(Dispatchers.IO) {
                    getSearchResult(author = action.author)
                }
                _state.value = _state.value.copy(isRefreshing = false)
            }

            is TreeAction.RefreshTreeChildren -> viewModelScope.launch {
                _state.value = _state.value.copy(isRefreshing = true)
                runBlocking(Dispatchers.IO) {
                    getTreeChildren(cid = action.cid)
                }
                _state.value = _state.value.copy(isRefreshing = false)
            }

            is TreeAction.LoadMoreSearchResult -> {
                _state.value =
                    _state.value.copy(nowSearchResultPageIndex = _state.value.nowSearchResultPageIndex + 1)
                viewModelScope.launch {
                    _state.value = _state.value.copy(isLoadingMore = true)
                    runBlocking(Dispatchers.IO) {
                        getSearchResult(_state.value.nowSearchResultPageIndex, action.author)
                    }
                    _state.value = _state.value.copy(isLoadingMore = false)
                }
            }

            is TreeAction.LoadMoreTreeChildren -> {
                _state.value =
                    _state.value.copy(nowTreeChildrenPageIndex = _state.value.nowTreeChildrenPageIndex + 1)
                viewModelScope.launch {
                    _state.value = _state.value.copy(isLoadingMore = true)
                    runBlocking(Dispatchers.IO) {
                        getTreeChildren(_state.value.nowTreeChildrenPageIndex, action.cid)
                    }
                    _state.value = _state.value.copy(isLoadingMore = false)
                }
            }

            is TreeAction.SetSearchResult -> {
                if (action.page == 0) _state.value.searchResult.clear()
                action.data?.let { list ->
                    list.forEach {
                        _state.value.searchResult.add(it)
                    }
                }
            }

            is TreeAction.SetTreeChildren -> {
                if (action.page == 0) _state.value.children.clear()
                action.data?.let { list ->
                    list.forEach {
                        _state.value.children.add(it)
                    }
                }
            }

            is TreeAction.SetTree -> {
                action.data?.let { list ->
                    list.forEach {
                        _state.value.data.add(it)
                    }
                }
            }

            is TreeAction.OutputLogcat -> ToastAndLogcatUtil.log(
                action.tag, action.msg
            )

            is TreeAction.ShowToast -> _state.value = _state.value.copy(msg = action.msg)
        }
    }

    private fun <T> onRefresh(query: T) {
        when (query) {
            is String -> handleAction(TreeAction.RefreshSearchResult(query))
            is Int -> handleAction(TreeAction.RefreshTreeChildren(query))
        }
    }

    private fun <T> loadMore(query: T) {
        when (query) {
            is String -> handleAction(TreeAction.LoadMoreSearchResult(query))
            is Int -> handleAction(TreeAction.LoadMoreTreeChildren(query))
        }
    }

    private suspend fun getTree() {
        val result = navigationApiImpl.getTree()
        if (result.isSuccess) handleAction(TreeAction.SetTree(result.getOrNull()))
        else handleAction(
            TreeAction.OutputLogcat(
                msg = "获取体系结构失败:${
                    result.exceptionOrNull().toString()
                }"
            )
        )
    }

    private suspend fun getTreeChildren(page: Int = 0, cid: Int) {
        val result = navigationApiImpl.getTreeChildren(page, cid)
        if (result.isSuccess) handleAction(TreeAction.SetTreeChildren(page, result.getOrNull()))
        else
            handleAction(
                TreeAction.OutputLogcat(
                    msg = "获取体系子文章失败:${
                        result.exceptionOrNull().toString()
                    }"
                )
            )
    }

    private suspend fun getSearchResult(page: Int = 0, author: String) {
        val result = navigationApiImpl.searchAuthor(page, author)
        if (result.isSuccess) handleAction(TreeAction.SetSearchResult(page, result.getOrNull()))
        else handleAction(
            TreeAction.OutputLogcat(
                msg = "获取搜索结果失败失败:${
                    result.exceptionOrNull().toString()
                }"
            )
        )
    }

    private suspend fun getNavi() {
        val result = navigationApiImpl.getNavigation()
        result.getOrNull()?.let { list ->
            list.forEach {
                _state.value.navi.add(it)
            }
        }
    }

}