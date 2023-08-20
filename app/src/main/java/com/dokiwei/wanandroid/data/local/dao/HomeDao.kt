package com.dokiwei.wanandroid.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import com.dokiwei.wanandroid.model.entity.home.HomeEntity

/**
 * @author DokiWei
 * @date 2023/8/9 15:07
 */
@Dao
interface HomeDao {

    @Query("SELECT * FROM home_article_table")
    fun getAll(): PagingSource<Int, HomeEntity>

    @Insert(onConflict = REPLACE)
    suspend fun insertAll(articles: List<HomeEntity>)

    @Query("DELETE FROM home_article_table")
    suspend fun clearAll()

}