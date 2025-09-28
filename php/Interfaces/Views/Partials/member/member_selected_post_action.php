<?php

if (session_status() === PHP_SESSION_NONE) {
    session_start();
}

require_once $_SERVER['DOCUMENT_ROOT'] . '/Config/config.php';
require_once $_SERVER['DOCUMENT_ROOT'] . '/Config/log_config.php';
require_once $_SERVER['DOCUMENT_ROOT'] . '/Config/ui_items_config.php';
require_once $_SERVER['DOCUMENT_ROOT'] . '/Application/Services/ApiService.php';
require_once $_SERVER['DOCUMENT_ROOT'] . '/Interfaces/Views/Partials/requireApi.php';
require_once $_SERVER['DOCUMENT_ROOT'] . '/Interfaces/Views/Partials/member/get_member.php';


// カテゴリー一覧
function membersPostActionForSortEvent($groups_id, $selected_data_list)
{

    if($groups_id == null){
        $msg = "Exception - " . UI_ITEM_MEMBER . " - 入替アクション：" . UI_ITEM_GROUPS_ID . "がnullです";
        createLogs(LOG_TYPE_ERROR, $msg);
        throw new Exception($msg);
    }

    if($selected_data_list == null){
        $msg = "Exception - " . UI_ITEM_MEMBER . " - 入替アクション：" . "選択リストがnullです";
        createLogs(LOG_TYPE_ERROR, $msg);
        throw new Exception($msg);
    }

    if ($_SERVER['REQUEST_METHOD'] !== 'POST') {
        $msg = UI_ITEM_MEMBER . " - 入替アクション：POSTメソッドではないため、実行されませんでした。 Method = " . $_SERVER['REQUEST_METHOD'];
        createLogs(LOG_TYPE_INFO, $msg);
        return json_encode([
            'status' => 'skip',
            'message' => $msg
        ]);
    }

    $count_result = apiCallMemberCount(null, $_SESSION['user_groups_id']);
    $member_max_count = $count_result['data']['recode_count'];
    $member_checked_count = count($selected_data_list);

    if ($member_max_count != $member_checked_count) {
        $msg = UI_ITEM_MEMBER . " - 入替アクション：" . UI_ITEM_MEMBER . 'がすべて選択されていません';
        createLogs(LOG_TYPE_INFO, $msg);
        return json_encode([
            'status' => 'error',
            'message' => $msg
        ]);
    }

    $suc_cnt = 0;
    $err_cnt = 0;
    $cnt = 0;

    foreach ($selected_data_list as $data) {

        $array = explode("\t", $data);
        $post_member_id = $array[0];
        $post_groups_id = $array[1];
        $post_member_no = $array[2];
        $post_member_name = $array[3];
        $post_deleted = $array[4];

        if ($post_member_no != 999) {
            $cnt++;
            $result=apiCallMemberUpdate($post_member_id, $cnt);
            if($result['status']=="error"){
                $err_cnt++;
                $msg = UI_ITEM_MEMBER . " - 入替アクション：更新処理失敗 member_id=" . $post_member_id;
                createLogs(LOG_TYPE_INFO, $msg);
                continue;
            }
            $suc_cnt++;
        }
    }

    $msg = $suc_cnt . "件の" . UI_ITEM_MEMBER . "を入替ました<br>" . $err_cnt . "件の" . UI_ITEM_MEMBER . "をスキップしました";
    createLogs(LOG_TYPE_INFO, $msg);
    return json_encode([
        'status' => 'success',
        'message' => $msg
    ]);
}

