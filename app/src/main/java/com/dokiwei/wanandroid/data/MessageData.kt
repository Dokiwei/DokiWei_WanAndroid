package com.dokiwei.wanandroid.data

import kotlinx.serialization.Serializable

/**
 * @author DokiWei
 * @date 2023/7/20 17:36
 */
@Serializable
data class MessageData(
    val fromUser: String,
    val fullLink: String,
    val isRead: Int,
    val link: String,
    val message: String,
    val niceDate: String,
    val tag: String,
    val title: String,
)
