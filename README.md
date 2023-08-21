# DokiWei-WanAndroid

<div align="center">

[![Github][Dokiwei]][Dokiwei-Url]

</div>

## 目录

* [简介](#简介)
* [特点](#特点)
* [主要框架及插件](#主要框架及插件)
* [截图](#截图)
* [详细](#详细)
* [关于架构模式](#关于架构模式)

## 简介

本软件完全遵循Material Design 3的设计规范, 并且在Android12+支持Dynamic colors

在架构模式上使用了MVI架构, 并且严格遵循Google推荐的数据管理方式, 即唯一可信数据源, 单向数据流

UI完全由用 Jectpack Compose 以声明式编程的方式构建, Jetpack Compose 是 Android 推荐的用于构建本机 UI 的现代工具包。它简化并加速了 Android 上的 UI 开发。通过更少的代码、强大的工具和直观的 Kotlin API，快速使您的应用栩栩如生。Jectpack Compose 可以大幅提升界面的复用性, 在嵌套布局中几乎不会消耗性能,但是要注意的是, Compose 组件会多次重组, 所以请不要将不必要的数据传入或写进组件内, 如果实在需要请使用`remember`来避免不必要的重复赋值。Jectpack Compose 可以完美的契合 Kotlin 的 Flow 数据流,省去了其他繁琐的数据状态观察方式

网络请求框架使用Retrofit2, 此框架可以快速方便的构建网络请求

数据持久层使用了Room数据库来存储可能需要多次访问的数据, 并配合Paging3的`RemoteMediator`来实现从多个数据源获取数据

## 特点

* 使用现代化的开发架构以及插件进行App的构建
* 利用 Kotlin 的`inline`内联函数进行API数据实体类转化, 可以大幅提高 Lambda 函数运行的效率, 并且在函数中添加了API数据`errorCode`的监控, 以便在用户未登录时或其他问题时提醒用户
* 充分利用 Kotlin 的协程进行耗时操作, 避免堵塞主线程造成应用卡顿
* 自定义了一个网络拦截器用来输出网络日志方便调试
* 利用 MVI 架构精细的控制应用状态, 并且集中的 Intent 解决了 MVVM 调试难以溯源的问题

## 主要框架及插件
* [![Compose][Jetpack-Compose]][Jetpack-Compose-Url]
* [![Kotlin][Kotlin]][Kotlin-Url]
* [![Gradle][Gradle]][Gradle-Url]
* [![Room][Room]][Room-Url]
* [![Paging][Paging]][Paging-Url]
* [![Retrofit][Retrofit]][Retrofit-Url]
## 截图

| 启动页 | 首页 | 项目 | 导航 | 账号 |
| :----: | :--: | :--: | :--: | :--: |
|![启动页][start]| ![首页][home] | ![项目][home] | ![导航][navigation] | ![账号][account] |

|        启动页-深色         |        首页-深色        |         项目-深色          |           导航-深色           |         账号-深色          |
| :------------------------: | :---------------------: | :------------------------: | :---------------------------: | :------------------------: |
| ![启动页-深色][start-dark] | ![首页-深色][home-dark] | ![项目-深色][project-dark] | ![导航-深色][navigation-dark] | ![账号-深色][account-dark] |


## 详细

Room具有以下优势：
* 针对 SQL 查询的编译时验证。
* 可最大限度减少重复和容易出错的样板代码的方便注解。
* 简化了数据库迁移路径。

Room可以利用注解以及查询语句快速的自动生成java对象供开发者使用
```kotlin
@Dao
interface HomeDao {

    @Query("SELECT * FROM home_article_table")
    fun getAll(): PagingSource<Int, HomeEntity>

    @Insert(onConflict = REPLACE)
    suspend fun insertAll(articles: List<HomeEntity>)

    @Query("DELETE FROM home_article_table")
    suspend fun clearAll()

}
```

Paging具有以下功能:
* 分页数据的内存中缓存。该功能有助于确保您的应用在处理分页数据时高效使用系统资源。
* 内置的请求去重功能，可确保您的应用高效利用网络带宽和系统资源。
* 可配置的 RecyclerView 适配器，会在用户滚动到已加载数据的末尾时自动请求数据。
* 对 Kotlin 协程和数据流以及 LiveData 和 RxJava 的一流支持。
* 内置对错误处理功能的支持，包括刷新和重试功能。

我们只需要构建一个BasePagingMediator就可以为后来的RemoteMediator提供一个简单的多数据源模板
```kotlin
/**
 * @param T:Entity
 * @param R:RemoteKeys
 * @param E:ApiData
 */
@OptIn(ExperimentalPagingApi::class)
abstract class BaseRemoteMediator<T : Any, R : Any, E : Any>(
    private val articleDatabase: ArticleDatabase
) : RemoteMediator<Int, T>() {


    abstract suspend fun getApi(currentPage: Int): Result<List<E>>
    abstract fun convertToEntity(data: E): T
    abstract fun convertToRemoteKeys(data: T, prevPage: Int?, nextPage: Int?): R

    abstract suspend fun clearAll()
    abstract suspend fun insertAll(remoteKeys: List<R>, data: List<T>)

    abstract suspend fun lastUpdated(): Long

    override suspend fun initialize(): InitializeAction {
        val cacheTimeout = TimeUnit.MILLISECONDS.convert(1, TimeUnit.HOURS)
        return if (System.currentTimeMillis() - lastUpdated() <= cacheTimeout) {
            InitializeAction.SKIP_INITIAL_REFRESH
        } else {
            InitializeAction.LAUNCH_INITIAL_REFRESH
        }
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, T>
    ): MediatorResult {
        return try {
            val currentPage = when (loadType) {
                LoadType.REFRESH -> {
                    val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                    remoteKeys?.minus(1) ?: 0
                }

                LoadType.PREPEND -> {
                    val remoteKeys = getRemoteKeyForFirstItem(state)
                    val pervPage = remoteKeys ?: return MediatorResult.Success(
                        endOfPaginationReached = false
                    )
                    pervPage
                }

                LoadType.APPEND -> {
                    val remoteKeys = getRemoteKeyForLastItem(state)
                    val nextPage = remoteKeys ?: return MediatorResult.Success(
                        endOfPaginationReached = false
                    )
                    nextPage
                }
            }

            val result = getApi(currentPage)
            val endOfPaginationReached = result.getOrNull().isNullOrEmpty()
            val prevPage = if (currentPage == 0) null else currentPage - 1
            val nextPage = if (endOfPaginationReached) null else currentPage + 1
            articleDatabase.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    clearAll()
                }
                result.getOrNull()?.let {
                    val entity = it.map { data -> convertToEntity(data) }
                    val keys = entity.map { data ->
                        convertToRemoteKeys(data, prevPage, nextPage)
                    }
                    insertAll(keys, entity)
                }
            }
            MediatorResult.Success(endOfPaginationReached)
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }


    /**
     *
     *@return nextPage
     */
    abstract suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, T>): Int?

    /**
     *@return pervPage
     */
    abstract suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, T>): Int?

    /**
     *@return nextPage
     */
    abstract suspend fun getRemoteKeyForLastItem(state: PagingState<Int, T>): Int?


}
```

## 关于架构模式

### MVI

#### `MVI`架构是什么?

`MVI` 与 `MVVM` 很相似，其借鉴了前端框架的思想，更加强调数据的单向流动和唯一数据源,架构图如下所示
 ![img](https://p3-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/2ecd46797c084f08b9efc8fb5246a5db~tplv-k3u1fbpfcp-zoom-in-crop-mark:4536:0:0:0.awebp)
 其主要分为以下几部分

1. `Model`: 与`MVVM`中的`Model`不同的是，`MVI`的`Model`主要指`UI`状态（`State`）。例如页面加载状态、控件位置等都是一种`UI`状态
2. `View`: 与其他`MVX`中的`View`一致，可能是一个`Activity`或者任意`UI`承载单元。`MVI`中的`View`通过订阅`Model`的变化实现界面刷新
3. `Intent`: 此`Intent`不是`Activity`的`Intent`，用户的任何操作都被包装成`Intent`后发送给`Model`层进行数据请求

#### 单向数据流

`MVI`强调数据的单向流动，主要分为以下几步：

1. 用户操作以`Intent`的形式通知`Model`
2. `Model`基于`Intent`更新`State`
3. `View`接收到`State`变化刷新UI。

数据永远在一个环形结构中单向流动，不能反向流动：
 ![img](https://p3-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/bb3fe9361e244430bd2f69b70c7b0e75~tplv-k3u1fbpfcp-zoom-in-crop-mark:4536:0:0:0.awebp)

上面简单的介绍了下`MVI`架构，下面我们一起来看下具体是怎么使用`MVI`架构的

![img](https://p3-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/765d1d45817744728c78ceb1e2566b9a~tplv-k3u1fbpfcp-zoom-in-crop-mark:4536:0:0:0.awebp)

我们使用`ViewModel`来承载`MVI`的`Model`层，总体结构也与`MVVM`类似,主要区别在于`Model`与`View`层交互的部分

1. `Model`层承载`UI`状态，并暴露出`ViewState`供`View`订阅，`ViewState`是个`data class`,包含所有页面状态
2. `View`层通过`Action`更新`ViewState`，替代`MVVM`通过调用`ViewModel`方法交互的方式

此段关于MVI架构的解释部分引用自掘金用户:程序员江同学的[MVVM 进阶版：MVI 架构了解一下~](https://juejin.cn/post/7022624191723601928)

<!-- MARKDOWN LINKS & IMAGES -->
[Dokiwei]:https://img.shields.io/badge/Github-DokiWei-blue.svg?style=flat&logo=github&logoColor=#181717
[Dokiwei-Url]:https://github.com/Dokiwei
[Jetpack-Compose]: https://img.shields.io/badge/jetpack_compose-1.5.0_beta03-4285F4?style=for-the-badge&logo=jetpackcompose&logoColor=#4285F4
[Jetpack-Compose-Url]:https://developer.android.google.cn/jetpack/compose
[Kotlin]: https://img.shields.io/badge/Kotlin-1.8.10-7F52FF?style=for-the-badge&logo=kotlin&logoColor=#7F52FF
[Kotlin-Url]:https://kotlinlang.org/
[Gradle]: https://img.shields.io/badge/Gradle-8.2.0_alpha14-02303A?style=for-the-badge&logo=gradle&logoColor=#02303A
[Gradle-Url]:https://gradle.org/
[Room]:https://img.shields.io/badge/Room-2.5.0-000000?style=for-the-badge
[Room-url]: https://developer.android.google.cn/jetpack/androidx/releases/room
[Paging]:https://img.shields.io/badge/Paging-3.2.0-000000?style=for-the-badge
[Paging-url]: https://developer.android.google.cn/topic/libraries/architecture/paging/v3-overview
[Retrofit]:https://img.shields.io/badge/Retrofit-2.9.0-000000?style=for-the-badge
[Retrofit-url]: https://square.github.io/retrofit/

[start]:/img/start.jpg "启动页"
[start-dark]:/img/start_dark.jpg "启动页-深色"
[home]:/img/home.jpg "首页"
[home-dark]:/img/home_dark.jpg "首页-深色"
[project]:/img/project.jpg "项目"
[project-dark]:/img/project_dark.jpg "项目-深色"
[navigation]:/img/navigation.jpg "导航"
[navigation-dark]:/img/navigation_dark.jpg "导航-深色"
[account]:/img/account.jpg "账号"
[account-dark]:/img/account_dark.jpg "账号-深色"
