package com.dokiwei.wanandroid.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.dokiwei.wanandroid.data.local.database.ArticleDatabase
import com.dokiwei.wanandroid.data.paging.SquareRemoteMediator
import com.dokiwei.wanandroid.model.entity.home.SquareEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * @author DokiWei
 * @date 2023/8/11 18:19
 */
class SquareRepository @Inject constructor(
    private val articleDatabase: ArticleDatabase
) {
    @OptIn(ExperimentalPagingApi::class)
    fun getAllArticles(): Flow<PagingData<SquareEntity>> {
        val pagingSourceFactory = { articleDatabase.squareDao().getAll() }
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                initialLoadSize = 40
            ),
            remoteMediator = SquareRemoteMediator(articleDatabase),
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }
}