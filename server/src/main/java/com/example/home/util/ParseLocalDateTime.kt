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
            // "Z" があって時間がない場合 → 時間補完
            val fixedDateTime = when {
                localDateTimeString.matches(Regex("""\d{4}-\d{2}-\d{2}Z""")) -> {
                    localDateTimeString.replace("Z", "T00:00:00Z")
                }

                !localDateTimeString.endsWith("Z") -> {
                    localDateTimeString + "Z"
                }

                else -> localDateTimeString
            }

            val instant = Instant.parse(fixedDateTime)
            val formatter = DateTimeFormatter.ofPattern(format).withZone(ZoneId.of("Asia/Tokyo"))
            formatter.format(instant)
        } catch (e: DateTimeParseException) {
            println("パースエラー: ${e.message}")
            ""
        }
    }

}