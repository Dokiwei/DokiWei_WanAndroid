package com.dokiwei.wanandroid.ui.screen.home.content.square

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.dokiwei.wanandroid.data.repository.SquareRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * @author DokiWei
 * @date 2023/7/14 22:50
 */
@HiltViewModel
class SquareViewModel @Inject constructor(
    squareRepository: SquareRepository
) : ViewModel() {

    val data = squareRepository.getAllArticles().cachedIn(viewModelScope)

}