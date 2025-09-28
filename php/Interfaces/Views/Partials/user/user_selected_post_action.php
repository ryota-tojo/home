<?php

if (session_status() === PHP_SESSION_NONE) {
    session_start();
}

require_once $_SERVER['DOCUMENT_ROOT'] . '/Config/config.php';
require_once $_SERVER['DOCUMENT_ROOT'] . '/Config/log_config.php';
require_once $_SERVER['DOCUMENT_ROOT'] . '/Config/ui_items_config.php';
require_once $_SERVER['DOCUMENT_ROOT'] . '/Application/Services/ApiService.php';
require_once $_SERVER['DOCUMENT_ROOT'] . '/Interfaces/Views/Partials/requireApi.php';
require_once $_SERVER['DOCUMENT_ROOT'] . '/Interfaces/Views/Partials/user/get_user.php';
require_once $_SERVER['DOCUMENT_ROOT'] . '/Interfaces/Views/Partials/user/create_user.php';


function groupUserPostActionForGroupApprovalEvent($groups_id, $selected_data_list)
{

    if ($groups_id == null) {
        $msg = "Exception - " . UI_ITEM_GROUP_INFO . " - 承認アクション：" . UI_ITEM_GROUPS_ID . "がnullです";
        createLogs(LOG_TYPE_ERROR, $msg);
        throw new Exception($msg);
    }

    if ($selected_data_list == null) {
        $msg = "Exception - " . UI_ITEM_GROUP_INFO . " - 承認アクション：" . "選択リストがnullです";
        createLogs(LOG_TYPE_ERROR, $msg);
        throw new Exception($msg);
    }

    if ($_SERVER['REQUEST_METHOD'] !== 'POST') {
        $msg = UI_ITEM_GROUP_INFO . " - 承認アクション：POSTメソッドではないため、実行されませんでした。 Method = " . $_SERVER['REQUEST_METHOD'];
        createLogs(LOG_TYPE_INFO, $msg);
        return json_encode([
            'status' => 'skip',
            'message' => $msg
        ]);
    }

    $suc_cnt = 0;
    $err_cnt = 0;
    $cnt = 0;

    foreach ($selected_data_list as $data) {

        $array = explode("\t", $data);
        $post_user_id = $array[0];
        $post_permission = $array[1];
        $post_approval = $array[2];
        $post_deleted = $array[3];
        $post_leader_flg = $array[4];
        $post_group_approval = $array[5];

        if ($post_permission != "0") {
            $err_cnt += 1;
            continue;
        }
        if ($post_leader_flg == "1") {
            $err_cnt += 1;
            continue;
        }
        if ($post_group_approval == "1") {
            $err_cnt += 1;
            continue;
        }
        $result = apiCallGroupInfoUpdate($groups_id, $post_user_id, null, 1);
        if ($result['status'] == "error") {
            $err_cnt++;
            $msg = UI_ITEM_GROUP_INFO . " - 承認アクション：更新処理失敗 user_id=" . $post_user_id;
            createLogs(LOG_TYPE_INFO, $msg);
            continue;
        }
        $suc_cnt++;
    }

    $msg = $suc_cnt . "件の" . UI_ITEM_ENTRY_TEMPLATE . "を承認ました<br>" . $err_cnt . "件の" . UI_ITEM_ENTRY_TEMPLATE . "をスキップしました";
    createLogs(LOG_TYPE_INFO, $msg);
    return json_encode([
        'status' => 'success',
        'message' => $msg
    ]);
}

