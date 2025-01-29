package com.example.home.domain.value_object

import java.time.LocalDate

/**
 * トランザクションテーブル -デフォルト値
 */
object TsDefaultData {
    val GROUP_SETTING = mapOf(
        "display_yyyy" to LocalDate.now().year.toString(),
        "graph_type" to "1",
        "slack_basic_webhook_url" to "",
        "slack_basic_send_flg" to ""
    )
    val USER_SETTING = mapOf(
        "Setting1" to "0",
        "Setting2" to "1"
    )
    val CATEGORYS = mapOf(
        "1" to "分類１",
        "2" to "分類２",
        "3" to "分類３"
    )
    val MEMBERS = mapOf(
        "1" to "メンバー１",
        "2" to "メンバー２",
        "3" to "メンバー３"
    )
    val COMMENTS = mapOf(
        "199001" to "コメントを入力してください"
    )
}
