package com.dokiwei.wanandroid.data.paging

import androidx.paging.PagingState
import com.dokiwei.wanandroid.data.base.BaseRemoteMediator
import com.dokiwei.wanandroid.data.local.database.ArticleDatabase
import com.dokiwei.wanandroid.model.apidata.ArticleData
import com.dokiwei.wanandroid.model.entity.home.HomeEntity
import com.dokiwei.wanandroid.model.entity.remotekey.HomeRemoteKeys
import com.dokiwei.wanandroid.network.impl.HomeApiImpl
import javax.inject.Inject

/**
 * @author DokiWei
 * @date 2023/8/11 17:49
 */
class HomeRemoteMediator @Inject constructor(
    db: ArticleDatabase
) : BaseRemoteMediator<HomeEntity, HomeRemoteKeys, ArticleData>(db) {

    private val homeApiImpl = HomeApiImpl()
    private val dao = db.homeDao()
    private val keys = db.homeRemoteKeysDao()

    override suspend fun getApi(currentPage: Int): Result<List<ArticleData>> {
        return homeApiImpl.getHomeArticle(currentPage)
    }

    override suspend fun clearAll() {
        dao.clearAll()
        keys.clearAll()
    }

    override suspend fun lastUpdated(): Long {
        return keys.getLastUpdated()
    }


    override suspend fun getRemoteKeyForLastItem(state: PagingState<Int, HomeEntity>): Int? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { articleEntity ->
                keys.getKey(articleEntity.order)
            }?.nextPage
    }

    override suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, HomeEntity>): Int? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { articleEntity ->
                keys.getKey(articleEntity.order)
            }?.prevPage
    }

    override suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, HomeEntity>): Int? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.order?.let { order ->
                keys.getKey(order)
            }?.nextPage
        }
    }

    override suspend fun insertAll(remoteKeys: List<HomeRemoteKeys>, data: List<HomeEntity>) {
        dao.insertAll(data)
        keys.insertAll(remoteKeys)
    }

    override fun convertToRemoteKeys(
        data: HomeEntity,
        prevPage: Int?,
        nextPage: Int?
    ): HomeRemoteKeys {
        return HomeRemoteKeys(
            order = data.order,
            prevPage, nextPage, System.currentTimeMillis()
        )
    }

    override fun convertToEntity(data: ArticleData): HomeEntity {
        return HomeEntity(
            order = 0,
            id = data.id,
            title = data.title,
            author = data.author,
            shareUser = data.shareUser,
            superChapterName = data.superChapterName,
            chapterName = data.chapterName,
            niceDate = data.niceDate,
            niceShareDate = data.niceShareDate,
            collect = data.collect,
            link = data.link,
            fresh = data.fresh,
            tags = data.tags,
            userId = data.userId
        )
    }

}