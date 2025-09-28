<?php
require_once $_SERVER['DOCUMENT_ROOT'] . '/Config/config.php';
require_once $_SERVER['DOCUMENT_ROOT'] . '/Config/ui_items_config.php';
require_once $_SERVER['DOCUMENT_ROOT'] . '/Application/Services/ApiService.php';
require_once $_SERVER['DOCUMENT_ROOT'] . '/Interfaces/Views/Partials/requireApi.php';

function entryUser($user_name, $password,$permission,$approval,$deleted){
    $user_create_api_result=apiCallUserCreate($user_name, $password,$permission,$approval,$deleted);
    if($user_create_api_result['status'] != "success"){
        $data = [
            "status" => "error",
            "message" => $user_create_api_result['data']['message']
        ];
        createLogs(LOG_TYPE_ERROR, UI_ITEM_USER . "登録 - " . UI_ITEM_USER . "登録：失敗");
        return json_encode($data);
    }
    $userId = $user_create_api_result['data']['user']['user_info']['user_id'];

    $user_setting_items = require $_SERVER['DOCUMENT_ROOT'] . '/Config/InitializationConfig/user_setting_items.php';
    foreach ($user_setting_items as [$key,$value]) {
        $result = apiCallUserCreateSetting($userId,$key,$value);
        if ($result['status'] !== 'success') {
            $errors[] = UI_ITEM_USER . "設定 {$key} の登録に失敗しました";
        }
    }
    if (!empty($errors)) {
        createLogs(LOG_TYPE_ERROR, UI_ITEM_USER . "設定登録 - " . UI_ITEM_USER . "設定登録：失敗");
        return json_encode([
            'status' => 'error',
            'message' => implode("\n", $errors)
        ]);
    }

    $data = [
        "status" => "success",
        "message" => UI_ITEM_USER . "を登録しました",
        "user_id" => $userId
    ];

    createLogs(LOG_TYPE_INFO, UI_ITEM_USER . "登録 - " . UI_ITEM_USER . "登録：成功");
    return json_encode($data);

}