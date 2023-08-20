package com.dokiwei.wanandroid.network.impl

import com.dokiwei.wanandroid.model.apidata.CollectData
import com.dokiwei.wanandroid.network.client.RetrofitClient

/**
 * @author DokiWei
 * @date 2023/7/11 22:47
 */
class CollectApiImpl {

    suspend fun myLike(page: Int): Result<List<CollectData>> {
        return try {
            RetrofitClient.collectApi.myLike(page).parseJsonDatasList<CollectData>()
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun collect(id: Int): Result<Int> {
        return try {
            RetrofitClient.collectApi.like(id).parseJsonElement("errorCode")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun unCollect(id: Int): Result<Int> {
        return try {
            RetrofitClient.collectApi.unlike(id).parseJsonElement("errorCode")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}