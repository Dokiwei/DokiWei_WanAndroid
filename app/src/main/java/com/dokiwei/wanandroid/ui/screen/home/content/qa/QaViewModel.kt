package com.dokiwei.wanandroid.ui.screen.home.content.qa

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.dokiwei.wanandroid.data.repository.QaRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * @author DokiWei
 * @date 2023/7/14 23:15
 */
@HiltViewModel
class QaViewModel @Inject constructor(
    qaRepository: QaRepository
) : ViewModel() {

    val data = qaRepository.getAllArticles().cachedIn(viewModelScope)
}