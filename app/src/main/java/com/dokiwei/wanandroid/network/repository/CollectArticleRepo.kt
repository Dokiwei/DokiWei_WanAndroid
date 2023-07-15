package com.dokiwei.wanandroid.network.repository

import com.dokiwei.wanandroid.network.client.RetrofitClient
import org.json.JSONObject

/**
 * @author DokiWei
 * @date 2023/7/11 22:47
 */
class CollectArticleRepo {
    suspend fun likeArticle(id: Int): Result<Boolean> {
        return try {
            val response = RetrofitClient.likeArticleApi.likeArticle(id)
            val responseBody = response.string()
            val json = JSONObject(responseBody)
            val errorCode = json.getInt("errorCode")
            if (errorCode == 0) {
                Result.success(true)
            } else {
                Result.failure(Exception("Error code: $errorCode"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    suspend fun unLikeArticle(id: Int): Result<Boolean> {
        return try {
            val response = RetrofitClient.unlikeArticleApi.unLike(id)
            val responseBody = response.string()
            val json = JSONObject(responseBody)
            val errorCode = json.getInt("errorCode")
            if (errorCode == 0) {
                Result.success(true)
            } else {
                Result.failure(Exception("Error code: $errorCode"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}