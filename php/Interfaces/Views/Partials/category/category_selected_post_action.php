<?php

if (session_status() === PHP_SESSION_NONE) {
    session_start();
}

require_once $_SERVER['DOCUMENT_ROOT'] . '/Config/config.php';
require_once $_SERVER['DOCUMENT_ROOT'] . '/Config/log_config.php';
require_once $_SERVER['DOCUMENT_ROOT'] . '/Config/ui_items_config.php';
require_once $_SERVER['DOCUMENT_ROOT'] . '/Application/Services/ApiService.php';
require_once $_SERVER['DOCUMENT_ROOT'] . '/Interfaces/Views/Partials/requireApi.php';
require_once $_SERVER['DOCUMENT_ROOT'] . '/Interfaces/Views/Partials/category/get_category.php';


// カテゴリー一覧
function categoriesPostActionForSortEvent($groups_id, $selected_data_list)
{

    if($groups_id == null){
        $msg = "Exception - " . UI_ITEM_CATEGORY . " - 入替アクション：" . UI_ITEM_GROUPS_ID . "がnullです";
        createLogs(LOG_TYPE_ERROR, $msg);
        throw new Exception($msg);
    }

    if($selected_data_list == null){
        $msg = "Exception - " . UI_ITEM_CATEGORY . " - 入替アクション：" . "選択リストがnullです";
        createLogs(LOG_TYPE_ERROR, $msg);
        throw new Exception($msg);
    }

    if ($_SERVER['REQUEST_METHOD'] !== 'POST') {
        $msg = UI_ITEM_CATEGORY . " - 入替アクション：POSTメソッドではないため、実行されませんでした。 Method = " . $_SERVER['REQUEST_METHOD'];
        createLogs(LOG_TYPE_INFO, $msg);
        return json_encode([
            'status' => 'skip',
            'message' => $msg
        ]);
    }

    $count_result = apiCallCategoryCount(null, $_SESSION['user_groups_id']);
    $category_max_count = $count_result['data']['recode_count'];
    $category_checked_count = count($selected_data_list);

    if ($category_max_count != $category_checked_count) {
        $msg = UI_ITEM_CATEGORY . " - 入替アクション：" . UI_ITEM_CATEGORY . 'がすべて選択されていません';
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
        $post_category_id = $array[0];
        $post_groups_id = $array[1];
        $post_category_no = $array[2];
        $post_category_name = $array[3];
        $post_deleted = $array[4];

        if ($post_category_no != 999) {
            $cnt++;
            $result=apiCallCategoryUpdate($post_category_id, $cnt);
            if($result['status']=="error"){
                $err_cnt++;
                $msg = UI_ITEM_CATEGORY . " - 入替アクション：更新処理失敗 category_id=" . $post_category_id;
                createLogs(LOG_TYPE_INFO, $msg);
                continue;
            }
            $suc_cnt++;
        }
    }

    $msg = $suc_cnt . "件の" . UI_ITEM_CATEGORY . "を入替ました<br>" . $err_cnt . "件の" . UI_ITEM_CATEGORY . "をスキップしました";
    createLogs(LOG_TYPE_INFO, $msg);
    return json_encode([
        'status' => 'success',
        'message' => $msg
    ]);
}

