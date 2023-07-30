package com.dokiwei.wanandroid.ui.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModelProvider
import com.dokiwei.wanandroid.ui.screens.mainnavhost.MainNavHost
import com.dokiwei.wanandroid.ui.theme.MainTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ViewModelProvider(this)[PublicViewModel::class.java]
        setContent {
            MainTheme {
                MainNavHost()
            }
        }
    }
}