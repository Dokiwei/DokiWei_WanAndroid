package com.dokiwei.wanandroid.data.base

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.dokiwei.wanandroid.data.local.database.ArticleDatabase
import java.util.concurrent.TimeUnit

/**
 * @author DokiWei
 * @date 2023/8/9 23:34
 */

/**
 * @param T:Entity
 * @param R:RemoteKeys
 * @param E:ApiData
 */
@OptIn(ExperimentalPagingApi::class)
abstract class BaseRemoteMediator<T : Any, R : Any, E : Any>(
    private val articleDatabase: ArticleDatabase
) : RemoteMediator<Int, T>() {


    abstract suspend fun getApi(currentPage: Int): Result<List<E>>
    abstract fun convertToEntity(data: E): T
    abstract fun convertToRemoteKeys(data: T, prevPage: Int?, nextPage: Int?): R

    abstract suspend fun clearAll()
    abstract suspend fun insertAll(remoteKeys: List<R>, data: List<T>)

    abstract suspend fun lastUpdated(): Long

    override suspend fun initialize(): InitializeAction {
        val cacheTimeout = TimeUnit.MILLISECONDS.convert(1, TimeUnit.HOURS)
        return if (System.currentTimeMillis() - lastUpdated() <= cacheTimeout) {
            InitializeAction.SKIP_INITIAL_REFRESH
        } else {
            InitializeAction.LAUNCH_INITIAL_REFRESH
        }
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, T>
    ): MediatorResult {
        return try {
            val currentPage = when (loadType) {
                LoadType.REFRESH -> {
                    val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                    remoteKeys?.minus(1) ?: 0
                }

                LoadType.PREPEND -> {
                    val remoteKeys = getRemoteKeyForFirstItem(state)
                    val pervPage = remoteKeys ?: return MediatorResult.Success(
                        endOfPaginationReached = false
                    )
                    pervPage
                }

                LoadType.APPEND -> {
                    val remoteKeys = getRemoteKeyForLastItem(state)
                    val nextPage = remoteKeys ?: return MediatorResult.Success(
                        endOfPaginationReached = false
                    )
                    nextPage
                }
            }

            val result = getApi(currentPage)
            val endOfPaginationReached = result.getOrNull().isNullOrEmpty()
            val prevPage = if (currentPage == 0) null else currentPage - 1
            val nextPage = if (endOfPaginationReached) null else currentPage + 1
            articleDatabase.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    clearAll()
                }
                result.getOrNull()?.let {
                    val entity = it.map { data -> convertToEntity(data) }
                    val keys = entity.map { data ->
                        convertToRemoteKeys(data, prevPage, nextPage)
                    }
                    insertAll(keys, entity)
                }
            }
            MediatorResult.Success(endOfPaginationReached)
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }


    /**
     *
     *@return nextPage
     */
    abstract suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, T>): Int?

    /**
     *@return pervPage
     */
    abstract suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, T>): Int?

    /**
     *@return nextPage
     */
    abstract suspend fun getRemoteKeyForLastItem(state: PagingState<Int, T>): Int?


}