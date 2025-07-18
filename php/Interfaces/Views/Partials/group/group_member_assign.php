<?php
require_once $_SERVER['DOCUMENT_ROOT'] . '/config/config.php';
require_once $_SERVER['DOCUMENT_ROOT'] . '/Application/Services/ApiService.php';
require_once $_SERVER['DOCUMENT_ROOT'] . '/Interfaces/Views/Partials/requireApi.php';

function groupMemberAssign($user_id, $groups_id,$group_password){

    if($user_id == ""){
        $data = [
            "status" => "error",
            "message" => "ユーザーが選択されていません"
        ];
        return json_encode($data);
    }
    $group_refer_api_result = apiCallGroupRefer($groups_id);
    if($group_refer_api_result['status'] != "success"){
        $data = [
            "status" => "error",
            "message" => "存在しない所属グループIDです"
        ];
        return json_encode($data);
    }

    // パスワードが不一致
    $assign_group_password = $group_refer_api_result['data']['group'][0]['group_list']['group_password'];
    if($assign_group_password != $group_password){
        $data = [
            "status" => "error",
            "message" => "パスワードが一致しません"
        ];
        return json_encode($data);
    }

    $group_info_create_api_result = apiCallGroupInfoCreate($groups_id, $user_id, 0);
    if($group_info_create_api_result['status'] != "success"){
        $data = [
            "status" => "error",
            "message" => "所属グループ情報の登録に失敗しました"
        ];
        return json_encode($data);
    }

    $data = [
        "status" => "success",
        "message" => "所属グループに加入申請しました"
    ];
    return json_encode($data);
}