<?php
require_once $_SERVER['DOCUMENT_ROOT'] . '/config/config.php';
require_once $_SERVER['DOCUMENT_ROOT'] . '/Application/Services/ApiService.php';
require_once $_SERVER['DOCUMENT_ROOT'] . '/Interfaces/Views/Partials/requireApi.php';

function groupEntry($user_id, $groups_id, $group_name, $group_password){

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

    $data = [
        "status" => "success",
        "message" => "所属グループを登録しました"
    ];
    return json_encode($data);
}