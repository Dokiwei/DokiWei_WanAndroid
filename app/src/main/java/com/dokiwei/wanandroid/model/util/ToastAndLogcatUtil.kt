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

    fun log(tag: String = "默认", msg: String, level: Int=0) {
        when (level) {
            0 -> logE(tag, msg)
            1 -> logD(tag, msg)
            else -> logI(tag, msg)
        }
    }

    private fun logE(tag: String, msg: String) {
        Log.e(tag, msg)
    }

    private fun logD(tag: String, msg: String) {
        Log.d(tag, msg)
    }

    private fun logI(tag: String, msg: String) {
        Log.i(tag, msg)
    }
}