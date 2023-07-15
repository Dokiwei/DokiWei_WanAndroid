package com.dokiwei.wanandroid.network.client

import com.dokiwei.wanandroid.network.api.ArticleListApi
import com.dokiwei.wanandroid.network.api.BannerApi
import com.dokiwei.wanandroid.network.api.LikeArticleApi
import com.dokiwei.wanandroid.network.api.LoginApi
import com.dokiwei.wanandroid.network.api.LogoutApi
import com.dokiwei.wanandroid.network.api.ProjectApi
import com.dokiwei.wanandroid.network.api.ProjectTitleApi
import com.dokiwei.wanandroid.network.api.QaApi
import com.dokiwei.wanandroid.network.api.RegisterApi
import com.dokiwei.wanandroid.network.api.UnlikeArticleApi
import com.dokiwei.wanandroid.network.api.UserArticleApi
import com.dokiwei.wanandroid.network.api.UserInfoApi
import com.dokiwei.wanandroid.ui.main.MyApplication
import com.franmontiel.persistentcookiejar.PersistentCookieJar
import com.franmontiel.persistentcookiejar.cache.SetCookieCache
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor
import okhttp3.OkHttpClient
import retrofit2.Retrofit

/**
 * @author DokiWei
 * @date 2023/7/9 19:09
 */
object RetrofitClient {
    private val cookieJar =
        PersistentCookieJar(SetCookieCache(), SharedPrefsCookiePersistor(MyApplication.context))
    private val client = OkHttpClient.Builder()
        .retryOnConnectionFailure(true)
        .addInterceptor(LoggingInterceptor())
        .cookieJar(cookieJar)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://www.wanandroid.com/")
        .client(client)
        .build()

    val loginApi: LoginApi = retrofit.create(LoginApi::class.java)
    val registerApi: RegisterApi = retrofit.create(RegisterApi::class.java)
    val logoutApi: LogoutApi = retrofit.create(LogoutApi::class.java)
    val bannerApi: BannerApi = retrofit.create(BannerApi::class.java)
    val articleListApi: ArticleListApi = retrofit.create(ArticleListApi::class.java)
    val userArticleListApi: UserArticleApi = retrofit.create(UserArticleApi::class.java)
    val likeArticleApi: LikeArticleApi = retrofit.create(LikeArticleApi::class.java)
    val unlikeArticleApi: UnlikeArticleApi = retrofit.create(UnlikeArticleApi::class.java)
    val userInfoApi: UserInfoApi = retrofit.create(UserInfoApi::class.java)
    val qaApi: QaApi = retrofit.create(QaApi::class.java)
    val projectTitleApi: ProjectTitleApi = retrofit.create(ProjectTitleApi::class.java)
    val projectApi:ProjectApi= retrofit.create(ProjectApi::class.java)

}
