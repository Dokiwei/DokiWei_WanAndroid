package com.dokiwei.wanandroid.data.base

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState

/**
 * @author DokiWei
 * @date 2023/9/6 22:09
 */
class BasePagingSource<T : Any>(private val pageSize:Int=40,private val data: suspend (Int) -> Result<List<T>>) :
    PagingSource<Int, T>() {

    override fun getRefreshKey(state: PagingState<Int, T>): Int? {
        return null
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, T> {
        val pageNumber = params.key.also { Log.e("测试", "paramsKey:$it") } ?: 0
        if (pageNumber == 0) {
            val result = data(pageNumber)
            result.getOrNull()?.let {
                return LoadResult.Page(data = it, nextKey = null, prevKey = 1)
            }
            result.exceptionOrNull()?.let { return LoadResult.Error(it) }
        } else if (pageNumber % pageSize == 0) {
            val result = data(pageNumber % pageSize)
            val prevKey = (pageNumber % pageSize) - 1
            val nextKey = (pageNumber % pageSize) + 1
            result.getOrNull()?.let {
                return LoadResult.Page(data = it, nextKey = prevKey, prevKey = nextKey)
            }
            result.exceptionOrNull()?.let { return LoadResult.Error(it) }
        }
        return LoadResult.Error(
            Exception("Unknown error")
        )
    }
}