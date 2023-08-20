package com.dokiwei.wanandroid.network.impl

import com.dokiwei.wanandroid.model.apidata.ArticleData
import com.dokiwei.wanandroid.model.apidata.BannerData
import com.dokiwei.wanandroid.model.apidata.HotKeyData
import com.dokiwei.wanandroid.network.client.RetrofitClient

/**
 * @author DokiWei
 * @date 2023/7/11 20:02
 */
class HomeApiImpl {

    suspend fun getHomeArticle(page: Int): Result<List<ArticleData>> {
        return try {
            RetrofitClient.homeApi.homeArticle(page).parseJsonDatasList<ArticleData>()
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getBanner(): Result<List<BannerData>> {
        return try {
            RetrofitClient.homeApi.banner().parseJsonDataList<BannerData>()
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getQa(page: Int): Result<List<ArticleData>> {
        return try {
            RetrofitClient.homeApi.qaArticle(page).parseJsonDatasList<ArticleData>()
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getSquareArticle(page: Int): Result<List<ArticleData>> {
        return try {
            RetrofitClient.homeApi.squareArticle(page).parseJsonDatasList<ArticleData>()
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun search(page: Int, k: String): Result<List<ArticleData>> {
        return try {
            RetrofitClient.homeApi.search(page, k).parseJsonDatasList<ArticleData>()
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getHotKey(): Result<List<HotKeyData>> {
        return try {
            RetrofitClient.homeApi.searchHotKey().parseJsonDataList<HotKeyData>()
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

