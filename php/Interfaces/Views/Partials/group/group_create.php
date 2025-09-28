<?php
require_once $_SERVER['DOCUMENT_ROOT'] . '/Config/config.php';
require_once $_SERVER['DOCUMENT_ROOT'] . '/Application/Services/ApiService.php';
require_once $_SERVER['DOCUMENT_ROOT'] . '/Interfaces/Views/Partials/requireApi.php';

function groupCreate($user_id, $groups_id, $group_name, $group_password){

    if($user_id == ""){
        $data = [
            "status" => "error",
            "message" => "ユーザーが選択されていません"
        ];
        return json_encode($data);
    }
    $group_refer_api_result = apiCallGroupRefer($groups_id);
    if($group_refer_api_result['status'] == "success"){
        $data = [
            "status" => "error",
            "message" => "既に登録済みの所属グループIDです"
        ];
        return json_encode($data);
    }

    $group_create_api_result = apiCallGroupCreate($groups_id, $group_name, $group_password);
    if($group_create_api_result['status'] != "success"){
        $data = [
            "status" => "error",
            "message" => "所属グループIDの登録に失敗しました"
        ];
        return json_encode($data);
    }

    $group_info_create_api_result = apiCallGroupInfoCreate($groups_id, $user_id, 1);
    if($group_info_create_api_result['status'] != "success"){
        $data = [
            "status" => "error",
            "message" => "所属グループ情報の登録に失敗しました"
        ];
        return json_encode($data);
    }

    $group_setting_items = require $_SERVER['DOCUMENT_ROOT'] . '/Config/InitializationConfig/group_setting_items.php';
    foreach ($group_setting_items as [$key,$value]) {
        $result = apiCallGroupCreateSetting($groups_id,$key,$value);
        if ($result['status'] !== 'success') {
            $errors[] = "設定 {$key} の登録に失敗しました";
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
        "message" => "所属グループを登録しました"
    ];
    return json_encode($data);
}