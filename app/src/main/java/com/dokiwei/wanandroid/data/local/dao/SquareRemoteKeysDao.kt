package com.dokiwei.wanandroid.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dokiwei.wanandroid.model.entity.remotekey.SquareRemoteKeys

/**
 * @author DokiWei
 * @date 2023/8/9 23:27
 */
@Dao
interface SquareRemoteKeysDao {
    @Query("SELECT * FROM square_article_remote_keys_table WHERE `order` = :order")
    suspend fun getKey(order: Int): SquareRemoteKeys

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKeys: List<SquareRemoteKeys>)

    @Query("DELETE FROM square_article_remote_keys_table")
    suspend fun clearAll()

    @Query("SELECT last_updated FROM square_article_remote_keys_table ORDER BY `order` ASC LIMIT 1")
    suspend fun getLastUpdated(): Long
}