package com.dokiwei.wanandroid.ui.screen.project

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.cachedIn
import com.dokiwei.wanandroid.data.local.database.ArticleDatabase
import com.dokiwei.wanandroid.data.paging.ProjectRemoteMediator
import com.dokiwei.wanandroid.data.repository.ProjectRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @author DokiWei
 * @date 2023/7/15 16:18
 */
@HiltViewModel
class ProjectViewModel @Inject constructor(
    projectRepository: ProjectRepository, db: ArticleDatabase
) : ViewModel() {

    private val remoteMediator = ProjectRemoteMediator(db)

    @OptIn(ExperimentalPagingApi::class)
    private val _state = MutableStateFlow(
        ProjectState(
            title = projectRepository.getAllTitle().cachedIn(viewModelScope),
            data = projectRepository.getAllProject(remoteMediator).cachedIn(viewModelScope)
        )
    )
    val state = _state

    fun dispatch(intent: ProjectIntent) {
        when (intent) {
            is ProjectIntent.SelectedTab -> _state.value =
                _state.value.copy(selectedTabIndex = intent.index)


            is ProjectIntent.UpdateCid -> viewModelScope.launch {
                remoteMediator.updateCid(intent.cid)
            }

            is ProjectIntent.UpdateTabExpanded -> _state.value =
                _state.value.copy(isTabExpand = intent.boolean)

            is ProjectIntent.UpdateScrollToTop -> _state.value =
                _state.value.copy(scrollToTop = intent.boolean)
        }
    }

}