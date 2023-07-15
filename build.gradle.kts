// 顶级构建文件，您可以在其中添加所有子项目/模块通用的配置选项。
@Suppress("DSL_SCOPE_VIOLATION") // TODO: 修复 KTIJ-19369 后移除
plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.kotlinAndroid) apply false
}
true // 需要使抑制注释适用于插件块