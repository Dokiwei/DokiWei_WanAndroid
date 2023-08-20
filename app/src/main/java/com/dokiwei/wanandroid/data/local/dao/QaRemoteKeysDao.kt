package com.dokiwei.wanandroid.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dokiwei.wanandroid.model.entity.remotekey.QaRemoteKeys

/**
 * @author DokiWei
 * @date 2023/8/9 23:27
 */
@Dao
interface QaRemoteKeysDao {
    @Query("SELECT * FROM qa_article_remote_keys_table WHERE `order` = :order")
    suspend fun getKey(order: Int): QaRemoteKeys

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKeys: List<QaRemoteKeys>)

    @Query("DELETE FROM qa_article_remote_keys_table")
    suspend fun clearAllRemoteKeys()

    @Query("SELECT last_updated FROM qa_article_remote_keys_table ORDER BY `order` ASC LIMIT 1")
    suspend fun getLastUpdated(): Long
}