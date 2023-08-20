package com.dokiwei.wanandroid.model.entity.remotekey

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.dokiwei.wanandroid.model.util.Constants.HOME_ARTICLE_REMOTE_KEYS_TABLE

/**
 * @author DokiWei
 * @date 2023/8/9 15:04
 */
@Entity(tableName = HOME_ARTICLE_REMOTE_KEYS_TABLE)
data class HomeRemoteKeys(
    @PrimaryKey(autoGenerate = true)
    val order: Int,
    val prevPage: Int?,
    val nextPage: Int?,
    @ColumnInfo(name = "last_updated") val lastUpdated: Long
)
