package com.dokiwei.wanandroid.model.apidata

import kotlinx.serialization.Serializable

/**
 * @author DokiWei
 * @date 2023/7/13 16:57
 */
@Serializable
data class UserInfoData(
    val coinInfo: CoinInfoData, val userInfo: UserInfo
)

@Serializable
data class CoinInfoData(
    val rank: String, val coinCount: Int, val level: Int, val username: String
)

@Serializable
data class UserInfo(
    val username: String, val email: String, val id: Int
)

@Serializable
data class CoinCountData(
    val coinCount: Int, val reason: String, val desc: String
)

@Serializable
data class UserArticleData(
    val coinInfo: CoinInfoData, val shareArticles: List<ArticleData>
)
