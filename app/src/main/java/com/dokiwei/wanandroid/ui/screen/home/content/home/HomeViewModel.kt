package com.dokiwei.wanandroid.ui.screen.home.content.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.dokiwei.wanandroid.data.repository.HomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

/**
 * @author DokiWei
 * @date 2023/7/14 22:37
 */
private const val TAG = "Home"

@HiltViewModel
class HomeViewModel @Inject constructor(
    homeRepository: HomeRepository
) : ViewModel() {

    private val _state = MutableStateFlow(
        HomeState(
            bannerData = homeRepository.getAllBanner().cachedIn(viewModelScope),
            articleData = homeRepository.getAllArticles().cachedIn(viewModelScope)
        )
    )
    val state = _state

}