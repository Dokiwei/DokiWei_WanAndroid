package com.dokiwei.wanandroid.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dokiwei.wanandroid.model.entity.home.QaEntity

/**
 * @author DokiWei
 * @date 2023/8/9 23:27
 */
@Dao
interface QaDao {

    @Query("SELECT * FROM qa_article_table")
    fun getAll(): PagingSource<Int, QaEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(articles: List<QaEntity>)

    @Query("DELETE FROM qa_article_table")
    suspend fun clearAll()

}