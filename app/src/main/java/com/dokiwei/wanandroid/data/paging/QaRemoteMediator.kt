package com.dokiwei.wanandroid.data.paging

import androidx.paging.PagingState
import com.dokiwei.wanandroid.data.base.BaseRemoteMediator
import com.dokiwei.wanandroid.data.local.database.ArticleDatabase
import com.dokiwei.wanandroid.model.apidata.ArticleData
import com.dokiwei.wanandroid.model.entity.home.QaEntity
import com.dokiwei.wanandroid.model.entity.remotekey.QaRemoteKeys
import com.dokiwei.wanandroid.model.util.converter.ArticleConverter
import com.dokiwei.wanandroid.network.impl.HomeApiImpl
import javax.inject.Inject

/**
 * @author DokiWei
 * @date 2023/8/10 0:34
 */
class QaRemoteMediator @Inject constructor(
    db: ArticleDatabase
) : BaseRemoteMediator<QaEntity, QaRemoteKeys, ArticleData>(db) {

    private val homeApiImpl = HomeApiImpl()
    private val dao = db.qaDao()
    private val keys = db.qaRemoteKeysDao()

    override suspend fun getApi(currentPage: Int): Result<List<ArticleData>> {
        return homeApiImpl.getQa(currentPage)
    }

    override suspend fun clearAll() {
        dao.clearAll()
        keys.clearAllRemoteKeys()
    }

    override suspend fun lastUpdated(): Long {
        return keys.getLastUpdated()
    }

    override suspend fun insertAll(remoteKeys: List<QaRemoteKeys>, data: List<QaEntity>) {
        dao.insertAll(data)
        keys.insertAll(remoteKeys)
    }


    override suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, QaEntity>): Int? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { articleEntity ->
                keys.getKey(articleEntity.order)
            }?.prevPage
    }

    override suspend fun getRemoteKeyForLastItem(state: PagingState<Int, QaEntity>): Int? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { articleEntity ->
                keys.getKey(articleEntity.order)
            }?.nextPage
    }

    override suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, QaEntity>): Int? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.order?.let { order ->
                keys.getKey(order)
            }?.nextPage
        }
    }

    override fun convertToRemoteKeys(data: QaEntity, prevPage: Int?, nextPage: Int?): QaRemoteKeys {
        return QaRemoteKeys(
            order = data.order,
            prevPage = prevPage,
            nextPage = nextPage,
            lastUpdated = System.currentTimeMillis()
        )
    }

    override fun convertToEntity(data: ArticleData)= ArticleConverter().convertToEntity<QaEntity>(data)

}