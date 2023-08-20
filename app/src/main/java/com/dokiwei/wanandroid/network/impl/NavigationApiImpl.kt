package com.dokiwei.wanandroid.network.impl

import com.dokiwei.wanandroid.model.apidata.ArticleData
import com.dokiwei.wanandroid.model.apidata.NaviData
import com.dokiwei.wanandroid.model.apidata.TreeData
import com.dokiwei.wanandroid.network.client.RetrofitClient

/**
 * @author DokiWei
 * @date 2023/7/31 18:44
 */
class NavigationApiImpl {
    suspend fun getNavigation():Result<List<NaviData>>{
        return try {
            RetrofitClient.navigationApi.navi().parseJsonDataList<NaviData>()
        }catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getTree(): Result<List<TreeData>> {
        return try {
            RetrofitClient.navigationApi.tree().parseJsonDataList<TreeData>()
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getTreeChildren(page: Int, cid: Int): Result<List<ArticleData>> {
        return try {
            RetrofitClient.navigationApi.treeChildren(page, cid).parseJsonDatasList<ArticleData>()
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun searchAuthor(page: Int, author: String): Result<List<ArticleData>> {
        return try {
            RetrofitClient.navigationApi.searchAuthor(page, author).parseJsonDatasList<ArticleData>()
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}