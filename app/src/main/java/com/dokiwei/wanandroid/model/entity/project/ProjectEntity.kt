package com.dokiwei.wanandroid.model.entity.project

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.dokiwei.wanandroid.model.util.Constants

/**
 * @author DokiWei
 * @date 2023/8/17 19:14
 */
@Entity(tableName = Constants.PROJECT_TABLE)
data class ProjectEntity(
    @PrimaryKey(autoGenerate = true)
    val order: Int,
    val id: Int,
    val title: String,
    val desc: String,
    val author: String,
    val envelopePic: String,
    val link: String,
    val niceDate: String,
    val projectLink: String,
    val collect: Boolean
)
