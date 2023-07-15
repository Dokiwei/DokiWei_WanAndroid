package com.dokiwei.wanandroid.util

import android.app.Application
import android.content.Context
import android.widget.Toast

/**
 * @author DokiWei
 * @date 2023/7/11 18:48
 */
object ToastUtil {
    fun showMsg(context:Context,str:String){
        Toast.makeText(context,str,Toast.LENGTH_SHORT).show()
    }
}