package com.dokiwei.wanandroid.ui.animation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.navigation.NavBackStackEntry

/**
 * @author DokiWei
 * @date 2023/7/7 17:26
 */
object ScreenAnim {
    fun enterScreenAnim():AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition {
        return{
            slideInHorizontally(
                initialOffsetX = { it },
                animationSpec = tween(500, easing = LinearOutSlowInEasing)
            ) + fadeIn(animationSpec = tween(500))
        }
    }

    fun exitScreenAnim(): (AnimatedContentTransitionScope<NavBackStackEntry>) -> ExitTransition {
        return {
            slideOutHorizontally(
                targetOffsetX = { -it },
                animationSpec = tween(500, easing = FastOutLinearInEasing)
            ) + fadeOut(animationSpec = tween(500))
        }
    }

    fun popEnterScreenAnim(): AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition {
        return {
            slideInHorizontally(
                initialOffsetX = { -it },
                animationSpec = tween(500, easing = LinearOutSlowInEasing)
            ) + fadeIn(animationSpec = tween(500))
        }
    }

    fun popExitScreenAnim(): (AnimatedContentTransitionScope<NavBackStackEntry>) -> ExitTransition {
        return {
            slideOutHorizontally(
                targetOffsetX = { it },
                animationSpec = tween(500, easing = FastOutLinearInEasing)
            ) + fadeOut(animationSpec = tween(500))
        }
    }
}
