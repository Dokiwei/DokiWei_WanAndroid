package com.dokiwei.wanandroid.model.util

import android.util.Log
import android.widget.Toast
import com.dokiwei.wanandroid.ui.main.MyApplication

/**
 * @author DokiWei
 * @date 2023/7/11 18:48
 */
object ToastAndLogcatUtil {

    fun showMsg(msg: String) {
        Toast.makeText(MyApplication.context, msg, Toast.LENGTH_SHORT).show()
    }

    fun log(tag: String = "默认", msg: String, level: LogLevel = LogLevel.ERROR) {
        when (level) {
            LogLevel.VERBOSE -> Log.v(tag, msg)
            LogLevel.DEBUG -> Log.d(tag, msg)
            LogLevel.INFO -> Log.i(tag, msg)
            LogLevel.WARN -> Log.w(tag, msg)
            LogLevel.ERROR -> Log.e(tag, msg)
        }
    }

}