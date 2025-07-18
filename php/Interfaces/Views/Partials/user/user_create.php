<?php
require_once $_SERVER['DOCUMENT_ROOT'] . '/config/config.php';
require_once $_SERVER['DOCUMENT_ROOT'] . '/Application/Services/ApiService.php';
require_once $_SERVER['DOCUMENT_ROOT'] . '/Interfaces/Views/Partials/requireApi.php';

function userEntry($user_name, $password,$permission,$approval,$deleted){
    $user_create_api_result=apiCallUserCreate($user_name, $password,$permission,$approval,$deleted);
    if($user_create_api_result['status'] != "success"){
        $data = [
            "status" => "error",
            "message" => $user_create_api_result['data']['message']
        ];
        return json_encode($data);
    }
    $userId = $user_create_api_result['data']['user']['user_info']['user_id'];

    $user_setting_items = require $_SERVER['DOCUMENT_ROOT'] . '/config/InitializationConfig/user_setting_items.php';
    foreach ($user_setting_items as [$key,$value]) {
        $result = apiCallUserCreateSetting($userId,$key,$value);
        if ($result['status'] !== 'success') {
            $errors[] = "ユーザー設定 {$key} の登録に失敗しました";
        }
    }
    if (!empty($errors)) {
        return json_encode([
            'status' => 'error',
            'message' => implode("\n", $errors)
        ]);
    }

    $data = [
        "status" => "success",
        "message" => "ユーザーを登録しました",
        "user_id" => $userId
    ];
    return json_encode($data);

}