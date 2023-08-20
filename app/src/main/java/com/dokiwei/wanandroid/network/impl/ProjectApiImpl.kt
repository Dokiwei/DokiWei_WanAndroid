package com.dokiwei.wanandroid.network.impl

import com.dokiwei.wanandroid.model.apidata.ProjectData
import com.dokiwei.wanandroid.model.apidata.ProjectTabsData
import com.dokiwei.wanandroid.network.client.RetrofitClient

/**
 * @author DokiWei
 * @date 2023/7/15 17:57
 */
class ProjectApiImpl {
    suspend fun getProjectTitle(): Result<List<ProjectTabsData>> {
        return try {
            RetrofitClient.projectApi.getProjectTitle().parseJsonDataList<ProjectTabsData>()
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getProject(page: Int, id: Int): Result<List<ProjectData>> {
        return try {
            RetrofitClient.projectApi.getProjectList(page, id).parseJsonDatasList<ProjectData>()
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}