<?php
if (session_status() === PHP_SESSION_NONE) {
    session_start();
}

require_once $_SERVER['DOCUMENT_ROOT'] . '/Config/config.php';
require_once $_SERVER['DOCUMENT_ROOT'] . '/Config/log_config.php';
require_once $_SERVER['DOCUMENT_ROOT'] . '/Application/Services/ApiService.php';
require_once $_SERVER['DOCUMENT_ROOT'] . '/Interfaces/Views/Partials/requireApi.php';
require_once $_SERVER['DOCUMENT_ROOT'] . '/Interfaces/Views/Partials/systems/logs/create_logs.php';
require_once $_SERVER['DOCUMENT_ROOT'] . '/Interfaces/Views/Partials/systems/screen/get_screen_id.php';

function getScreen($file_name)
{
    $screen_id = getScreenId($file_name);

    $screen_name = "※画面名取得失敗※";
    $screen_remarks = "※画面コンテンツ取得失敗※";
    $log_type = LOG_TYPE_ERROR;

    $master_screen_api_result = apiCallMasterScreenRefer($screen_id);
    $screen_data =$master_screen_api_result['data'];
    foreach ($screen_data['screen_list'] as $screen) {
        if ($screen['screen_id'] == $screen_id) {
            $screen_name = $screen['screen_name'];
            $screen_remarks = $screen['remarks'];
            $log_type = LOG_TYPE_INFO;
        }
    }

    if (isset($_SESSION['screen_name']) == false) {
        $log_content = "画面初期表示";
        $_SESSION['screen_name'] = $screen_name;
    } else {
        if ($_SESSION['screen_name'] != $screen_name) {
            $log_content = "画面初期表示";
        } else {
            $log_content = "画面更新";
        }
    }
    $_SESSION['screen_name'] = $screen_name;

    createLogs($log_type, $log_content);

    return [
        "id" => $screen_id,
        "name" => $screen_name,
        "remarks" => $screen_remarks];

}