function categoriesPostActionForActivateEvent($groups_id, $selected_data_list)
{

    if($groups_id == null){
        $msg = "Exception - " . UI_ITEM_CATEGORY . " - 有効化アクション：" . UI_ITEM_GROUPS_ID . "がnullです";
        createLogs(LOG_TYPE_ERROR, $msg);
        throw new Exception($msg);
    }

    if($selected_data_list == null){
        $msg = "Exception - " . UI_ITEM_CATEGORY . " - 有効化アクション：" . "選択リストがnullです";
        createLogs(LOG_TYPE_ERROR, $msg);
        throw new Exception($msg);
    }

    if ($_SERVER['REQUEST_METHOD'] !== 'POST') {
        $msg = UI_ITEM_CATEGORY . " - 有効化アクション：POSTメソッドではないため、実行されませんでした。 Method = " . $_SERVER['REQUEST_METHOD'];
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
        $post_category_id = $array[0];
        $post_groups_id = $array[1];
        $post_category_no = $array[2];
        $post_category_name = $array[3];
        $post_deleted = $array[4];

        // 既に有効状態の場合
        if ($post_deleted == "0") {
            $err_cnt++;
            continue;
        }

        $result = apiCallCategoryUnDisable($post_category_id);
        if($result['status']=="error"){
            $err_cnt++;
            $msg = UI_ITEM_CATEGORY . " - 有効化アクション：更新処理失敗 category_id=" . $post_category_id;
            createLogs(LOG_TYPE_INFO, $msg);
            continue;
        }
        $category_max_no = getMaxCategoryNo($_SESSION['user_groups_id']);
        $category_new_no = $category_max_no + 1;
        apiCallCategoryUpdate($post_category_id, $category_new_no);
        $suc_cnt++;
    }

    $msg = $suc_cnt . "件の" . UI_ITEM_CATEGORY . "有効状態に変更しました<br>" . $err_cnt . "件の" . UI_ITEM_CATEGORY . "をスキップしました";
    createLogs(LOG_TYPE_INFO, $msg);
    return json_encode([
        'status' => 'success',
        'message' => $msg
    ]);
}

function categoriesPostActionFordeactivateEvent($groups_id, $selected_data_list)
{

    if($groups_id == null){
        $msg = "Exception - " . UI_ITEM_CATEGORY . " - 無効化アクション：" . UI_ITEM_GROUPS_ID . "がnullです";
        createLogs(LOG_TYPE_ERROR, $msg);
        throw new Exception($msg);
    }

    if($selected_data_list == null){
        $msg = "Exception - " . UI_ITEM_CATEGORY . " - 無効化アクション：" . "選択リストがnullです";
        createLogs(LOG_TYPE_ERROR, $msg);
        throw new Exception($msg);
    }

    if ($_SERVER['REQUEST_METHOD'] !== 'POST') {
        $msg = UI_ITEM_CATEGORY . " - 無効化アクション：POSTメソッドではないため、実行されませんでした。 Method = " . $_SERVER['REQUEST_METHOD'];
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
        $post_category_id = $array[0];
        $post_groups_id = $array[1];
        $post_category_no = $array[2];
        $post_category_name = $array[3];
        $post_deleted = $array[4];

        // 既に無効状態の場合
        if ($post_deleted == "1") {
            $err_cnt++;
            continue;
        }

        $result = apiCallCategoryDisable($post_category_id);
        if($result['status']=="error"){
            $err_cnt++;
            $msg = UI_ITEM_CATEGORY . " - 無効化アクション：更新処理失敗 category_id=" . $post_category_id;
            createLogs(LOG_TYPE_INFO, $msg);
            continue;
        }
        apiCallCategoryUpdate($post_category_id, 999);
        $suc_cnt++;
    }

    $msg = $suc_cnt . "件の" . UI_ITEM_CATEGORY . "無効状態に変更しました<br>" . $err_cnt . "件の" . UI_ITEM_CATEGORY . "をスキップしました";
    createLogs(LOG_TYPE_INFO, $msg);
    return json_encode([
        'status' => 'success',
        'message' => $msg
    ]);
}

