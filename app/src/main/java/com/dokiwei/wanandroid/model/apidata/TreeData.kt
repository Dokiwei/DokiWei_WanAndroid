package com.dokiwei.wanandroid.model.apidata

import kotlinx.serialization.Serializable

/**
 * @author DokiWei
 * @date 2023/7/31 18:40
 */
@Serializable
data class TreeData(
    val name:String,
    val children:List<TreeChildren>
)
@Serializable
data class TreeChildren(
    val id:Int,
    val name:String
)