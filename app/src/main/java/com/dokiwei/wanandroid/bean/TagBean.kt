package com.dokiwei.wanandroid.bean

import kotlinx.serialization.Serializable

/**
 * @author DokiWei
 * @date 2023/7/22 23:02
 */
@Serializable
data class TagBean(
    val name: String,
    val url: String
)