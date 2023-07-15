package com.dokiwei.wanandroid.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColors = lightColorScheme(
    primary = md_theme_light_primary,//主要
    onPrimary = md_theme_light_onPrimary,//在主要
    primaryContainer = md_theme_light_primaryContainer,//主容器
    onPrimaryContainer = md_theme_light_onPrimaryContainer,//在主容器上
    secondary = md_theme_light_secondary,//次要
    onSecondary = md_theme_light_onSecondary,//在次要上
    secondaryContainer = md_theme_light_secondaryContainer,//次要容器
    onSecondaryContainer = md_theme_light_onSecondaryContainer,//在次要容器上
    tertiary = md_theme_light_tertiary,//第三级
    onTertiary = md_theme_light_onTertiary,//在第三级
    tertiaryContainer = md_theme_light_tertiaryContainer,//三级容器
    onTertiaryContainer = md_theme_light_onTertiaryContainer,//在第三级容器上
    error = md_theme_light_error,//错误
    errorContainer = md_theme_light_errorContainer,//错误容器
    onError = md_theme_light_onError,//出错时
    onErrorContainer = md_theme_light_onErrorContainer,//在错误容器上
    background = md_theme_light_background,//背景
    onBackground = md_theme_light_onBackground,//在背景上
    surface = md_theme_light_surface,//表面
    onSurface = md_theme_light_onSurface,//在表面上
    surfaceVariant = md_theme_light_surfaceVariant,//表面变体
    onSurfaceVariant = md_theme_light_onSurfaceVariant,//表面变体
    outline = md_theme_light_outline,//大纲
    inverseOnSurface = md_theme_light_inverseOnSurface,//反曲面
    inverseSurface = md_theme_light_inverseSurface,//反曲面
    inversePrimary = md_theme_light_inversePrimary,//逆原色
    surfaceTint = md_theme_light_surfaceTint,//表面色调
    outlineVariant = md_theme_light_outlineVariant,//大纲变体
    scrim = md_theme_light_scrim,//稀松布
)


private val DarkColors = darkColorScheme(
    primary = md_theme_dark_primary,
    onPrimary = md_theme_dark_onPrimary,
    primaryContainer = md_theme_dark_primaryContainer,
    onPrimaryContainer = md_theme_dark_onPrimaryContainer,
    secondary = md_theme_dark_secondary,
    onSecondary = md_theme_dark_onSecondary,
    secondaryContainer = md_theme_dark_secondaryContainer,
    onSecondaryContainer = md_theme_dark_onSecondaryContainer,
    tertiary = md_theme_dark_tertiary,
    onTertiary = md_theme_dark_onTertiary,
    tertiaryContainer = md_theme_dark_tertiaryContainer,
    onTertiaryContainer = md_theme_dark_onTertiaryContainer,
    error = md_theme_dark_error,
    errorContainer = md_theme_dark_errorContainer,
    onError = md_theme_dark_onError,
    onErrorContainer = md_theme_dark_onErrorContainer,
    background = md_theme_dark_background,
    onBackground = md_theme_dark_onBackground,
    surface = md_theme_dark_surface,
    onSurface = md_theme_dark_onSurface,
    surfaceVariant = md_theme_dark_surfaceVariant,
    onSurfaceVariant = md_theme_dark_onSurfaceVariant,
    outline = md_theme_dark_outline,
    inverseOnSurface = md_theme_dark_inverseOnSurface,
    inverseSurface = md_theme_dark_inverseSurface,
    inversePrimary = md_theme_dark_inversePrimary,
    surfaceTint = md_theme_dark_surfaceTint,
    outlineVariant = md_theme_dark_outlineVariant,
    scrim = md_theme_dark_scrim,
)


@Composable
fun MainTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColors
        else -> LightColors
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}