package com.dokiwei.wanandroid.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dokiwei.wanandroid.model.entity.remotekey.ProjectRemoteKeys

/**
 * @author DokiWei
 * @date 2023/8/17 19:20
 */
@Dao
interface ProjectRemoteKeysDao {
    @Query("SELECT * FROM project_remote_keys_table WHERE `order` = :order")
    suspend fun getKey(order: Int): ProjectRemoteKeys

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKeys: List<ProjectRemoteKeys>)

    @Query("DELETE FROM project_remote_keys_table")
    suspend fun clearAll()

    @Query("SELECT last_updated FROM project_remote_keys_table ORDER BY `order` ASC LIMIT 1")
    suspend fun getLastUpdated(): Long
}