package com.dokiwei.wanandroid.ui.main

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context

/**
 * @author DokiWei
 * @date 2023/7/13 23:02
 */
class MyApplication : Application() {
    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }
}