package com.dokiwei.wanandroid.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dokiwei.wanandroid.model.entity.remotekey.HomeRemoteKeys

/**
 * @author DokiWei
 * @date 2023/8/9 15:15
 */
@Dao
interface HomeRemoteKeysDao {

    @Query("SELECT * FROM home_article_remote_keys_table WHERE `order` = :order")
    suspend fun getKey(order: Int): HomeRemoteKeys

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKeys: List<HomeRemoteKeys>)

    @Query("DELETE FROM home_article_remote_keys_table")
    suspend fun clearAll()

    @Query("SELECT last_updated FROM home_article_remote_keys_table ORDER BY `order` ASC LIMIT 1")
    suspend fun getLastUpdated(): Long
}