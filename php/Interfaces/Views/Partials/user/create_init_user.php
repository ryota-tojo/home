<?php
require_once $_SERVER['DOCUMENT_ROOT'] . '/Config/config.php';
require_once $_SERVER['DOCUMENT_ROOT'] . '/Application/Services/ApiService.php';
require_once $_SERVER['DOCUMENT_ROOT'] . '/Interfaces/Views/Partials/requireApi.php';
require_once $_SERVER['DOCUMENT_ROOT'] . '/Interfaces/Views/Partials/user/create_user.php';
require_once $_SERVER['DOCUMENT_ROOT'] . '/Interfaces/Views/Partials/group/group_create.php';
require_once $_SERVER['DOCUMENT_ROOT'] . '/Interfaces/Views/Partials/group/group_member_assign.php';

function createInitUser($user_items)
{
    foreach ($user_items as [$user_name, $password, $permission, $approval, $deleted,$groups_id,$group_name,$group_password,$group_approval]) {
        // ユーザーを作成してIDを取得
        $user_entry_result = entryUser($user_name, $password, $permission, $approval, $deleted);
        $user_data = json_decode($user_entry_result, true);
        $user_id = $user_data['user_id'] ?? null;

        // グループを参照
        if($groups_id != ""){
            $group_create_result = groupCreate($user_id, $groups_id, $group_name, $group_password);
            $group_data = json_decode($group_create_result, true);
            $group_create_status = $group_data['status'];
            $group_create_message = $group_data['message'];

            if ($group_create_status == "error" and $group_create_message == "既に登録済みの所属グループIDです") {
                // グループが存在していたらメンバーをアサイン
                groupMemberAssign($user_id, $groups_id, $group_password);

                // 承認の場合はグループ情報を更新
                if($group_approval == 1){
                    apiCallGroupInfoUpdate($groups_id, $user_id, null, 1);
                }
            }
        }
    }
}