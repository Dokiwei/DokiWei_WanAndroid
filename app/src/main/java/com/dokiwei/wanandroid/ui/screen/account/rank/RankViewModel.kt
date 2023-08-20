package com.dokiwei.wanandroid.ui.screen.account.rank

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dokiwei.wanandroid.model.apidata.CoinInfoData
import com.dokiwei.wanandroid.network.impl.AccountApiImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

/**
 * @author DokiWei
 * @date 2023/8/20 19:28
 */
data class RankDataState(
    val rank: List<CoinInfoData>? = null, val userInfo: CoinInfoData? = null
)

class RankViewModel : ViewModel() {
    private val accountApiImpl = AccountApiImpl()

    private val _rankData = MutableStateFlow(RankDataState())
    val rankData = _rankData

    init {
        viewModelScope.launch {
            getUserCoinInfo()
            getRank()
        }
    }

    private suspend fun getUserCoinInfo() {
        val result = accountApiImpl.getCoinInfo()
        result.getOrNull()?.let { _rankData.value = _rankData.value.copy(userInfo = it) }
    }

    private suspend fun getRank() {
        val result = accountApiImpl.getRank()
        result.getOrNull()?.let { _rankData.value = _rankData.value.copy(rank = it) }
    }
}