package com.dokiwei.wanandroid.model.util

/**
 * @author DokiWei
 * @date 2023/8/8 17:45
 */
object Constants {
    const val BASE_URL = "https://www.wanandroid.com/"

    const val HOME_ARTICLE_TABLE = "home_article_table"
    const val QA_ARTICLE_TABLE = "qa_article_table"
    const val SQUARE_ARTICLE_TABLE = "square_article_table"
    const val HOME_ARTICLE_REMOTE_KEYS_TABLE = "home_article_remote_keys_table"
    const val QA_ARTICLE_REMOTE_KEYS_TABLE = "qa_article_remote_keys_table"
    const val SQUARE_ARTICLE_REMOTE_KEYS_TABLE = "square_article_remote_keys_table"

    const val PROJECT_TABLE = "project_table"
    const val PROJECT_REMOTE_KEYS_TABLE = "project_remote_keys_table"
    const val PROJECT_TABS_TABLE = "project_tabs_table"
    const val PROJECT_TABS_REMOTE_KEYS_TABLE = "project_tabs_remote_keys_table"

    const val DATABASE_ARTICLE = "article_database"

    const val API_PAGE_SIZE = 40

    val mainRoute = listOf(Screen.Home.route, Screen.Project.route, Screen.Navigation.route, Screen.Account.route)
}

enum class LogLevel{
    VERBOSE,DEBUG,INFO,WARN,ERROR
}

sealed class Screen(val route: String) {
    data object Home : Screen(HomeScreen.Main.route)
    data object Project : Screen(ProjectScreen.Main.route)
    data object Navigation : Screen(NavigationScreen.Main.route)
    data object Account : Screen(AccountScreen.Main.route)
}

sealed class HomeScreen(val route: String) {
    data object Main : HomeScreen("首页")
    data object Home : HomeScreen("热门")
    data object Qa : HomeScreen("问答")
    data object Square : HomeScreen("广场")
    data object Search : HomeScreen("搜索")
}

sealed class ProjectScreen(val route: String) {
    data object Main : ProjectScreen("项目")
    data object Project : ProjectScreen("项目界面")
}

sealed class NavigationScreen(val route: String) {
    data object Main : NavigationScreen("导航")
    data object Tree : NavigationScreen("体系")
}

sealed class AccountScreen(val route: String) {
    data object Main : AccountScreen("我的")
    data object Login : AccountScreen("登录")
    data object Register : AccountScreen("注册")
    data object Account : AccountScreen("账号")
    data object Collect : AccountScreen("我的收藏")
    data object Message : AccountScreen("我的消息")
    data object Coin : AccountScreen("我的积分")
    data object Rank : AccountScreen("积分排行榜")
    data object About : AccountScreen("关于")
}

sealed class OtherScreen(val route: String) {
    data object WebView : OtherScreen("网页")
    data object Start: OtherScreen("启动")
    data object UserArticles: OtherScreen("作者详细")
}
