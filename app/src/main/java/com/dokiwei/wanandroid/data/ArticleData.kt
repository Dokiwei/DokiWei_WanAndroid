package com.dokiwei.wanandroid.data

import kotlinx.serialization.Serializable

/**
 * @author DokiWei
 * @date 2023/7/11 19:48
 */
@Serializable
data class ArticleListData(
    val title:String,
    val author:String,
    val shareUser:String,
    val superChapterName:String,
    val chapterName:String,
    val niceShareDate:String,
    val collect:Boolean,
    val id:Int,
    val link:String,
    val fresh:Boolean
)