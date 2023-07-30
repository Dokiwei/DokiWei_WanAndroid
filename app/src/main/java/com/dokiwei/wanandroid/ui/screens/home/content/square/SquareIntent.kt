package com.dokiwei.wanandroid.ui.screens.home.content.square

/**
 * @author DokiWei
 * @date 2023/7/26 23:41
 */
sealed class SquareIntent {
    object Refresh : SquareIntent()
    object LoadMore : SquareIntent()
}