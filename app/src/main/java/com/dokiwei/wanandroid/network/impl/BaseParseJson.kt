package com.dokiwei.wanandroid.network.impl

import com.dokiwei.wanandroid.model.util.ToastAndLogcatUtil
import com.dokiwei.wanandroid.ui.main.MyApplication
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import okhttp3.ResponseBody
import org.json.JSONObject

/**
 * @author DokiWei
 * @date 2023/8/6 17:13
 */
inline fun <reified T> ResponseBody.parseJsonDatasList(): Result<List<T>> {
    val json = Json { ignoreUnknownKeys = true }
    return try {
        val responseBody = this.string()
        val jsonElement = json.parseToJsonElement(responseBody).jsonObject
        val errorCode = jsonElement["errorCode"]?.jsonPrimitive?.int ?: -1
        if (errorCode == 0) {
            val dataJson = jsonElement["data"]?.jsonObject?.get("datas")?.jsonArray
                ?: error("Missing data field")
            val data = json.decodeFromJsonElement<List<T>>(dataJson)
            Result.success(data)
        } else {
            val errorMsg = jsonElement["errorMsg"]?.jsonPrimitive?.content ?: "获取错误信息失败"
            errorResultToLog(errorCode,errorMsg)
            Result.failure(Exception("Error code: $errorCode \nError Msg: $errorMsg"))
        }
    } catch (e: Exception) {
        Result.failure(e)
    }
}

inline fun <reified T> ResponseBody.parseJsonDataList(): Result<List<T>> {
    val json = Json { ignoreUnknownKeys = true }
    return try {
        val responseBody = this.string()
        val jsonElement = json.parseToJsonElement(responseBody).jsonObject
        val errorCode = jsonElement["errorCode"]?.jsonPrimitive?.int ?: -1
        if (errorCode == 0) {
            val dataJson = jsonElement["data"]?.jsonArray ?: error("Missing data field")
            val data = json.decodeFromJsonElement<List<T>>(dataJson)
            Result.success(data)
        } else {
            val errorMsg = jsonElement["errorMsg"]?.jsonPrimitive?.content ?: "获取错误信息失败"
            errorResultToLog(errorCode,errorMsg)
            Result.failure(Exception("Error code: $errorCode \nError Msg: $errorMsg"))
        }
    } catch (e: Exception) {
        Result.failure(e)
    }
}

inline fun <reified T> ResponseBody.parseJsonData(): Result<T> {
    val json = Json { ignoreUnknownKeys = true }
    return try {
        val responseBody = this.string()
        val jsonElement = json.parseToJsonElement(responseBody).jsonObject
        val errorCode = jsonElement["errorCode"]?.jsonPrimitive?.int ?: -1
        if (errorCode == 0) {
            val dataJson = jsonElement["data"]?.jsonObject ?: error("Missing data field")
            val data = json.decodeFromJsonElement<T>(dataJson)
            Result.success(data)
        } else {
            val errorMsg = jsonElement["errorMsg"]?.jsonPrimitive?.content ?: "获取错误信息失败"
            errorResultToLog(errorCode,errorMsg)
            Result.failure(Exception("Error code: $errorCode \nError Msg: $errorMsg"))
        }
    } catch (e: Exception) {
        Result.failure(e)
    }
}

inline fun <reified T> ResponseBody.parseJsonElement(key: String): Result<T> {
    return try {
        val responseBody = this.string()
        val jsonObject = JSONObject(responseBody)
        val errorCode = jsonObject.getInt("errorCode")
        if (errorCode == 0) {
            val data = when (T::class) {
                String::class -> jsonObject.getString(key) as T
                Int::class -> jsonObject.getInt(key) as T
                Boolean::class -> jsonObject.getBoolean(key) as T
                else -> return Result.failure(Exception("不支持的类型: ${T::class}"))
            }
            Result.success(data)
        } else {
            val errorMsg = jsonObject.getString("errorMsg")
            errorResultToLog(errorCode,errorMsg)
            Result.failure(Exception("Error code: $errorCode \nError Msg: $errorMsg"))
        }
    } catch (e: Exception) {
        Result.failure(e)
    }
}

inline fun <reified T> ResponseBody.parseUnreadQuantity(key: String): Result<T> {
    return try {
        val responseBody = this.string()
        val jsonObject = JSONObject(responseBody)
        val errorCode = jsonObject.getInt("errorCode")
        if (errorCode == 0) {
            val data = when (T::class) {
                String::class -> jsonObject.getString(key) as T
                Int::class -> jsonObject.getInt(key) as T
                Boolean::class -> jsonObject.getBoolean(key) as T
                else -> return Result.failure(Exception("不支持的类型: ${T::class}"))
            }
            Result.success(data)
        } else {
            val errorMsg = jsonObject.getString("errorMsg")
            Result.failure(Exception("Error code: $errorCode \nError Msg: $errorMsg"))
        }
    } catch (e: Exception) {
        Result.failure(e)
    }
}

fun errorResultToLog(errorCode:Int,errorMsg:String){
    when (errorCode) {
        -1001 -> {
            MyApplication.isShowLoginTip.value = true
            ToastAndLogcatUtil.log(tag = "API请求结果", msg = "用户未登录,需要进行登录")
        }
        else -> {
            ToastAndLogcatUtil.log(
                tag = "API请求结果",
                msg = "请求失败:errorCode=$errorCode  errorMsg=$errorMsg"
            )
        }
    }
}