function groupUserPostActionForGroupDisapprovalEvent($groups_id, $selected_data_list)
{

    if ($groups_id == null) {
        $msg = "Exception - " . UI_ITEM_GROUP_INFO . " - 否認アクション：" . UI_ITEM_GROUP_INFO . "がnullです";
        createLogs(LOG_TYPE_ERROR, $msg);
        throw new Exception($msg);
    }

    if ($selected_data_list == null) {
        $msg = "Exception - " . UI_ITEM_GROUP_INFO . " - 否認アクション：" . "選択リストがnullです";
        createLogs(LOG_TYPE_ERROR, $msg);
        throw new Exception($msg);
    }

    if ($_SERVER['REQUEST_METHOD'] !== 'POST') {
        $msg = UI_ITEM_GROUP_INFO . " - 否認アクション：POSTメソッドではないため、実行されませんでした。 Method = " . $_SERVER['REQUEST_METHOD'];
        createLogs(LOG_TYPE_INFO, $msg);
        return json_encode([
            'status' => 'skip',
            'message' => $msg
        ]);
    }

    $suc_cnt = 0;
    $err_cnt = 0;
    $cnt = 0;

    foreach ($selected_data_list as $data) {

        $array = explode("\t", $data);
        $post_user_id = $array[0];
        $post_permission = $array[1];
        $post_approval = $array[2];
        $post_deleted = $array[3];
        $post_leader_flg = $array[4];
        $post_group_approval = $array[5];

        if ($post_permission != "0") {
            $err_cnt += 1;
            continue;
        }
        if ($post_leader_flg == "1") {
            $err_cnt += 1;
            continue;
        }
        if ($post_group_approval == "0") {
            $err_cnt += 1;
            continue;
        }
        $result = apiCallGroupInfoUpdate($groups_id, $post_user_id, null, 0);
        if ($result['status'] == "error") {
            $err_cnt++;
            $msg = UI_ITEM_GROUP_INFO . " - 否認アクション：更新処理失敗 user_id=" . $post_user_id;
            createLogs(LOG_TYPE_INFO, $msg);
            continue;
        }
        $suc_cnt++;
    }

    $msg = $suc_cnt . "件の" . UI_ITEM_ENTRY_TEMPLATE . "を否認ました<br>" . $err_cnt . "件の" . UI_ITEM_ENTRY_TEMPLATE . "をスキップしました";
    createLogs(LOG_TYPE_INFO, $msg);
    return json_encode([
        'status' => 'success',
        'message' => $msg
    ]);
}

function groupUserPostActionForGroupDeleteEvent($groups_id, $selected_data_list)
{

    if ($groups_id == null) {
        $msg = "Exception - " . UI_ITEM_GROUP_INFO . " - 除籍アクション：" . UI_ITEM_GROUP_INFO . "がnullです";
        createLogs(LOG_TYPE_ERROR, $msg);
        throw new Exception($msg);
    }

    if ($selected_data_list == null) {
        $msg = "Exception - " . UI_ITEM_GROUP_INFO . " - 除籍アクション：" . "選択リストがnullです";
        createLogs(LOG_TYPE_ERROR, $msg);
        throw new Exception($msg);
    }

    if ($_SERVER['REQUEST_METHOD'] !== 'POST') {
        $msg = UI_ITEM_GROUP_INFO . " - 除籍アクション：POSTメソッドではないため、実行されませんでした。 Method = " . $_SERVER['REQUEST_METHOD'];
        createLogs(LOG_TYPE_INFO, $msg);
        return json_encode([
            'status' => 'skip',
            'message' => $msg
        ]);
    }

    $suc_cnt = 0;
    $err_cnt = 0;
    $cnt = 0;

    foreach ($selected_data_list as $data) {

        $array = explode("\t", $data);
        $post_user_id = $array[0];
        $post_permission = $array[1];
        $post_approval = $array[2];
        $post_deleted = $array[3];
        $post_leader_flg = $array[4];
        $post_group_approval = $array[5];

        if ($post_permission != "0") {
            $err_cnt += 1;
            continue;
        }
        if ($post_leader_flg == "1") {
            $err_cnt += 1;
            continue;
        }
        if ($post_group_approval == "1") {
            $err_cnt += 1;
            continue;
        }
        $result = apiCallGroupInfoDelete($groups_id, $post_user_id);
        if ($result['status'] == "error") {
            $err_cnt++;
            $msg = UI_ITEM_GROUP_INFO . " - 除籍アクション：削除処理失敗 user_id=" . $post_user_id;
            createLogs(LOG_TYPE_INFO, $msg);
            continue;
        }
        $suc_cnt++;
    }

    $msg = $suc_cnt . "件の" . UI_ITEM_ENTRY_TEMPLATE . "を除籍ました<br>" . $err_cnt . "件の" . UI_ITEM_ENTRY_TEMPLATE . "をスキップしました";
    createLogs(LOG_TYPE_INFO, $msg);
    return json_encode([
        'status' => 'success',
        'message' => $msg
    ]);
}

