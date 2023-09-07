package com.dokiwei.wanandroid.data.base

import androidx.paging.PagingSource
import androidx.paging.PagingState

/**
 * @author DokiWei
 * @date 2023/9/6 22:09
 */
class BasePagingSource<T : Any>(
    private val pageSize: Int = 40,
    private val data: suspend (Int) -> Result<List<T>>
) :
    PagingSource<Int, T>() {

    override fun getRefreshKey(state: PagingState<Int, T>): Int? {
        return null
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, T> {
        val pageNumber = params.key ?: 0
        val result = data(pageNumber % pageSize)
        result.getOrNull()?.let {
            val prevKey = if (pageNumber > 0) (pageNumber % pageSize) - 1 else null
            val nextKey = (pageNumber % pageSize) + 1
            return LoadResult.Page(data = it, nextKey = nextKey, prevKey = prevKey)
        }
        result.exceptionOrNull()?.let { return LoadResult.Error(it) }
        return LoadResult.Error(
            Exception("Unknown error")
        )
    }
}