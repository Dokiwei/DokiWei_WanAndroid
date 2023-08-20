package com.dokiwei.wanandroid.model.util

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelLazy
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import com.dokiwei.wanandroid.ui.main.MainActivity
import com.dokiwei.wanandroid.ui.main.PublicViewModel
import kotlin.reflect.KClass

/**
 * @author DokiWei
 * @date 2023/7/8 16:46
 *
 * modifier的自定义扩展方法
 *
 */
fun Modifier.mainBody() =
    composed {
        this
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    }

@SuppressLint("UnnecessaryComposedModifier")
fun Modifier.cardContent() = composed {
    this
        .fillMaxWidth()
        .padding(10.dp, 5.dp)
}

fun NavController.myCustomNavigate(route: String) {
    navigate(route) {
        launchSingleTop = true
        restoreState = true
    }
}

fun publicViewModel(): PublicViewModel = ViewModelLazy(
    PublicViewModel::class,
    { MainActivity.publicViewModelStore!! },
    { ViewModelProvider.Factory.from() }
).value

fun <VM : ViewModel> publicViewModel(
    viewModelClass: KClass<VM>
) = ViewModelLazy(
    viewModelClass,
    { MainActivity.publicViewModelStore!! },
    { ViewModelProvider.Factory.from() }
).value

