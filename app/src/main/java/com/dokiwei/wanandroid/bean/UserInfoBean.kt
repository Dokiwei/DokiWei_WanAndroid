package com.dokiwei.wanandroid.bean

import kotlinx.serialization.Serializable

/**
 * @author DokiWei
 * @date 2023/7/13 16:57
 */
@Serializable
data class UserInfoBean(
    val coinInfo: CoinInfo,
    val userInfo: UserInfo
)

@Serializable
data class CoinInfo(
    val rank: String,
    val coinCount: Int,
    val level: Int
)
@Serializable
data class UserInfo(
    val username:String,
    val email:String
)