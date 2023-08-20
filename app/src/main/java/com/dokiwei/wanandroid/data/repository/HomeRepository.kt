package com.dokiwei.wanandroid.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.dokiwei.wanandroid.data.local.database.ArticleDatabase
import com.dokiwei.wanandroid.data.paging.HomeRemoteMediator
import com.dokiwei.wanandroid.model.apidata.BannerData
import com.dokiwei.wanandroid.model.entity.home.HomeEntity
import com.dokiwei.wanandroid.network.impl.HomeApiImpl
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * @author DokiWei
 * @date 2023/8/9 16:22
 */
class HomeRepository @Inject constructor(
    private val articleDatabase: ArticleDatabase
) {
    @OptIn(ExperimentalPagingApi::class)
    fun getAllArticles(): Flow<PagingData<HomeEntity>> {
        val pagingSourceFactory = {
            articleDatabase.homeDao().getAll()
        }
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                initialLoadSize = 40
            ),
            remoteMediator = HomeRemoteMediator(articleDatabase),
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }

    fun getAllBanner(): Flow<PagingData<BannerData>> {
        val pagingSourceFactory = {
            fun getAll(): PagingSource<Int, BannerData> {
                return object : PagingSource<Int, BannerData>() {
                    override fun getRefreshKey(state: PagingState<Int, BannerData>): Int? {
                        return null
                    }

                    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, BannerData> {
                        val result = HomeApiImpl().getBanner()
                        result.getOrNull()?.let {
                            return LoadResult.Page(data = it, nextKey = null, prevKey = null)
                        }
                        return LoadResult.Error(
                            result.exceptionOrNull() ?: Exception("Unknown error")
                        )
                    }
                }
            }
            getAll()
        }
        return Pager(
            config = PagingConfig(pageSize = 3),
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }
}