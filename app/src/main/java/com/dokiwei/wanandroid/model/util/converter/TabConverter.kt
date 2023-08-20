package com.dokiwei.wanandroid.model.util.converter

import androidx.room.TypeConverter
import com.dokiwei.wanandroid.model.apidata.TagData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * @author DokiWei
 * @date 2023/8/9 15:28
 */
// 定义一个类型转换器类，用于将 List<TagEntity> 转换为 String，或者反过来
class TagConverter {
    // 定义一个函数，用于将 List<TagEntity> 转换为 String
    // 这里我们使用 Gson 库来将对象序列化为 JSON 字符串
    // 您可以根据您的喜好选择其他方式
    @TypeConverter
    fun fromTagList(tagList: List<TagData>): String {
        return Gson().toJson(tagList)
    }

    // 定义一个函数，用于将 String 转换为 List<TagEntity>
    // 这里我们使用 Gson 库来将 JSON 字符串反序列化为对象
    // 您可以根据您的喜好选择其他方式
    @TypeConverter
    fun toTagList(tagString: String): List<TagData> {
        val type = object : TypeToken<List<TagData>>() {}.type
        return Gson().fromJson(tagString, type)
    }
}