function groupUserPostActionForGroupUserEntryEvent($groups_id, $user_name, $password, $re_password)
{

    if ($groups_id == null) {
        $msg = "Exception - " . UI_ITEM_GROUP_INFO . " - 登録アクション：" . UI_ITEM_GROUPS_ID . "がnullです";
        createLogs(LOG_TYPE_ERROR, $msg);
        throw new Exception($msg);
    }

    if ($user_name == null) {
        $msg = "Exception - " . UI_ITEM_GROUP_INFO . " - 登録アクション：" . UI_ITEM_USER_NAME . "がnullです";
        createLogs(LOG_TYPE_ERROR, $msg);
        throw new Exception($msg);
    }

    if ($password == null) {
        $msg = "Exception - " . UI_ITEM_GROUP_INFO . " - 登録アクション：" . UI_ITEM_USER_PASSWORD . "がnullです";
        createLogs(LOG_TYPE_ERROR, $msg);
        throw new Exception($msg);
    }

    if ($re_password == null) {
        $msg = "Exception - " . UI_ITEM_GROUP_INFO . " - 登録アクション：" . UI_ITEM_USER_PASSWORD . "(確認用)がnullです";
        createLogs(LOG_TYPE_ERROR, $msg);
        throw new Exception($msg);
    }

    if ($_SERVER['REQUEST_METHOD'] !== 'POST') {
        $msg = UI_ITEM_GROUP_INFO . " - 登録アクション：POSTメソッドではないため、実行されませんでした。 Method = " . $_SERVER['REQUEST_METHOD'];
        createLogs(LOG_TYPE_INFO, $msg);
        return json_encode([
            'status' => 'skip',
            'message' => $msg
        ]);
    }

    if($password != $re_password){
        $msg = UI_ITEM_USER . " - 登録アクション：登録処理失敗 入力した" . UI_ITEM_USER_PASSWORD . "が一致しません groups_id={$groups_id} user_name={$user_name} password={$password} re_password={$re_password}";
        createLogs(LOG_TYPE_ERROR, $msg);
        return json_encode([
            'status' => 'error',
            'message' => UI_ITEM_USER . "の登録に失敗しました"
        ]);
    }

    $user_entry_result = entryUser($user_name, $password, 0, 0, 0);
    $user_entry_data = json_decode($user_entry_result, true);
    if($user_entry_data['status'] == 'error') {
        $msg = UI_ITEM_USER . " - 登録アクション：登録処理失敗 APIエラー groups_id={$groups_id} user_name={$user_name} password={$password} re_password={$re_password}";
        createLogs(LOG_TYPE_ERROR, $msg);
        return json_encode([
            'status' => 'error',
            'message' => UI_ITEM_USER . "の登録に失敗しました"
        ]);
    }

    $group_info_entry_result = apiCallGroupInfoCreate($groups_id, $user_entry_data['user_id'], 0);
    if($group_info_entry_result['status'] == 'error') {
        $msg = UI_ITEM_GROUP_INFO . " - 登録アクション：登録処理失敗 APIエラー groups_id={$groups_id} user_id={$user_entry_data['user_id']}";
        createLogs(LOG_TYPE_ERROR, $msg);
        return json_encode([
            'status' => 'error',
            'message' => UI_ITEM_GROUP_INFO . "の登録に失敗しました"
        ]);
    }

    $msg = UI_ITEM_USER . " - 登録アクション：登録処理成功 groups_id={$groups_id} user_id={$user_entry_data['user_id']}";
    createLogs(LOG_TYPE_ERROR, $msg);
    return json_encode([
        'status' => 'success',
        'message' => UI_ITEM_USER . "を登録しました。\n承認されるまでお待ちください。"
    ]);
}

