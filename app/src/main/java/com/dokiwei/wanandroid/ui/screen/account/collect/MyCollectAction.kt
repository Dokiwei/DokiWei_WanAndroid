package com.dokiwei.wanandroid.ui.screen.account.collect

import com.dokiwei.wanandroid.model.apidata.CollectData

/**
 * @author DokiWei
 * @date 2023/7/27 17:27
 */
sealed class MyCollectAction {
    object Refresh : MyCollectAction()
    object LoadMore : MyCollectAction()
    data class SetMyCollectList(val page: Int, val data: List<CollectData>?) : MyCollectAction()
    data class ShowToast(val msg: String) : MyCollectAction()
    data class OutputLogcat(
        val msg: String
    ) : MyCollectAction()

    data class UpdateScrollToTop(val boolean: Boolean) : MyCollectAction()
}