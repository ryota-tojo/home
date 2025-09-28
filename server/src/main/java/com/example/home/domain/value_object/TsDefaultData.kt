package com.example.home.domain.value_object

import java.time.LocalDate

/**
 * トランザクションテーブル -デフォルト値
 */
object TsDefaultData {
    val GROUP_SETTING = mapOf(
        "display_year" to LocalDate.now().year.toString(),
        "graph_type" to "1",
        "notification_send_flg" to "0",
        "notification_url" to "https://default",
        "notification_token" to ""
    )
    val USER_SETTING = mapOf(
        "Setting1" to "0",
        "Setting2" to "1"
    )
    val CATEGORIES = mapOf(
        "1" to "カテゴリー１",
        "2" to "カテゴリー２",
        "3" to "カテゴリー３"
    )
    val MEMBERS = mapOf(
        "1" to "購入者１",
        "2" to "購入者２",
        "3" to "購入者３"
    )
    val COMMENTS = mapOf(
        "199001" to "コメントを入力してください"
    )
}