function groupUserPostActionForGroupUserUpdateEvent($user_id, $user_name)
{

    if ($user_id == null) {
        $msg = "Exception - " . UI_ITEM_GROUP_INFO . " - 更新アクション：" . UI_ITEM_USER_ID . "がnullです";
        createLogs(LOG_TYPE_ERROR, $msg);
        throw new Exception($msg);
    }

    if ($user_name == null) {
        $msg = "Exception - " . UI_ITEM_GROUP_INFO . " - 更新アクション：" . UI_ITEM_GROUP_NAME . "がnullです";
        createLogs(LOG_TYPE_ERROR, $msg);
        throw new Exception($msg);
    }

    if ($_SERVER['REQUEST_METHOD'] !== 'POST') {
        $msg = UI_ITEM_GROUP_INFO . " - 更新アクション：POSTメソッドではないため、実行されませんでした。 Method = " . $_SERVER['REQUEST_METHOD'];
        createLogs(LOG_TYPE_INFO, $msg);
        return json_encode([
            'status' => 'skip',
            'message' => $msg
        ]);
    }

    $result = apiCallUserUpdateInfo($user_id, $user_name);
    if($result['status'] == 'error') {
        $msg = UI_ITEM_GROUP_INFO . " - 更新アクション：更新処理失敗 APIエラー user_id={$user_id} user_name={$user_name}";
        createLogs(LOG_TYPE_ERROR, $msg);
        return json_encode([
            'status' => 'error',
            'message' => UI_ITEM_GROUP_INFO . "の更新に失敗しました"
        ]);
    }

    $msg = UI_ITEM_GROUP_INFO . " - 更新アクション：更新処理成功 user_id={$user_id} user_name={$user_name}";
    createLogs(LOG_TYPE_ERROR, $msg);
    return json_encode([
        'status' => 'success',
        'message' => UI_ITEM_GROUP_INFO . "を更新しました"
    ]);
}

function groupUserPostActionForGetUserIdEvent($groups_id, $user_id)
{

    if ($groups_id == null) {
        $msg = "Exception - " . UI_ITEM_USER . " - ユーザー取得アクション：" . UI_ITEM_GROUPS_ID . "がnullです";
        createLogs(LOG_TYPE_ERROR, $msg);
        throw new Exception($msg);
    }

    if ($user_id == null) {
        $msg = "Exception - " . UI_ITEM_USER . " - ユーザー取得アクション：" . UI_ITEM_USER_ID . "がnullです";
        createLogs(LOG_TYPE_ERROR, $msg);
        throw new Exception($msg);
    }

    if ($_SERVER['REQUEST_METHOD'] !== 'POST') {
        $msg = UI_ITEM_USER . " - ユーザー取得アクション：POSTメソッドではないため、実行されませんでした。 Method = " . $_SERVER['REQUEST_METHOD'];
        createLogs(LOG_TYPE_INFO, $msg);
        return json_encode([
            'status' => 'skip',
            'message' => $msg,
            'user_name' => ''
        ]);
    }

    $result = apiCallUserRefer($user_id, null, null, null, null, $groups_id);
    if ($result['status'] == "error") {
        $msg = UI_ITEM_USER . " - ユーザー取得アクション：取得処理失敗 APIエラー user_id={$user_id}";
        createLogs(LOG_TYPE_ERROR, $msg);
        return json_encode([
            'status' => 'error',
            'message' => UI_ITEM_USER . "情報の取得に失敗しました",
            'user_name' => ''
        ]);

    }
    foreach ($result['data']['user'] as $data) {
        $msg = UI_ITEM_USER . " - ユーザー取得アクション：取得処理成功 user_id={$user_id}";
        $user_name = $data['user_info']['user_name'];
        createLogs(LOG_TYPE_INFO, $msg);
        return json_encode([
            'status' => 'success',
            'message' => UI_ITEM_USER . '情報を取得しました。',
            'user_name' => $user_name
        ]);
    }
}

