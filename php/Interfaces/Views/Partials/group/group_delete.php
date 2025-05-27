<?php
require_once $_SERVER['DOCUMENT_ROOT'] . '/config/config.php';
require_once $_SERVER['DOCUMENT_ROOT'] . '/Application/Services/api_service.php';
require_once $_SERVER['DOCUMENT_ROOT'] . '/Interfaces/Views/Partials/requireApi.php';

function groupDelete($groups_id){

    $group_info_delete_api_result = apiCallGroupInfoDelete($groups_id);
    if($group_info_delete_api_result['status'] == "error"){
        $data = [
            "status" => "error",
            "message" => "所属グループ情報の削除に失敗しました。"
        ];
        return json_encode($data);
    }

    $group_delete_api_result = apiCallGroupDelete($groups_id);
    if($group_delete_api_result['status'] != "success"){
        $data = [
            "status" => "error",
            "message" => "所属グループの削除に失敗しました"
        ];
        return json_encode($data);
    }

    $data = [
        "status" => "success",
        "message" => "所属グループを登録しました"
    ];
    return json_encode($data);
}