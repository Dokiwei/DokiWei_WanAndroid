package com.dokiwei.wanandroid.bean

import kotlinx.serialization.Serializable

/**
 * @author DokiWei
 * @date 2023/7/11 17:37
 */

@Serializable
data class BannerBean(
    val title: String,
    val imagePath: String,
    val url: String
)
