package com.dokiwei.wanandroid.network.client

import com.dokiwei.wanandroid.model.util.Constants.BASE_URL
import com.dokiwei.wanandroid.network.api.AccountApi
import com.dokiwei.wanandroid.network.api.CollectApi
import com.dokiwei.wanandroid.network.api.HomeApi
import com.dokiwei.wanandroid.network.api.NavigationApi
import com.dokiwei.wanandroid.network.api.ProjectApi
import com.dokiwei.wanandroid.ui.main.MyApplication
import com.franmontiel.persistentcookiejar.PersistentCookieJar
import com.franmontiel.persistentcookiejar.cache.SetCookieCache
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

/**
 * @author DokiWei
 * @date 2023/7/9 19:09
 */
object RetrofitClient {
    private val cookieJar =
        PersistentCookieJar(SetCookieCache(), SharedPrefsCookiePersistor(MyApplication.context))
    private val client = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .retryOnConnectionFailure(true)
        .addInterceptor(LoggingInterceptor())
        .cookieJar(cookieJar)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .build()

    val accountApi: AccountApi = retrofit.create(AccountApi::class.java)
    val homeApi: HomeApi = retrofit.create(HomeApi::class.java)
    val collectApi: CollectApi = retrofit.create(CollectApi::class.java)
    val projectApi: ProjectApi = retrofit.create(ProjectApi::class.java)
    val navigationApi: NavigationApi = retrofit.create(NavigationApi::class.java)

}
