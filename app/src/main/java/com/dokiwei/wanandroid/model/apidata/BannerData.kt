package com.dokiwei.wanandroid.model.apidata

import kotlinx.serialization.Serializable

/**
 * @author DokiWei
 * @date 2023/7/11 17:37
 */

@Serializable
data class BannerData(
    val title: String,
    val imagePath: String,
    val url: String
)
