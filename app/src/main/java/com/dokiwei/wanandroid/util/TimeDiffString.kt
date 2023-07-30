package com.dokiwei.wanandroid.util

import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

/**
 * @author DokiWei
 * @date 2023/7/11 21:42
 */
object TimeDiffString {
    fun isDateString(dateString: String): Boolean {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        return try {
            LocalDateTime.parse(dateString, formatter)
            true
        } catch (e: DateTimeParseException) {
            false
        }
    }

    fun getTimeDiffString(dateString: String): String {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        val dateTime = LocalDateTime.parse(dateString, formatter)
        val now = LocalDateTime.now()
        val duration = Duration.between(dateTime, now)
        return when {
            duration.seconds < 60 -> "${duration.seconds}秒内"
            duration.toMinutes() < 60 -> "${duration.toMinutes()}分钟内"
            duration.toHours() < 24 -> "${duration.toHours()}小时内"
            duration.toDays() < 7 -> "${duration.toDays()}天前"
            duration.toDays() < 30 -> "${duration.toDays() / 7}周前"
            duration.toDays() < 365 -> "${duration.toDays() / 30}个月前"
            else -> "${duration.toDays() / 365}年前"
        }
    }

}