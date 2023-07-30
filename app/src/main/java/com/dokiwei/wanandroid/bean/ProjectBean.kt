package com.dokiwei.wanandroid.bean

import kotlinx.serialization.Serializable

/**
 * @author DokiWei
 * @date 2023/7/15 17:57
 */

@Serializable
data class ProjectBean(
    val id: Int,
    val title: String,
    val desc: String,
    val author: String,
    val envelopePic: String,
    val link: String,
    val niceDate: String,
    val projectLink: String,
    val collect: Boolean
)