function membersPostActionForActivateEvent($groups_id, $selected_data_list)
{

    if($groups_id == null){
        $msg = "Exception - " . UI_ITEM_MEMBER . " - 有効化アクション：" . UI_ITEM_GROUPS_ID . "がnullです";
        createLogs(LOG_TYPE_ERROR, $msg);
        throw new Exception($msg);
    }

    if($selected_data_list == null){
        $msg = "Exception - " . UI_ITEM_MEMBER . " - 有効化アクション：" . "選択リストがnullです";
        createLogs(LOG_TYPE_ERROR, $msg);
        throw new Exception($msg);
    }

    if ($_SERVER['REQUEST_METHOD'] !== 'POST') {
        $msg = UI_ITEM_MEMBER . " - 有効化アクション：POSTメソッドではないため、実行されませんでした。 Method = " . $_SERVER['REQUEST_METHOD'];
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
        $post_member_id = $array[0];
        $post_groups_id = $array[1];
        $post_member_no = $array[2];
        $post_member_name = $array[3];
        $post_deleted = $array[4];

        // 既に有効状態の場合
        if ($post_deleted == "0") {
            $err_cnt++;
            continue;
        }

        $result = apiCallMemberUnDisable($post_member_id);
        if($result['status']=="error"){
            $err_cnt++;
            $msg = UI_ITEM_MEMBER . " - 有効化アクション：更新処理失敗 member_id=" . $post_member_id;
            createLogs(LOG_TYPE_INFO, $msg);
            continue;
        }
        $member_max_no = getMaxMemberNo($_SESSION['user_groups_id']);
        $member_new_no = $member_max_no + 1;
        apiCallMemberUpdate($post_member_id, $member_new_no);
        $suc_cnt++;
    }

    $msg = $suc_cnt . "件の" . UI_ITEM_MEMBER . "有効状態に変更しました<br>" . $err_cnt . "件の" . UI_ITEM_MEMBER . "をスキップしました";
    createLogs(LOG_TYPE_INFO, $msg);
    return json_encode([
        'status' => 'success',
        'message' => $msg
    ]);
}

function membersPostActionForDeactivateEvent($groups_id, $selected_data_list)
{

    if($groups_id == null){
        $msg = "Exception - " . UI_ITEM_MEMBER . " - 無効化アクション：" . UI_ITEM_GROUPS_ID . "がnullです";
        createLogs(LOG_TYPE_ERROR, $msg);
        throw new Exception($msg);
    }

    if($selected_data_list == null){
        $msg = "Exception - " . UI_ITEM_MEMBER . " - 無効化アクション：" . "選択リストがnullです";
        createLogs(LOG_TYPE_ERROR, $msg);
        throw new Exception($msg);
    }

    if ($_SERVER['REQUEST_METHOD'] !== 'POST') {
        $msg = UI_ITEM_MEMBER . " - 無効化アクション：POSTメソッドではないため、実行されませんでした。 Method = " . $_SERVER['REQUEST_METHOD'];
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
        $post_member_id = $array[0];
        $post_groups_id = $array[1];
        $post_member_no = $array[2];
        $post_member_name = $array[3];
        $post_deleted = $array[4];

        // 既に無効状態の場合
        if ($post_deleted == "1") {
            $err_cnt++;
            continue;
        }

        $result = apiCallMemberDisable($post_member_id);
        if($result['status']=="error"){
            $err_cnt++;
            $msg = UI_ITEM_MEMBER . " - 無効化アクション：更新処理失敗 member_id=" . $post_member_id;
            createLogs(LOG_TYPE_INFO, $msg);
            continue;
        }
        apiCallMemberUpdate($post_member_id, 999);
        $suc_cnt++;
    }

    $msg = $suc_cnt . "件の" . UI_ITEM_MEMBER . "無効状態に変更しました<br>" . $err_cnt . "件の" . UI_ITEM_MEMBER . "をスキップしました";
    createLogs(LOG_TYPE_INFO, $msg);
    return json_encode([
        'status' => 'success',
        'message' => $msg
    ]);
}

