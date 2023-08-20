package com.dokiwei.wanandroid.model.entity.project

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.dokiwei.wanandroid.model.util.Constants

/**
 * @author DokiWei
 * @date 2023/8/17 19:53
 */
@Entity(tableName = Constants.PROJECT_TABS_TABLE)
data class ProjectTabEntity(
    @PrimaryKey(autoGenerate = true)
    val order: Int,
    val id: Int,
    val name: String
)