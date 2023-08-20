package com.dokiwei.wanandroid.model.util

import com.dokiwei.wanandroid.R

/**
 * @author DokiWei
 * @date 2023/8/20 21:00
 */
fun randomAvatar(): Int {
    val imageIds = intArrayOf(
        R.drawable.user,
        R.drawable.user1,
        R.drawable.user2,
        R.drawable.user3,
        R.drawable.user4,
        R.drawable.user5,
        R.drawable.user6,
        R.drawable.user7,
        R.drawable.user8
    )

    return imageIds[(0..8).random()]
}