package com.dokiwei.wanandroid.network.repository

import com.dokiwei.wanandroid.data.ProjectData
import com.dokiwei.wanandroid.data.ProjectTitleData
import com.dokiwei.wanandroid.network.client.RetrofitClient
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

/**
 * @author DokiWei
 * @date 2023/7/15 17:57
 */
class ProjectRepo {
    private val json = Json { ignoreUnknownKeys = true }
    suspend fun getProjectTitle():Result<List<ProjectTitleData>>{
        return try {
            val response = RetrofitClient.projectApi.getProjectTitle()
            val responseBody =response.string()
            val jsonElement = json.parseToJsonElement(responseBody).jsonObject
            val errorCode = jsonElement["errorCode"]?.jsonPrimitive?.int?:-1
            if (errorCode==0){
                val dataJson = jsonElement["data"]?.jsonArray?:error("Missing data field")
                val projectTitleList= json.decodeFromJsonElement<List<ProjectTitleData>>(dataJson)
                Result.success(projectTitleList)
            }else{
                Result.failure(Exception("Error code: $errorCode"))
            }
        }catch (e: Exception) {
            Result.failure(e)
        }
    }
    suspend fun getProject(page:Int,id:Int):Result<List<ProjectData>>{
        return try {
            val response=RetrofitClient.projectApi.getProjectList(page, id)
            val responseBody=response.string()
            val jsonElement=json.parseToJsonElement(responseBody).jsonObject
            val errorCode=jsonElement["errorCode"]?.jsonPrimitive?.int?:-1
            if (errorCode==0){
                val dataJson=jsonElement["data"]?.jsonObject?.get("datas")?.jsonArray ?: error("Missing data field")
                val projectList=json.decodeFromJsonElement<List<ProjectData>>(dataJson)
                Result.success(projectList)
            }else{
                Result.failure(Exception("Error code: $errorCode"))
            }
        }catch (e: Exception) {
            Result.failure(e)
        }
    }
}