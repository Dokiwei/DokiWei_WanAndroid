package com.dokiwei.wanandroid.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dokiwei.wanandroid.model.entity.project.ProjectEntity

/**
 * @author DokiWei
 * @date 2023/8/17 19:19
 */
@Dao
interface ProjectDao {

    @Query("SELECT * FROM project_table")
    fun getAll(): PagingSource<Int, ProjectEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(articles: List<ProjectEntity>)

    @Query("DELETE FROM project_table")
    suspend fun clearAll()
}