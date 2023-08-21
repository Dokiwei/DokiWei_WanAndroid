package com.dokiwei.wanandroid.ui.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModelLazy
import androidx.lifecycle.ViewModelStore
import com.dokiwei.wanandroid.ui.screen.other.navcontroller.MainNavHost
import com.dokiwei.wanandroid.ui.theme.MainTheme
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.statusBarsHeight
import com.google.accompanist.insets.ui.Scaffold
import com.google.accompanist.systemuicontroller.rememberSystemUiController
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
                ProvideWindowInsets {
                    rememberSystemUiController().setStatusBarColor(
                        Color.Transparent, darkIcons = MaterialTheme.colors.isLight
                    )

                    Surface(color = MaterialTheme.colors.background) {
                        Scaffold(
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Column {
                                Spacer(modifier = Modifier
                                    .statusBarsHeight()
                                    .fillMaxWidth()
                                )
                                MainNavHost()
                            }
                        }
                    }
                }
            }
        }
    }
}