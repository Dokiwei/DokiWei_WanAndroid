package com.dokiwei.wanandroid.ui.screen.home

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableIntStateOf
import androidx.lifecycle.ViewModel

/**
 * @author DokiWei
 * @date 2023/7/9 23:55
 */
class HomeViewModel : ViewModel() {


    //tabRow数据
    private val _selectedTabIndex = mutableIntStateOf(1)
    val selectedTabIndex: State<Int> = _selectedTabIndex
    fun onTabSelected(index: Int) {
        _selectedTabIndex.intValue = index
    }
}