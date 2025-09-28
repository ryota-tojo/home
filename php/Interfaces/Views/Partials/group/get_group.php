<?php
if (session_status() === PHP_SESSION_NONE) {
    session_start();
}

require_once $_SERVER['DOCUMENT_ROOT'] . '/Config/config.php';
require_once $_SERVER['DOCUMENT_ROOT'] . '/Config/log_config.php';
require_once $_SERVER['DOCUMENT_ROOT'] . '/Config/ui_items_config.php';
require_once $_SERVER['DOCUMENT_ROOT'] . '/Application/Services/ApiService.php';
require_once $_SERVER['DOCUMENT_ROOT'] . '/Interfaces/Views/Partials/requireApi.php';

function isExistGroupsId($groups_id){
    $result = apiCallGroupRefer($groups_id);
    if ($result['status'] == "success") {
        return true;
    }
    return false;
}

function getGroup($groups_id){

    $group_refer_result = apiCallGroupRefer($groups_id);
    if ($group_refer_result['status'] == "error") {
        $msg = UI_ITEM_GROUP . " - 取得：取得処理失敗 APIエラー groups_id={$groups_id}";
        createLogs(LOG_TYPE_ERROR, $msg);
        return json_encode([
            "status" => "error",
            "message" => UI_ITEM_GROUP . "の取得に失敗しました"
        ]);
    }
    $group_info_refer_result = apiCallGroupInfoRefer($groups_id);
    if ($group_info_refer_result['status'] == "error") {
        $msg = UI_ITEM_GROUP_INFO . " - 取得：取得処理失敗 APIエラー groups_id={$groups_id}";
        createLogs(LOG_TYPE_ERROR, $msg);
        return json_encode([
            "status" => "error",
            "message" => UI_ITEM_GROUP_INFO . "の取得に失敗しました"
        ]);
    }

    $group_name = "";
    $group_password = "";
    $setting_display_year = "";
    $setting_graph_type = "";
    $setting_notification_send_flg = "";
    $setting_notification_url = "";
    $setting_notification_token = "";

    foreach ($group_refer_result['data']['group'] as $group) {

        $group_name = $group['group_list']['group_name'];
        $group_password = $group['group_list']['group_password'];

        $group_settings = [];
        foreach ($group['group_setting'] as $setting) {
            $group_settings[$setting['setting_key']] = $setting['setting_value'];
        }
        $setting_display_year = $group_settings['display_year'] ?? null;
        $setting_graph_type = $group_settings['graph_type'] ?? null;
        $setting_notification_send_flg = $group_settings['notification_send_flg'] ?? null;
        $setting_notification_url = $group_settings['notification_url'] ?? null;
        $setting_notification_token = $group_settings['notification_token'] ?? null;
    }

    $group_info = [
        "group_name" => $group_name ?? "",
        "group_password" => $group_password ?? ""
    ];
    $group_setting = [
        "display_year" => $setting_display_year ?? "",
        "graph_type" => $setting_graph_type ?? "",
        "notification_send_flg" => $setting_notification_send_flg ?? "",
        "notification_url" => $setting_notification_url ?? "",
        "notification_token" => $setting_notification_token ?? "",
    ];
    $data = [
        "group_info" => $group_info,
        "group_setting" => $group_setting
    ];

    $msg = UI_ITEM_GROUP . " - 取得：取得処理成功 groups_id={$groups_id}";
    createLogs(LOG_TYPE_INFO, $msg);
    return json_encode([
        "status" => "success",
        "message" => UI_ITEM_GROUP . "を取得しました",
        "data" => $data
    ]);
}