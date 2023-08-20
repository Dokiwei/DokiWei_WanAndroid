package com.dokiwei.wanandroid.data.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingState
import com.dokiwei.wanandroid.data.base.BaseRemoteMediator
import com.dokiwei.wanandroid.data.local.database.ArticleDatabase
import com.dokiwei.wanandroid.model.apidata.ProjectData
import com.dokiwei.wanandroid.model.apidata.ProjectTabsData
import com.dokiwei.wanandroid.model.entity.project.ProjectEntity
import com.dokiwei.wanandroid.model.entity.project.ProjectTabEntity
import com.dokiwei.wanandroid.model.entity.remotekey.ProjectRemoteKeys
import com.dokiwei.wanandroid.model.entity.remotekey.ProjectTabRemoteKeys
import com.dokiwei.wanandroid.network.impl.ProjectApiImpl
import kotlinx.coroutines.runBlocking
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * @author DokiWei
 * @date 2023/8/17 19:04
 */
@OptIn(ExperimentalPagingApi::class)
class ProjectRemoteMediator @Inject constructor(
    db: ArticleDatabase
) : BaseRemoteMediator<ProjectEntity, ProjectRemoteKeys, ProjectData>(db) {

    private val projectApiImpl = ProjectApiImpl()
    private val dao = db.projectDao()
    private val keys = db.projectRemoteKeysDao()

    // 默认去访问的分类id
    private var cid = 294

    // 用来外部更新分类id
    suspend fun updateCid(cid: Int) {
        runBlocking { clearAll() }
        this.cid = cid
    }

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun getApi(currentPage: Int): Result<List<ProjectData>> {
        return projectApiImpl.getProject(currentPage, cid)
    }

    override suspend fun clearAll() {
        dao.clearAll()
        keys.clearAll()
    }

    override suspend fun lastUpdated(): Long {
        return keys.getLastUpdated()
    }

    override suspend fun getRemoteKeyForLastItem(state: PagingState<Int, ProjectEntity>): Int? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { articleEntity ->
                keys.getKey(articleEntity.order)
            }?.nextPage
    }

    override suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, ProjectEntity>): Int? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { articleEntity ->
                keys.getKey(articleEntity.order)
            }?.prevPage
    }

    override suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, ProjectEntity>): Int? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.order?.let { order ->
                keys.getKey(order)
            }?.nextPage
        }
    }

    override suspend fun insertAll(remoteKeys: List<ProjectRemoteKeys>, data: List<ProjectEntity>) {
        dao.insertAll(data)
        keys.insertAll(remoteKeys)
    }

    override fun convertToRemoteKeys(
        data: ProjectEntity,
        prevPage: Int?,
        nextPage: Int?
    ) = ProjectRemoteKeys(
        order = data.order,
        prevPage = prevPage,
        nextPage = nextPage,
        lastUpdated = System.currentTimeMillis()
    )

    override fun convertToEntity(data: ProjectData) = ProjectEntity(
        order = 0,
        id = data.id,
        title = data.title,
        desc = data.desc,
        author = data.author,
        envelopePic = data.envelopePic,
        link = data.link,
        niceDate = data.niceDate,
        projectLink = data.projectLink,
        collect = data.collect
    )


}

@OptIn(ExperimentalPagingApi::class)
class ProjectTabRemoteMediator @Inject constructor(
    db: ArticleDatabase
) : BaseRemoteMediator<ProjectTabEntity, ProjectTabRemoteKeys, ProjectTabsData>(db) {

    private val projectApiImpl = ProjectApiImpl()
    private val dao = db.projectTabDao()
    private val keys = db.projectTabRemoteKeysDao()

    override suspend fun initialize(): InitializeAction {
        val cacheTimeout = TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS)
        return if (System.currentTimeMillis() - lastUpdated() <= cacheTimeout) {
            InitializeAction.SKIP_INITIAL_REFRESH
        } else {
            InitializeAction.LAUNCH_INITIAL_REFRESH
        }
    }

    override suspend fun getApi(currentPage: Int): Result<List<ProjectTabsData>> {
        return projectApiImpl.getProjectTitle()
    }

    override suspend fun clearAll() {
        dao.clearAll()
        keys.clearAll()
    }

    override suspend fun lastUpdated(): Long {
        return keys.getLastUpdated()
    }

    override suspend fun getRemoteKeyForLastItem(state: PagingState<Int, ProjectTabEntity>): Int? {
        return null
    }

    override suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, ProjectTabEntity>): Int? {
        return null
    }

    override suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, ProjectTabEntity>): Int? {
        return null
    }

    override suspend fun insertAll(
        remoteKeys: List<ProjectTabRemoteKeys>,
        data: List<ProjectTabEntity>
    ) {
        dao.insertAll(data)
        keys.insertAll(remoteKeys)
    }

    override fun convertToRemoteKeys(
        data: ProjectTabEntity,
        prevPage: Int?,
        nextPage: Int?
    ) = ProjectTabRemoteKeys(
        order = data.order,
        prevPage = prevPage,
        nextPage = nextPage,
        lastUpdated = System.currentTimeMillis()
    )

    override fun convertToEntity(data: ProjectTabsData) = ProjectTabEntity(
        order = 0,
        id = data.id,
        name = data.name
    )


}