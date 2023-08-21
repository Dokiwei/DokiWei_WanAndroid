# DokiWei-WanAndroid

## 简介

本软件完全遵循Material Design 3的设计规范,并且在Android12+支持Dynamic colors

在架构模式上使用了MVI架构,并且严格遵循Google推荐的数据管理方式,即唯一可信数据源,单向数据流

UI完全由用compose以声明式编程的方式构建,compose可以大幅提升界面的复用性,并且在嵌套布局中几乎不会消耗性能,在可观察数据可以完美的契合kotlin的flow,省去了其他繁琐的数据状态观察方式

网络请求框架使用Retrofit2,此框架可以快速方便的构建网络请求

数据持久使用了Room数据库来存储可能需要多次访问的数据,并配合Paging3的RemoteMediator来实现从多个数据源获取数据

## 截图

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

[转自掘金用户:程序员江同学]: https://juejin.cn/post/7022624191723601928	"MVVM 进阶版：MVI 架构了解一下~"

