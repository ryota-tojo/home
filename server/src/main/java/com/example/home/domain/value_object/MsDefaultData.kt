package com.example.home.domain.value_object

import java.time.LocalDate

/**
 * マスターテーブル -デフォルト値
 * (未使用)
 */
object MsDefaultData {
    val MASTER_SETTING = mapOf(
        "account_lockout_count" to "5" to "",
        "maintenance" to "0" to "",
        "slack_report_send_flg" to "0" to "",
        "slack_report_webhook_url" to "" to "",
        "slack_send_test" to "0" to "",
        "layout" to "0" to "",
        "admin_userdata_view" to "10" to "",
        "admin_notice_view" to "5" to "",
        "admin_notice_initial_title" to "お知らせ" to "",
        "admin_notice_initial_content" to "XXXXX" to "",
        "user_input_history_view" to "10" to "",
        "user_management_view" to "20" to "",
        "user_analysis_graph_size_pc_width" to "750" to "",
        "user_analysis_graph_size_pc_height" to "200" to "",
        "user_analysis_graph_size_sp_width" to "320" to "",
        "user_analysis_graph_size_sp_height" to "200" to "",
        "user_analysis_graph_size_tb_width" to "680" to "",
        "user_analysis_graph_size_tb_height" to "200" to "",
        "user_communication_histry_view" to "10" to "",
        "user_communication_list_view" to "20" to "",
        "user_communication_view_conditions" to "0" to ""
    )

    val CHOICES = mapOf(
        "type" to 0 to "収入",
        "type" to 1 to "支出",
        "payment" to 0 to "現金",
        "payment" to 1 to "カード",
        "payment" to 2 to "引落し",
        "payment" to 3 to "振込み",
        "settlement_pc" to 0 to "未精算",
        "settlement_pc" to 1 to "精算済",
        "settlement_sp" to 0 to "未",
        "settlement_sp" to 1 to "済"
    )
}
