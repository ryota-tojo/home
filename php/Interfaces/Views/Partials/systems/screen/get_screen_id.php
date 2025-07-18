<?php
require_once $_SERVER['DOCUMENT_ROOT'] . '/config/config.php';
require_once $_SERVER['DOCUMENT_ROOT'] . '/config/log_config.php';
require_once $_SERVER['DOCUMENT_ROOT'] . '/Application/Services/ApiService.php';
require_once $_SERVER['DOCUMENT_ROOT'] . '/Interfaces/Views/Partials/requireApi.php';

function getScreenId($file_name)
{
    switch ($file_name) {
        case 'initialization.php':
            return 'BSC-NML000';

        case 'login.php':
            return 'BSC-NML001';

        case 'user_entry.php':
            return 'BSC-NML002';

        case 'home.php':
            return 'BSC-NML003';

        case 'logout.php':
            return 'BSC-NML004';

        case 'maintenance.php':
            return 'BSC-NML005';

        case 'session_list.php':
            return 'BSC-NML101';

        case 'access_error.php':
            return 'BSC-ERR001';

        // 管理画面 - マスタ設定
        case 'admin_master_setting_system.php':
            return 'ADM-MST001';

        case 'admin_master_setting_send.php':
            return 'ADM-MST002';

        // 管理画面 - データ管理 - ユーザー
        case 'admin_user_control.php':
            return 'ADM-USR001';

        case 'admin_user_entry.php':
            return 'ADM-USR002';

        case 'admin_user_update.php':
            return 'ADM-USR003';

        case 'admin_user_setting_all_apply.php':
            return 'ADM-USR004';

        // 管理画面 - データ管理 - 所属グループ
        case 'admin_group_control.php':
            return 'ADM-GRP001';

        case 'admin_group_entry.php':
            return 'ADM-GRP002';

        case 'admin_group_update.php':
            return 'ADM-GRP003';

        case 'admin_group_assign.php':
            return 'ADM-GRP004';

        case 'admin_group_leader_change.php':
            return 'ADM-GRP005';

        case 'admin_group_setting_all_apply.php':
            return 'ADM-GRP101';

        // 管理画面 - 運用管理
        case 'admin_notice_entry.php':
            return 'ADM-OPR001';

        case 'admin_notice_update.php':
            return 'ADM-OPR002';

        case 'admin_maintenance.php':
            return 'ADM-OPR003';

        // 管理画面 - データベース管理
        case 'admin_db_backup.php':
            return 'ADM-DBM001';

        case 'admin_db_initialize.php':
            return 'ADM-DBM002';

        // ユーザー画面 - 所属グループ設定
        case 'user_group_entry.php':
            return 'USR-GRP001';

        case 'user_group_application.php':
            return 'USR-GRP002';


        case 'user_group_config.php':
            return 'USR-GRP110';


        case 'user_group_user_list.php':
            return 'USR-GRP120';

        case 'user_group_user_entry.php':
            return 'USR-GRP121';

        case 'user_group_user_update.php':
            return 'USR-GRP122';

        case 'user_group_change_leader.php':
            return 'USR-GRP123';


        case 'user_group_category_list.php':
            return 'USR-GRP130';

        case 'user_group_category_entry.php':
            return 'USR-GRP131';

        case 'user_group_category_update.php':
            return 'USR-GRP132';

        case 'user_group_member_list.php':
            return 'USR-GRP140';

        case 'user_group_member_entry.php':
            return 'USR-GRP141';

        case 'user_group_member_update.php':
            return 'USR-GRP142';

        case 'user_group_template_register_list.php':
            return 'USR-GRP150';

        case 'user_group_template_register_entry.php':
            return 'USR-GRP151';

        case 'user_group_template_register_update.php':
            return 'USR-GRP152';


        case 'user_group_template_input_list.php':
            return 'USR-GRP160';

        case 'user_group_template_input_entry.php':
            return 'USR-GRP161';

        case 'user_group_template_input_update.php':
            return 'USR-GRP162';


        case 'user_group_template_search_list.php':
            return 'USR-GRP170';

        case 'user_group_template_search_entry.php':
            return 'USR-GRP171';

        case 'user_group_template_search_update.php':
            return 'USR-GRP172';

        // ユーザー画面 - 予算
        case 'budget_input.php':
            return 'USR-BGT001';

        case 'budget_list.php':
            return 'USR-BGT002';

        // ユーザー画面 - データ入力
        case 'template_input.php':
            return 'USR-IPT001';

        case 'purchase_input.php':
            return 'USR-IPT002';

        case 'purchase_update.php':
            return 'USR-IPT003';

        // ユーザー画面 - データ管理
        case 'purchase_list.php':
            return 'USR-MNG001';

        case 'data_confirm.php':
            return 'USR-MNG002';

        case 'comment_list.php':
            return 'USR-MNG003';

        case 'comment_update.php':
            return 'USR-MNG004';

        // ユーザー画面 - データ分析
        case 'analysis_budget_vs_actual.php':
            return 'USR-ANA001';

        case 'analysis_year_comparison.php':
            return 'USR-ANA002';

        // ユーザー画面 - お付き合い帳
        case 'relation_list.php':
            return 'USR-COM001';

        case 'relation_entry.php':
            return 'USR-COM002';

        case 'relation_update.php':
            return 'USR-COM003';

        // ユーザー画面 - その他
        case 'user_settings.php':
            return 'USR-ETC001';

        // ユーザー画面 - その他
        case 'test.php':
            return 'BSC-TST001';

        default:
            return '';
    }
}
