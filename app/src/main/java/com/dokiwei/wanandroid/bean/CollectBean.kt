package com.dokiwei.wanandroid.bean

import kotlinx.serialization.Serializable

/**
 * @author DokiWei
 * @date 2023/7/19 20:40
 */
@Serializable
data class CollectBean(
    val author: String,
    val title: String,
    val niceDate: String,
    val link: String,
    val desc: String,
    val envelopePic: String,
    val id: Int,
    val originId: Int
)
