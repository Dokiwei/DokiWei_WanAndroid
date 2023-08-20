package com.dokiwei.wanandroid.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dokiwei.wanandroid.model.entity.remotekey.ProjectTabRemoteKeys

/**
 * @author DokiWei
 * @date 2023/8/17 20:15
 */
@Dao
interface ProjectTabRemoteKeysDao {
    @Query("SELECT * FROM project_tabs_remote_keys_table WHERE `order` = :order")
    suspend fun getKey(order: Int): ProjectTabRemoteKeys

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKeys: List<ProjectTabRemoteKeys>)

    @Query("DELETE FROM project_tabs_remote_keys_table")
    suspend fun clearAll()

    @Query("SELECT last_updated FROM project_tabs_remote_keys_table ORDER BY `order` ASC LIMIT 1")
    suspend fun getLastUpdated(): Long
}