function groupUserPostActionForLeaderChangeEvent($groups_id, $leader_id, $new_leader_id)
{

    if ($groups_id == null) {
        $msg = "Exception - " . UI_ITEM_USER . " - リーダー変更アクション：" . UI_ITEM_GROUPS_ID . "がnullです";
        createLogs(LOG_TYPE_ERROR, $msg);
        throw new Exception($msg);
    }

    if ($leader_id == null) {
        $msg = "Exception - " . UI_ITEM_USER . " - リーダー変更アクション：" . UI_ITEM_USER_ID . "（既存リーダー）がnullです";
        createLogs(LOG_TYPE_ERROR, $msg);
        throw new Exception($msg);
    }

    if ($new_leader_id == null) {
        $msg = "Exception - " . UI_ITEM_USER . " - リーダー変更アクション：" . UI_ITEM_USER_ID . "（新リーダー）がnullです";
        createLogs(LOG_TYPE_ERROR, $msg);
        throw new Exception($msg);
    }

    if ($_SERVER['REQUEST_METHOD'] !== 'POST') {
        $msg = UI_ITEM_USER . " - リーダー変更アクション：POSTメソッドではないため、実行されませんでした。 Method = " . $_SERVER['REQUEST_METHOD'];
        createLogs(LOG_TYPE_INFO, $msg);
        return json_encode([
            'status' => 'skip',
            'message' => $msg,
            'user_name' => ''
        ]);
    }

    $leader_id_refer_result = apiCallUserRefer($leader_id, null, null, null, null, $groups_id);
    if ($leader_id_refer_result['status'] == "error") {
        $msg = "Exception - " . UI_ITEM_USER . " - リーダー変更アクション：ユーザー取得処理失敗 user_id = {$leader_id}";
        createLogs(LOG_TYPE_ERROR, $msg);
        throw new Exception($msg);
    }

    $new_leader_id_refer_result = apiCallUserRefer($new_leader_id, null, null, null, null, $groups_id);
    if ($new_leader_id_refer_result['status'] == "error") {
        $msg = UI_ITEM_USER . " - リーダー変更アクション：ユーザー取得処理失敗 user_id = {$new_leader_id}";
        createLogs(LOG_TYPE_ERROR, $msg);
        return json_encode([
            'status' => 'error',
            'message' => UI_ITEM_GROUP . "のリーダー変更に失敗しました"
        ]);
    }

    $leader_change_result = apiCallGroupInfoLeaderChange($groups_id, $leader_id, $new_leader_id);
    if ($leader_change_result['status'] == 'error') {
        $msg = UI_ITEM_USER . " - リーダー変更アクション：更新処理失敗 APIエラー user_id = {$leader_id} -> {$new_leader_id}";
        createLogs(LOG_TYPE_ERROR, $msg);
        return json_encode([
            'status' => 'error',
            'message' => UI_ITEM_GROUP . "のリーダー変更に失敗しました"
        ]);
    }

    $msg = UI_ITEM_USER . " - リーダー変更アクション：更新処理成功user_id = {$leader_id} -> {$new_leader_id}";
    createLogs(LOG_TYPE_INFO, $msg);
    return json_encode([
        'status' => 'success',
        'message' => UI_ITEM_GROUP . "のリーダーを変更しました"
    ]);
}