function categoriesPostActionForDeleteEvent($groups_id, $selected_data_list)
{

    if($groups_id == null){
        $msg = "Exception - " . UI_ITEM_CATEGORY . " - 削除アクション：" . UI_ITEM_GROUPS_ID . "がnullです";
        createLogs(LOG_TYPE_ERROR, $msg);
        throw new Exception($msg);
    }

    if($selected_data_list == null){
        $msg = "Exception - " . UI_ITEM_CATEGORY . " - 削除アクション：" . "選択リストがnullです";
        createLogs(LOG_TYPE_ERROR, $msg);
        throw new Exception($msg);
    }

    if ($_SERVER['REQUEST_METHOD'] !== 'POST') {
        $msg = UI_ITEM_CATEGORY . " - 削除アクション：POSTメソッドではないため、実行されませんでした。 Method = " . $_SERVER['REQUEST_METHOD'];
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
        $post_category_id = $array[0];
        $post_groups_id = $array[1];
        $post_category_no = $array[2];
        $post_category_name = $array[3];
        $post_deleted = $array[4];

        // 有効状態の場合
        if ($post_deleted == "0") {
            $err_cnt++;
            continue;
        }

        $result = apiCallCategoryDelete($groups_id, $post_category_id);
        if($result['status']=="error"){
            $err_cnt++;
            $msg = UI_ITEM_CATEGORY . " - 削除アクション：更新処理失敗 category_id=" . $post_category_id;
            createLogs(LOG_TYPE_INFO, $msg);
            continue;
        }
        $suc_cnt++;
    }

    $msg = $suc_cnt . "件の" . UI_ITEM_CATEGORY . "を削除しました<br>" . $err_cnt . "件の" . UI_ITEM_CATEGORY . "をスキップしました";
    createLogs(LOG_TYPE_INFO, $msg);
    return json_encode([
        'status' => 'success',
        'message' => $msg
    ]);
}

function categoryPostActionForEntryEvent(
    $groups_id,
    $category_name
)
{
    $category_new_no = getMaxCategoryNo($groups_id) + 1;
    $result = apiCallCategoryCreate($groups_id,$category_new_no,$category_name);
    if($result['status']!='success'){
        $msg = UI_ITEM_CATEGORY . " - 登録アクション：登録処理失敗 APIエラー groups_id={$groups_id} category_name={$category_name}";
        createLogs(LOG_TYPE_ERROR, $msg);
        return json_encode([
            "status" => "error",
            "message" => UI_ITEM_CATEGORY . "の登録に失敗しました",
        ]);
    }

    $msg = UI_ITEM_CATEGORY . " - 登録アクション：処理成功 groups_id={$groups_id} category_no={$category_new_no} category_name={$category_name}";
    createLogs(LOG_TYPE_INFO, $msg);
    return json_encode([
        "status" => "success",
        "message" => UI_ITEM_CATEGORY . "を登録しました"
    ]);
}

function categoryPostActionForUpdateEvent(
    $category_id,
    $category_name,
    $status
)
{
    $result = apiCallCategoryUpdate($category_id,null,$category_name);
    if($result['status']!='success'){
        $msg = UI_ITEM_CATEGORY . " - 更新アクション：更新処理失敗 APIエラー category_id={$category_id} category_name={$category_name}";
        createLogs(LOG_TYPE_ERROR, $msg);
        return json_encode([
            "status" => "error",
            "message" => UI_ITEM_CATEGORY . "の更新に失敗しました",
        ]);
    }

    if($status == 0){
        $result_un_disable = apiCallCategoryUnDisable($category_id);
        if ($result_un_disable['status'] == "error") {
            $msg = UI_ITEM_CATEGORY . " - 更新アクション：有効化処理失敗 APIエラー category_id={$category_id} category_name={$category_name}";
            createLogs(LOG_TYPE_ERROR, $msg);
            return json_encode([
                "status" => "error",
                "message" => UI_ITEM_CATEGORY . "の有効化に失敗しました"
            ]);
        }
    } else{
        $result_disable = apiCallCategoryDisable($category_id);
        if ($result_disable['status'] == "error") {
            $msg = UI_ITEM_CATEGORY . " - 更新アクション：無効化処理失敗 APIエラー category_id={$category_id} category_name={$category_name}";
            createLogs(LOG_TYPE_ERROR, $msg);
            return json_encode([
                "status" => "error",
                "message" => UI_ITEM_CATEGORY . "の無効化に失敗しました"
            ]);
        }
    }

    $msg = UI_ITEM_CATEGORY . " - 更新アクション：処理成功 category_id={$category_id} category_name={$category_name}";
    createLogs(LOG_TYPE_INFO, $msg);
    return json_encode([
        "status" => "success",
        "message" => UI_ITEM_CATEGORY . "を更新しました",
    ]);
}