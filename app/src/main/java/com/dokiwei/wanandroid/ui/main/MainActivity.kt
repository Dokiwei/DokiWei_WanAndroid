package com.dokiwei.wanandroid.ui.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModelLazy
import androidx.lifecycle.ViewModelStore
import com.dokiwei.wanandroid.ui.screen.other.navcontroller.MainNavHost
import com.dokiwei.wanandroid.ui.theme.MainTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    companion object {
        var publicViewModelStore: ViewModelStore? = null
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val publicViewModel = ViewModelLazy(PublicViewModel::class, {viewModelStore }, {defaultViewModelProviderFactory}).value
        publicViewModelStore=viewModelStore
        setContent {
            MainTheme {
                MainNavHost()
            }
        }
    }
}