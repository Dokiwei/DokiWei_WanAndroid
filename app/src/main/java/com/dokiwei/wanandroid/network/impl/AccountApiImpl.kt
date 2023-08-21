package com.dokiwei.wanandroid.network.impl

import com.dokiwei.wanandroid.model.apidata.CoinCountData
import com.dokiwei.wanandroid.model.apidata.CoinInfoData
import com.dokiwei.wanandroid.model.apidata.MessageData
import com.dokiwei.wanandroid.model.apidata.UserArticleData
import com.dokiwei.wanandroid.model.apidata.UserInfoData
import com.dokiwei.wanandroid.network.client.RetrofitClient

/**
 * @author DokiWei
 * @date 2023/7/9 19:08
 */
class AccountApiImpl {
    suspend fun login(username: String, password: String): Result<Int> {
        return try {
            RetrofitClient.accountApi.login(username, password).parseJsonElement<Int>("errorCode")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun logout(): Result<Int> {
        return try {
            RetrofitClient.accountApi.logout().parseJsonElement<Int>("errorCode")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun register(
        username: String,
        password: String,
        rePassword: String
    ): Result<Int> {
        return try {
            RetrofitClient.accountApi.register(username, password, rePassword)
                .parseJsonElement<Int>("errorCode")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getUserInfo(): Result<UserInfoData> {
        return try {
            RetrofitClient.accountApi.userInfo().parseJsonData<UserInfoData>()
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getUnreadQuantity(): Result<Int> {
        return try {
            RetrofitClient.accountApi.getUnreadQuantity().parseUnreadQuantity<Int>("data")
        }catch (e:Exception){
            Result.failure(e)
        }
    }

    suspend fun getRead(page: Int): Result<List<MessageData>> {
        return try {
            RetrofitClient.accountApi.getRead(page).parseJsonDatasList()
        }catch (e:Exception){
            Result.failure(e)
        }
    }

    suspend fun getUnread(page: Int): Result<List<MessageData>> {
        return try {
            RetrofitClient.accountApi.getUnread(page).parseJsonDatasList()
        }catch (e:Exception){
            Result.failure(e)
        }
    }

    suspend fun getCoinList(page: Int):Result<List<CoinCountData>>{
        return try {
            RetrofitClient.accountApi.getCoinList(page).parseJsonDatasList()
        }catch (e:Exception){
            Result.failure(e)
        }
    }

    suspend fun getRank():Result<List<CoinInfoData>>{
        return try {
            RetrofitClient.accountApi.getRank().parseJsonDatasList()
        }catch (e:Exception){
            Result.failure(e)
        }
    }

    suspend fun getCoinInfo():Result<CoinInfoData>{
        return try {
            RetrofitClient.accountApi.coinInfo().parseJsonData()
        }catch (e:Exception){
            Result.failure(e)
        }
    }

    suspend fun getUserArticles(page:Int,userId:Int):Result<UserArticleData>{
        return try {
            RetrofitClient.accountApi.getUserArticles(page, userId).parseJsonDataAndArticles()
        }catch (e:Exception){
            Result.failure(e)
        }
    }
}
