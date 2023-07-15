package com.dokiwei.wanandroid.network.repository

import com.dokiwei.wanandroid.data.BannerData
import com.dokiwei.wanandroid.network.client.RetrofitClient
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

/**
 * @author DokiWei
 * @date 2023/7/11 17:35
 */
class BannerRepo {

    private val json = Json { ignoreUnknownKeys = true }

    suspend fun banner(): Result<List<BannerData>> {
        return try {
            val response = RetrofitClient.bannerApi.banner()
            val responseBody = response.string()
            val jsonElement = json.parseToJsonElement(responseBody).jsonObject
            val errorCode = jsonElement["errorCode"]?.jsonPrimitive?.int ?: -1
            if (errorCode == 0) {
                val dataJson = jsonElement["data"]?.jsonArray ?: error("Missing data field")
                val bannerList = json.decodeFromJsonElement<List<BannerData>>(dataJson)
                Result.success(bannerList)
            } else {
                Result.failure(Exception("Error code: $errorCode"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}