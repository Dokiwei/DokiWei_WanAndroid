package com.dokiwei.wanandroid.data

import kotlinx.serialization.Serializable

/**
 * @author DokiWei
 * @date 2023/7/15 17:57
 */
@Serializable
data class ProjectTitleData(
    val id:Int,
    val name:String
)
@Serializable
data class ProjectData(
    val id: Int,
    val title:String,
    val desc:String,
    val author:String,
    val envelopePic:String,
    val link:String,
    val niceDate:String,
    val projectLink:String,
    val collect:Boolean
)
