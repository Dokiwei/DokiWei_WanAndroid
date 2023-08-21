package com.dokiwei.wanandroid.data.paging

import androidx.paging.PagingState
import com.dokiwei.wanandroid.data.base.BaseRemoteMediator
import com.dokiwei.wanandroid.data.local.database.ArticleDatabase
import com.dokiwei.wanandroid.model.apidata.ArticleData
import com.dokiwei.wanandroid.model.entity.home.SquareEntity
import com.dokiwei.wanandroid.model.entity.remotekey.SquareRemoteKeys
import com.dokiwei.wanandroid.network.impl.HomeApiImpl
import javax.inject.Inject

/**
 * @author DokiWei
 * @date 2023/8/11 17:40
 */
class SquareRemoteMediator @Inject constructor(
    db: ArticleDatabase
) : BaseRemoteMediator<SquareEntity, SquareRemoteKeys, ArticleData>(db) {

    private val homeApiImpl = HomeApiImpl()
    private val dao = db.squareDao()
    private val keys = db.squareRemoteKeysDao()

    override suspend fun getApi(currentPage: Int): Result<List<ArticleData>> {
        return homeApiImpl.getSquareArticle(currentPage)
    }

    override suspend fun clearAll() {
        dao.clearAll()
        keys.clearAll()
    }

    override suspend fun lastUpdated(): Long {
        return keys.getLastUpdated()
    }

    override suspend fun insertAll(remoteKeys: List<SquareRemoteKeys>, data: List<SquareEntity>) {
        dao.insertAll(data)
        keys.insertAll(remoteKeys)
    }

    override suspend fun getRemoteKeyForLastItem(state: PagingState<Int, SquareEntity>): Int? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { articleEntity ->
                keys.getKey(articleEntity.order)
            }?.nextPage
    }

    override suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, SquareEntity>): Int? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { articleEntity ->
                keys.getKey(articleEntity.order)
            }?.prevPage
    }

    override suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, SquareEntity>): Int? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.order?.let { order ->
                keys.getKey(order)
            }?.nextPage
        }
    }

    override fun convertToRemoteKeys(
        data: SquareEntity,
        prevPage: Int?,
        nextPage: Int?
    ): SquareRemoteKeys {
        return SquareRemoteKeys(
            order = data.order,
            prevPage = prevPage,
            nextPage = nextPage,
            lastUpdated = System.currentTimeMillis()
        )
    }

    override fun convertToEntity(data: ArticleData): SquareEntity {
        return SquareEntity(
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