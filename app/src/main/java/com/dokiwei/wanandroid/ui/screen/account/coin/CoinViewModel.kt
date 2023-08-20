package com.dokiwei.wanandroid.ui.screen.account.coin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dokiwei.wanandroid.model.apidata.CoinCountData
import com.dokiwei.wanandroid.network.impl.AccountApiImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

/**
 * @author DokiWei
 * @date 2023/8/20 17:35
 */
class CoinViewModel : ViewModel() {
    private val accountApiImpl = AccountApiImpl()

    private val _coinList : MutableStateFlow<List<CoinCountData>?> =MutableStateFlow(null)
    val coinList = _coinList

    init {
        viewModelScope.launch {
            getCoinList()
        }
    }

    private suspend fun getCoinList(page: Int = 1) {
        val result = accountApiImpl.getCoinList(page)
        result.getOrNull()?.let {
            _coinList.value=it
        }
    }
}