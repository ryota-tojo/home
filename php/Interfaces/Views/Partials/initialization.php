<?php
require_once $_SERVER['DOCUMENT_ROOT'] . '/Config/config.php';
require_once $_SERVER['DOCUMENT_ROOT'] . '/Config/log_config.php';
require_once $_SERVER['DOCUMENT_ROOT'] . '/Application/Services/ApiService.php';
require_once $_SERVER['DOCUMENT_ROOT'] . '/Interfaces/Views/Partials/requireApi.php';
require_once $_SERVER['DOCUMENT_ROOT'] . '/Interfaces/Views/Partials/user/create_init_user.php';

function initialization(){

    $_SESSION['screen_name'] = "初期化";

    $master_setting_refer_result = apiCallMasterSettingRefer();
    if($master_setting_refer_result['status'] == "error"){

        createLogs(LOG_TYPE_INFO, "初期データ作成");

        // 設定マスタ
        $master_setting_items = require $_SERVER['DOCUMENT_ROOT'] . '/Config/InitializationConfig/master_setting_items.php';
        foreach ($master_setting_items as [$key, $value, $description]) {
            apiCallMasterSettingCreate($key, $value, $description);
        }

        // 選択肢マスタ
        $master_choices_items = require $_SERVER['DOCUMENT_ROOT'] . '/Config/InitializationConfig/master_choices_items.php';
        foreach ($master_choices_items as [$type, $no, $name_pc, $name_sp]) {
            apiCallMasterChoicesCreate($type, $no, $name_pc, $name_sp);
        }

        // 画面マスタ
        $screen_items = require $_SERVER['DOCUMENT_ROOT'] . '/Config/InitializationConfig/screen_items.php';
        foreach ($screen_items as [$id, $name, $remarks]) {
            apiCallMasterScreenCreate($id, $name, $remarks);
        }

        // お知らせ
        $notice_items = require $_SERVER['DOCUMENT_ROOT'] . '/Config/InitializationConfig/notice_items.php';
        foreach ($notice_items as [$title, $content]) {
            apiCallNoticeCreate($title, $content);
        }

        // adminユーザー
        $admin_user_items = require $_SERVER['DOCUMENT_ROOT'] . '/Config/InitializationConfig/admin_user_items.php';
        createInitUser($admin_user_items);

        // テストデータ
        require_once $_SERVER['DOCUMENT_ROOT'] . '/Interfaces/Views/Partials/test/create_test_data.php';
        create();
    }else{
        createLogs(LOG_TYPE_INFO, "初期データ作成スキップ");
    }
}