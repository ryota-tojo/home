package com.example.home.util

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

object ParseLocalDateTime {

    fun parseLocalDateTime(localDateTimeString: String?, format: String = "yyyy-MM-dd HH:mm:ss"): String {
        // "null" や null の場合は処理せず空文字を返す
        if (localDateTimeString.isNullOrBlank() || localDateTimeString == "null") {
            return ""
        }

        return try {
            // Instant.parse() のために "Z" (UTC) が必要
            val fixedDateTime = if (!localDateTimeString.endsWith("Z")) {
                localDateTimeString + "Z"
            } else {
                localDateTimeString
            }

            val instant = Instant.parse(fixedDateTime) // 例外が出る可能性がある
            val formatter = DateTimeFormatter.ofPattern(format).withZone(ZoneId.of("Asia/Tokyo"))
            formatter.format(instant)
        } catch (e: DateTimeParseException) {
            println("パースエラー: ${e.message}")
            "" // エラー時は空文字を返す
        }
    }
}