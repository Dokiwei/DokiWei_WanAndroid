package com.dokiwei.wanandroid.network.impl

import com.dokiwei.wanandroid.bean.ArticleBean
import com.dokiwei.wanandroid.bean.TreeBean
import com.dokiwei.wanandroid.network.client.RetrofitClient
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

/**
 * @author DokiWei
 * @date 2023/7/31 18:44
 */
class TreeApiImpl {
    private val json = Json { ignoreUnknownKeys = true }

    suspend fun getTree(): Result<List<TreeBean>> {
        return try {
            val response = RetrofitClient.treeApi.tree()
            val responseBody = response.string()
            val jsonElement = json.parseToJsonElement(responseBody).jsonObject
            val errorCode = jsonElement["errorCode"]?.jsonPrimitive?.int ?: -1
            if (errorCode == 0) {
                val dataJson = jsonElement["data"]?.jsonArray ?: error("Missing data field")
                val dataList = json.decodeFromJsonElement<List<TreeBean>>(dataJson)
                Result.success(dataList)
            } else {
                val errorMsg = jsonElement["errorMsg"]?.jsonPrimitive?.content ?: "获取错误信息失败"
                Result.failure(Exception("Error code: $errorCode \nError Msg: $errorMsg"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getTreeChildren(page: Int, cid: Int): Result<List<ArticleBean>> {
        return try {
            val response = RetrofitClient.treeApi.treeChildren(page, cid)
            val responseBody = response.string()
            val jsonElement = json.parseToJsonElement(responseBody).jsonObject
            val errorCode = jsonElement["errorCode"]?.jsonPrimitive?.int ?: -1
            if (errorCode == 0) {
                val dataJson = jsonElement["data"]?.jsonObject?.get("datas")?.jsonArray
                    ?: error("Missing data field")
                val dataList = json.decodeFromJsonElement<List<ArticleBean>>(dataJson)
                Result.success(dataList)
            } else {
                val errorMsg = jsonElement["errorMsg"]?.jsonPrimitive?.content ?: "获取错误信息失败"
                Result.failure(Exception("Error code: $errorCode \nError Msg: $errorMsg"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun searchAuthor(page: Int, author: String): Result<List<ArticleBean>> {
        return try {
            val response = RetrofitClient.treeApi.searchAuthor(page, author)
            val responseBody = response.string()
            val jsonElement = json.parseToJsonElement(responseBody).jsonObject
            val errorCode = jsonElement["errorCode"]?.jsonPrimitive?.int ?: -1
            if (errorCode == 0) {
                val dataJson = jsonElement["data"]?.jsonObject?.get("datas")?.jsonArray
                    ?: error("Missing data field")
                val dataList = json.decodeFromJsonElement<List<ArticleBean>>(dataJson)
                Result.success(dataList)
            } else {
                val errorMsg = jsonElement["errorMsg"]?.jsonPrimitive?.content ?: "获取错误信息失败"
                Result.failure(Exception("Error code: $errorCode \nError Msg: $errorMsg"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}