package com.dokiwei.wanandroid.model.apidata

import kotlinx.serialization.Serializable

/**
 * @author DokiWei
 * @date 2023/7/20 17:36
 */
@Serializable
data class MessageData(
    val date:Long,
    val fromUser: String,
    val fullLink: String,
    val message: String,
    val niceDate: String,
    val tag: String,
    val title: String,
)
