package com.dokiwei.wanandroid.model.apidata

import kotlinx.serialization.Serializable

/**
 * @author DokiWei
 * @date 2023/8/19 19:15
 */
@Serializable
data class NaviData(
    val name:String,
    val articles:List<NaviArticles>
)
@Serializable
data class NaviArticles(
    val title:String,
    val link:String,
)
