<?php
if (session_status() === PHP_SESSION_NONE) {
    session_start();
}

require_once $_SERVER['DOCUMENT_ROOT'] . '/Config/config.php';
require_once $_SERVER['DOCUMENT_ROOT'] . '/Config/log_config.php';
require_once $_SERVER['DOCUMENT_ROOT'] . '/Application/Services/ApiService.php';
require_once $_SERVER['DOCUMENT_ROOT'] . '/Interfaces/Views/Partials/requireApi.php';

function createLogs($log_type, $content)
{

    if ($_SESSION['output_logs'] != 1) {
        return false;
    }

    // ログのフルパスを作成
    $output_path = $_SERVER['DOCUMENT_ROOT'] . SYSTEM_LOGS_CONFIG['OUTPUT_PATH'];
    $output_log_file_name = SYSTEM_LOGS_CONFIG['OUTPUT_LOG_FILE_NAME'];
    $output_error_log_file_name = SYSTEM_LOGS_CONFIG['OUTPUT_ERROR_LOG_FILE_NAME'];
    $time_stamp = date(SYSTEM_LOGS_CONFIG['TIME_STAMP']);
    $log_file = $output_path . sprintf($output_log_file_name, $time_stamp);
    $error_log_file = $output_path . sprintf($output_error_log_file_name, $time_stamp);

    // ログの内容を作成
    $now_datetime = date('Y-m-d H:i:s');
    $user_id = $_SESSION['user_id'] ?? "-";
    $user_name = $_SESSION['user_name'] ?? "-";
    $screen = $_SESSION['screen_name'] ?? "";
    $log_content = "$now_datetime [$log_type] {$screen}画面 $user_id $user_name $content";

    // ログファイルを作成
    if (!file_exists($log_file)) {
        $handle = fopen($log_file, 'w');
        if ($handle) {
            fclose($handle);
        }
    }

    // ログを出力
    if($log_type != LOG_TYPE_ERROR){
        file_put_contents($log_file, "{$log_content}\n",FILE_APPEND);
    }else{
        file_put_contents($log_file, "{$log_content}\n",FILE_APPEND);
        file_put_contents($error_log_file, "{$log_content}\n",FILE_APPEND);
    }

    return true;
}