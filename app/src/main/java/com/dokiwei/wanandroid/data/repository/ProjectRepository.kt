package com.dokiwei.wanandroid.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.RemoteMediator
import com.dokiwei.wanandroid.data.local.database.ArticleDatabase
import com.dokiwei.wanandroid.data.paging.ProjectTabRemoteMediator
import com.dokiwei.wanandroid.model.entity.project.ProjectEntity
import com.dokiwei.wanandroid.model.entity.project.ProjectTabEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * @author DokiWei
 * @date 2023/8/17 19:49
 */
class ProjectRepository @Inject constructor(
    private val articleDatabase: ArticleDatabase
) {


    @OptIn(ExperimentalPagingApi::class)
    fun getAllTitle(): Flow<PagingData<ProjectTabEntity>> {
        val pagingSourceFactory = {
            articleDatabase.projectTabDao().getAll()
        }
        return Pager(
            config = PagingConfig(
                pageSize = 28
            ),
            remoteMediator = ProjectTabRemoteMediator(articleDatabase),
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }

    @OptIn(ExperimentalPagingApi::class)
    fun getAllProject(remoteMediator: RemoteMediator<Int, ProjectEntity>): Flow<PagingData<ProjectEntity>> {
        val pagingSourceFactory = {
            articleDatabase.projectDao().getAll()
        }
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                initialLoadSize = 40
            ),
            remoteMediator = remoteMediator,
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }

}