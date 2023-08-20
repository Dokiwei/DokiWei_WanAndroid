package com.dokiwei.wanandroid.model.util

/**
 * @author DokiWei
 * @date 2023/7/11 22:27
 */
object TextUtil {
    fun getArticleText(author: String?, shareUser: String?): String {
        return when {
            author.isNullOrBlank() && shareUser.isNullOrBlank() -> ""
            author.isNullOrBlank() -> "$shareUser"
            shareUser.isNullOrBlank() -> "$author"
            author == shareUser -> "$author"
            else -> "作者: $author 分享: $shareUser"
        }
    }

}