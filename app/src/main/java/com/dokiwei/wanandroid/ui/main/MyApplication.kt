package com.dokiwei.wanandroid.ui.main

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.flow.MutableStateFlow

/**
 * @author DokiWei
 * @date 2023/7/13 23:02
 */
@HiltAndroidApp
class MyApplication : Application() {
    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
        var isShowLoginTip=MutableStateFlow(false)
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }
}