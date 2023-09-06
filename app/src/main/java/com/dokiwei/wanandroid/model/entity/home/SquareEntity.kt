package com.dokiwei.wanandroid.model.entity.home

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.dokiwei.wanandroid.model.apidata.TagData
import com.dokiwei.wanandroid.model.util.Constants
import com.dokiwei.wanandroid.model.util.converter.TagSerializableConverter

/**
 * @author DokiWei
 * @date 2023/8/9 23:22
 */
@TypeConverters(TagSerializableConverter::class)
@Entity(tableName = Constants.SQUARE_ARTICLE_TABLE)
data class SquareEntity(
    @PrimaryKey(autoGenerate = true)
    val order: Int,
    val id: Int,
    val title: String,
    val author: String,
    val shareUser: String,
    val superChapterName: String,
    val chapterName: String,
    val niceDate: String,
    val niceShareDate: String,
    val collect: Boolean,
    val link: String,
    val fresh: Boolean,
    val tags: List<TagData>,
    val userId:Int
)
