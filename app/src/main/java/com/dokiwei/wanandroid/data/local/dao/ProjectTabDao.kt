package com.dokiwei.wanandroid.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dokiwei.wanandroid.model.entity.project.ProjectTabEntity

/**
 * @author DokiWei
 * @date 2023/8/17 20:14
 */
@Dao
interface ProjectTabDao {
    @Query("SELECT * FROM project_tabs_table")
    fun getAll(): PagingSource<Int, ProjectTabEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(articles: List<ProjectTabEntity>)

    @Query("DELETE FROM project_tabs_table")
    suspend fun clearAll()
}