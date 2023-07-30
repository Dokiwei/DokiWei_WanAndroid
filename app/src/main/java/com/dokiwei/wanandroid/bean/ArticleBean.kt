package com.dokiwei.wanandroid.bean

import kotlinx.serialization.Serializable

/**
 * @author DokiWei
 * @date 2023/7/11 19:48
 */
@Serializable
data class ArticleBean(
    val title: String,
    val author: String,
    val shareUser: String,
    val superChapterName: String,
    val chapterName: String,
    val niceDate: String,
    val niceShareDate: String,
    val collect: Boolean,
    val id: Int,
    val link: String,
    val fresh: Boolean,
    val tags: List<TagBean>
)
