package com.dokiwei.wanandroid.ui.screens.person.collect

import com.dokiwei.wanandroid.bean.CollectBean

/**
 * @author DokiWei
 * @date 2023/7/27 17:27
 */
sealed class MyCollectAction {
    object Refresh : MyCollectAction()
    object LoadMore : MyCollectAction()
    data class SetMyCollectList(val page: Int, val data: List<CollectBean>?) : MyCollectAction()
    data class ShowToast(val msg: String) : MyCollectAction()
    data class OutputLogcat(
        val msg: String
    ) : MyCollectAction()

    data class UpdateScrollToTop(val boolean: Boolean) : MyCollectAction()
}