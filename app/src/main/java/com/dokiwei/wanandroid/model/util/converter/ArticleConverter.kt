package com.dokiwei.wanandroid.model.util.converter

import com.dokiwei.wanandroid.model.apidata.ArticleData
import com.dokiwei.wanandroid.model.entity.home.HomeEntity
import com.dokiwei.wanandroid.model.entity.home.QaEntity
import com.dokiwei.wanandroid.model.entity.home.SquareEntity

/**
 * @author DokiWei
 * @date 2023/9/4 17:20
 */
class ArticleConverter {
    fun <T : Any> fromEntity(data: T) = when (data) {
        is HomeEntity -> ArticleData(
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

        is QaEntity -> ArticleData(
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

        is SquareEntity -> ArticleData(
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

        else -> throw IllegalArgumentException("Unknown data type")
    }


    inline fun <reified T : Any> convertToEntity(data: ArticleData) = when (T::class.java) {
        HomeEntity::class.java -> convertToHomeEntity(data) as T
        QaEntity::class.java -> convertToQaEntity(data) as T
        SquareEntity::class.java -> convertToSquareEntity(data) as T
        else -> throw IllegalArgumentException("Unknown entity type")
    }

    fun convertToHomeEntity(data: ArticleData) = HomeEntity(
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

    fun convertToQaEntity(data: ArticleData) = QaEntity(
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

    fun convertToSquareEntity(data: ArticleData) = SquareEntity(
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