function membersPostActionForDeleteEvent($groups_id, $selected_data_list)
{

    if($groups_id == null){
        $msg = "Exception - " . UI_ITEM_MEMBER . " - 削除アクション：" . UI_ITEM_GROUPS_ID . "がnullです";
        createLogs(LOG_TYPE_ERROR, $msg);
        throw new Exception($msg);
    }

    if($selected_data_list == null){
        $msg = "Exception - " . UI_ITEM_MEMBER . " - 削除アクション：" . "選択リストがnullです";
        createLogs(LOG_TYPE_ERROR, $msg);
        throw new Exception($msg);
    }

    if ($_SERVER['REQUEST_METHOD'] !== 'POST') {
        $msg = UI_ITEM_MEMBER . " - 削除アクション：POSTメソッドではないため、実行されませんでした。 Method = " . $_SERVER['REQUEST_METHOD'];
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
        $post_member_id = $array[0];
        $post_groups_id = $array[1];
        $post_member_no = $array[2];
        $post_member_name = $array[3];
        $post_deleted = $array[4];

        // 有効状態の場合
        if ($post_deleted == "0") {
            $err_cnt++;
            continue;
        }

        $result = apiCallMemberDelete(null, $post_member_id);
        if($result['status']=="error"){
            $err_cnt++;
            $msg = UI_ITEM_MEMBER . " - 削除アクション：更新処理失敗 member_id=" . $post_member_id;
            createLogs(LOG_TYPE_INFO, $msg);
            continue;
        }
        $suc_cnt++;
    }

    $msg = $suc_cnt . "件の" . UI_ITEM_MEMBER . "を削除しました<br>" . $err_cnt . "件の" . UI_ITEM_MEMBER . "をスキップしました";
    createLogs(LOG_TYPE_INFO, $msg);
    return json_encode([
        'status' => 'success',
        'message' => $msg
    ]);
}

function memberPostActionForEntryEvent(
    $groups_id,
    $member_name
)
{
    $member_new_no = getMaxMemberNo($groups_id) + 1;
    $result = apiCallMemberCreate($groups_id,$member_new_no,$member_name);
    if($result['status']!='success'){
        $msg = UI_ITEM_MEMBER . " - 登録アクション：登録処理失敗 APIエラー groups_id={$groups_id} member_name={$member_name}";
        createLogs(LOG_TYPE_ERROR, $msg);
        return json_encode([
            "status" => "error",
            "message" => UI_ITEM_MEMBER . "の登録に失敗しました"
        ]);
    }

    $msg = UI_ITEM_MEMBER . " - 登録アクション：登録処理成功 groups_id={$groups_id} member_no={$member_new_no} member_name={$member_name}";
    createLogs(LOG_TYPE_INFO, $msg);
    return json_encode([
        "status" => "success",
        "message" => UI_ITEM_MEMBER . "を登録しました",
    ]);
}

function memberPostActionForUpdateEvent(
    $member_id,
    $member_name,
    $status
)
{
    $result = apiCallMemberUpdate($member_id,null,$member_name);
    if($result['status']!='success'){
        $msg = UI_ITEM_MEMBER . " - 更新アクション：更新処理失敗 APIエラー member_id={$member_id} member_name={$member_name}";
        createLogs(LOG_TYPE_ERROR, $msg);
        return json_encode([
            "status" => "error",
            "message" => UI_ITEM_MEMBER . "の更新に失敗しました",
        ]);
    }

    if($status == 0){
        $result_un_disable = apiCallMemberUnDisable($member_id);
        if ($result_un_disable['status'] == "error") {
            $msg = UI_ITEM_MEMBER . " - 更新アクション：有効化処理失敗 APIエラー member_id={$member_id} member_name={$member_name}";
            createLogs(LOG_TYPE_ERROR, $msg);
            return json_encode([
                "status" => "error",
                "message" => UI_ITEM_MEMBER . "の有効化に失敗しました"
            ]);
        }
    } else{
        $result_disable = apiCallMemberDisable($member_id);
        if ($result_disable['status'] == "error") {
            $msg = UI_ITEM_MEMBER . " - 更新アクション：無効化処理失敗 APIエラー member_id={$member_id} member_name={$member_name}";
            createLogs(LOG_TYPE_ERROR, $msg);
            return json_encode([
                "status" => "error",
                "message" => UI_ITEM_MEMBER . "の無効化に失敗しました"
            ]);
        }
    }

    $msg = UI_ITEM_MEMBER . " - 更新アクション：処理成功 member_id={$member_id} member_name={$member_name}";
    createLogs(LOG_TYPE_INFO, $msg);
    return json_encode([
        "status" => "success",
        "message" => UI_ITEM_MEMBER . "を更新しました",
    ]);
}