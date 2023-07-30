package com.dokiwei.wanandroid.bean

import kotlinx.serialization.Serializable

/**
 * @author DokiWei
 * @date 2023/7/29 18:56
 */
@Serializable
data class HotKeyBean(
    val id: Int,
    val link: String,
    val name: String,
    val order: Int,
    val visible: Int
)