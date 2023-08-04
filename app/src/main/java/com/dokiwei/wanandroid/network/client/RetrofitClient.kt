package com.dokiwei.wanandroid.network.client

import com.dokiwei.wanandroid.network.api.AccountApi
import com.dokiwei.wanandroid.network.api.CollectApi
import com.dokiwei.wanandroid.network.api.HomeApi
import com.dokiwei.wanandroid.network.api.MessageApi
import com.dokiwei.wanandroid.network.api.ProjectApi
import com.dokiwei.wanandroid.network.api.TreeApi
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
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .retryOnConnectionFailure(true)
        .addInterceptor(LoggingInterceptor())
        .cookieJar(cookieJar)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://www.wanandroid.com/")
        .client(client)
        .build()

    val accountApi: AccountApi = retrofit.create(AccountApi::class.java)
    val homeApi: HomeApi = retrofit.create(HomeApi::class.java)
    val collectApi: CollectApi = retrofit.create(CollectApi::class.java)
    val projectApi: ProjectApi = retrofit.create(ProjectApi::class.java)
    val messageApi: MessageApi = retrofit.create(MessageApi::class.java)
    val treeApi: TreeApi = retrofit.create(TreeApi::class.java)

}
