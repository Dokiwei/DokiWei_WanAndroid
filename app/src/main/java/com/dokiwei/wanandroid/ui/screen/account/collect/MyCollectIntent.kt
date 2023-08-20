package com.dokiwei.wanandroid.ui.screen.account.collect

/**
 * @author DokiWei
 * @date 2023/7/27 17:27
 */
sealed class MyCollectIntent {
    object Refresh : MyCollectIntent()
    object LoadMore : MyCollectIntent()
    data class UpdateScrollToTop(val boolean: Boolean) : MyCollectIntent()
}