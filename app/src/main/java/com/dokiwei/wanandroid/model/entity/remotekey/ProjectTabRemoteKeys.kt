package com.dokiwei.wanandroid.model.entity.remotekey

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.dokiwei.wanandroid.model.util.Constants

/**
 * @author DokiWei
 * @date 2023/8/17 19:55
 */
@Entity(tableName = Constants.PROJECT_TABS_REMOTE_KEYS_TABLE)
data class ProjectTabRemoteKeys(
    @PrimaryKey(autoGenerate = true)
    val order: Int,
    val prevPage: Int?,
    val nextPage: Int?,
    @ColumnInfo(name = "last_updated") val lastUpdated: Long
)
