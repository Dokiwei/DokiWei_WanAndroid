package com.dokiwei.wanandroid.model.apidata

import kotlinx.serialization.Serializable

/**
 * @author DokiWei
 * @date 2023/7/22 23:02
 */
@Serializable
data class TagData(
    val name: String,
    val url: String
)