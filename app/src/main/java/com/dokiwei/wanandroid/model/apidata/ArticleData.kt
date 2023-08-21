package com.dokiwei.wanandroid.model.apidata

import kotlinx.serialization.Serializable

/**
 * @author DokiWei
 * @date 2023/7/11 19:48
 */
@Serializable
data class ArticleData(
    val id: Int,
    val title: String,
    val author: String,
    val shareUser: String,
    val superChapterName: String,
    val chapterName: String,
    val niceDate: String,
    val niceShareDate: String,
    val collect: Boolean,
    val link: String,
    val fresh: Boolean,
    val tags: List<TagData>,
    val userId:Int
)
