package com.dokiwei.wanandroid.model.util.converter

import com.dokiwei.wanandroid.model.apidata.ArticleData
import com.dokiwei.wanandroid.model.entity.home.HomeEntity

/**
 * @author DokiWei
 * @date 2023/8/9 18:16
 */
fun ArticleData.convertArticleDataToEntity(): HomeEntity {
    return HomeEntity(
        order = 0,
        id = id,
        title = title,
        author = author,
        shareUser = shareUser,
        superChapterName = superChapterName,
        chapterName = chapterName,
        niceDate = niceDate,
        niceShareDate = niceShareDate,
        collect = collect,
        link = link,
        fresh = fresh,
        tags = tags
    )
}