<?php

if (session_status() === PHP_SESSION_NONE) {
    session_start();
}

require_once $_SERVER['DOCUMENT_ROOT'] . '/Config/config.php';
require_once $_SERVER['DOCUMENT_ROOT'] . '/Config/log_config.php';
require_once $_SERVER['DOCUMENT_ROOT'] . '/Config/ui_items_config.php';
require_once $_SERVER['DOCUMENT_ROOT'] . '/Application/Services/ApiService.php';
require_once $_SERVER['DOCUMENT_ROOT'] . '/Interfaces/Views/Partials/requireApi.php';

require_once $_SERVER['DOCUMENT_ROOT'] . '/Interfaces/Views/Partials/template/input_template/get_input_template.php';
require_once $_SERVER['DOCUMENT_ROOT'] . '/Interfaces/Views/Partials/member/get_member.php';
require_once $_SERVER['DOCUMENT_ROOT'] . '/Interfaces/Views/Partials/category/get_category.php';
require_once $_SERVER['DOCUMENT_ROOT'] . '/Interfaces/Views/Partials/template/input_template/update_input_template.php';
require_once $_SERVER['DOCUMENT_ROOT'] . '/Interfaces/Views/Partials/template/input_template/is_duplication_for_input_template.php';


function inputTemplatesPostActionForSortEvent($groups_id, $selected_data_list)
{

    if ($groups_id == null) {
        $msg = "Exception - " . UI_ITEM_INPUT_TEMPLATE . " - 入替アクション：" . UI_ITEM_GROUPS_ID . "がnullです";
        createLogs(LOG_TYPE_ERROR, $msg);
        throw new Exception($msg);
    }

    if ($selected_data_list == null) {
        $msg = "Exception - " . UI_ITEM_INPUT_TEMPLATE . " - 入替アクション：" . "選択リストがnullです";
        createLogs(LOG_TYPE_ERROR, $msg);
        throw new Exception($msg);
    }

    if ($_SERVER['REQUEST_METHOD'] !== 'POST') {
        $msg = UI_ITEM_INPUT_TEMPLATE . " - 入替アクション：POSTメソッドではないため、実行されませんでした。 Method = " . $_SERVER['REQUEST_METHOD'];
        createLogs(LOG_TYPE_INFO, $msg);
        return json_encode([
            'status' => 'skip',
            'message' => $msg
        ]);
    }

    $template_max_count = getInputTemplateCount($groups_id);
    $template_checked_count = count($selected_data_list);

    if ($template_max_count != $template_checked_count) {
        $msg = UI_ITEM_INPUT_TEMPLATE . " - 入替アクション：" . UI_ITEM_INPUT_TEMPLATE . 'がすべて選択されていません';
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
        $post_template_no = $array[0];
        $post_template_id = $array[1];

        if ($post_template_no != 999) {
            $cnt++;
            $result = apiCallInputTemplateUpdate($_SESSION['user_groups_id'], $post_template_id, $cnt);
            if ($result['status'] == "error") {
                $err_cnt++;
                $msg = UI_ITEM_INPUT_TEMPLATE . " - 入替アクション：更新処理失敗 template_id=" . $post_template_id;
                createLogs(LOG_TYPE_INFO, $msg);
                continue;
            }
            $suc_cnt++;
        }
    }

    $msg = $suc_cnt . "件の" . UI_ITEM_INPUT_TEMPLATE . "を入替ました<br>" . $err_cnt . "件の" . UI_ITEM_INPUT_TEMPLATE . "をスキップしました";
    createLogs(LOG_TYPE_INFO, $msg);
    return json_encode([
        'status' => 'success',
        'message' => $msg
    ]);
}

function inputTemplatesPostActionForUsageEvent($groups_id, $selected_data_list)
{

    if ($groups_id == null) {
        $msg = "Exception - " . UI_ITEM_INPUT_TEMPLATE . " - 使用化アクション：" . UI_ITEM_GROUPS_ID . "がnullです";
        createLogs(LOG_TYPE_ERROR, $msg);
        throw new Exception($msg);
    }

    if ($selected_data_list == null) {
        $msg = "Exception - " . UI_ITEM_INPUT_TEMPLATE . " - 使用化アクション：" . "選択リストがnullです";
        createLogs(LOG_TYPE_ERROR, $msg);
        throw new Exception($msg);
    }

    if ($_SERVER['REQUEST_METHOD'] !== 'POST') {
        $msg = UI_ITEM_INPUT_TEMPLATE . " - 使用化アクション：POSTメソッドではないため、実行されませんでした。 Method = " . $_SERVER['REQUEST_METHOD'];
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
        $post_template_no = $array[0];
        $post_template_id = $array[1];
        $post_template_name = $array[2];
        $post_member_id = $array[3];
        $post_category_id = $array[4];
        $post_type = $array[5];
        $post_payment = $array[6];
        $post_settlement = $array[7];
        $post_amount = $array[8];
        $post_remarks = $array[9];
        $post_use = $array[10];
        $post_deleted = $array[11];

        // 既に使用状態の場合
        if ($post_use == "1") {
            $err_cnt++;
            continue;
        }

        // 無効状態の場合
        if ($post_deleted == "1") {
            $err_cnt++;
            continue;
        }

        // 購入者が無効化されている場合
        if (!isActiveMember($post_member_id, $groups_id)) {
            $err_cnt++;
            continue;
        }

        // カテゴリーが無効化されている場合
        if (!isActiveCategory($post_category_id, $groups_id)) {
            $err_cnt++;
            continue;
        }

        $result = apiCallInputTemplateUsage($_SESSION['user_groups_id'], $post_template_id);
        if ($result['status'] == "error") {
            $err_cnt++;
            $msg = UI_ITEM_INPUT_TEMPLATE . " - 使用化アクション：更新処理失敗 template_id=" . $post_template_id;
            createLogs(LOG_TYPE_INFO, $msg);
            continue;
        }
        $suc_cnt++;
    }

    $msg = $suc_cnt . "件の" . UI_ITEM_INPUT_TEMPLATE . "使用状態に変更しました<br>" . $err_cnt . "件の" . UI_ITEM_INPUT_TEMPLATE . "をスキップしました";
    createLogs(LOG_TYPE_INFO, $msg);
    return json_encode([
        'status' => 'success',
        'message' => $msg
    ]);
}

function inputTemplatesPostActionForUnusageEvent($groups_id, $selected_data_list)
{

    if ($groups_id == null) {
        $msg = "Exception - " . UI_ITEM_INPUT_TEMPLATE . " - 不使用化アクション：" . UI_ITEM_GROUPS_ID . "がnullです";
        createLogs(LOG_TYPE_ERROR, $msg);
        throw new Exception($msg);
    }

    if ($selected_data_list == null) {
        $msg = "Exception - " . UI_ITEM_INPUT_TEMPLATE . " - 不使用化アクション：" . "選択リストがnullです";
        createLogs(LOG_TYPE_ERROR, $msg);
        throw new Exception($msg);
    }

    if ($_SERVER['REQUEST_METHOD'] !== 'POST') {
        $msg = UI_ITEM_INPUT_TEMPLATE . " - 不使用化アクション：POSTメソッドではないため、実行されませんでした。 Method = " . $_SERVER['REQUEST_METHOD'];
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
        $post_template_no = $array[0];
        $post_template_id = $array[1];
        $post_template_name = $array[2];
        $post_member_id = $array[3];
        $post_category_id = $array[4];
        $post_type = $array[5];
        $post_payment = $array[6];
        $post_settlement = $array[7];
        $post_amount = $array[8];
        $post_remarks = $array[9];
        $post_use = $array[10];
        $post_deleted = $array[11];

        // 既に不使用状態の場合
        if ($post_use == "0") {
            $err_cnt++;
            continue;
        }

        // 無効状態の場合
        if ($post_deleted == "1") {
            $err_cnt++;
            continue;
        }

        $result = apiCallInputTemplateUnUsage($_SESSION['user_groups_id'], $post_template_id);
        if ($result['status'] == "error") {
            $err_cnt++;
            $msg = UI_ITEM_INPUT_TEMPLATE . " - 不使用化アクション：更新処理失敗 template_id=" . $post_template_id;
            createLogs(LOG_TYPE_INFO, $msg);
            continue;
        }
        $suc_cnt++;
    }

    $msg = $suc_cnt . "件の" . UI_ITEM_INPUT_TEMPLATE . "不使用状態に変更しました<br>" . $err_cnt . "件の" . UI_ITEM_INPUT_TEMPLATE . "をスキップしました";
    createLogs(LOG_TYPE_INFO, $msg);
    return json_encode([
        'status' => 'success',
        'message' => $msg
    ]);
}

function inputTemplatesPostActionForActivateEvent($groups_id, $selected_data_list)
{

    if ($groups_id == null) {
        $msg = "Exception - " . UI_ITEM_INPUT_TEMPLATE . " - 有効化アクション：" . UI_ITEM_GROUPS_ID . "がnullです";
        createLogs(LOG_TYPE_ERROR, $msg);
        throw new Exception($msg);
    }

    if ($selected_data_list == null) {
        $msg = "Exception - " . UI_ITEM_INPUT_TEMPLATE . " - 有効化アクション：" . "選択リストがnullです";
        createLogs(LOG_TYPE_ERROR, $msg);
        throw new Exception($msg);
    }

    if ($_SERVER['REQUEST_METHOD'] !== 'POST') {
        $msg = UI_ITEM_INPUT_TEMPLATE . " - 有効化アクション：POSTメソッドではないため、実行されませんでした。 Method = " . $_SERVER['REQUEST_METHOD'];
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
        $post_template_no = $array[0];
        $post_template_id = $array[1];
        $post_template_name = $array[2];
        $post_member_id = $array[3];
        $post_category_id = $array[4];
        $post_type = $array[5];
        $post_payment = $array[6];
        $post_settlement = $array[7];
        $post_amount = $array[8];
        $post_remarks = $array[9];
        $post_use = $array[10];
        $post_deleted = $array[11];

        // 既に有効状態の場合
        if ($post_deleted == "0") {
            $err_cnt++;
            continue;
        }

        $result = apiCallInputTemplateUnDisable($_SESSION['user_groups_id'], $post_template_id);
        if ($result['status'] == "error") {
            $err_cnt++;
            $msg = UI_ITEM_INPUT_TEMPLATE . " - 有効化アクション：更新処理失敗 template_id=" . $post_template_id;
            createLogs(LOG_TYPE_INFO, $msg);
            continue;
        }
        $suc_cnt++;
    }

    $msg = $suc_cnt . "件の" . UI_ITEM_INPUT_TEMPLATE . "有効状態に変更しました<br>" . $err_cnt . "件の" . UI_ITEM_INPUT_TEMPLATE . "をスキップしました";
    createLogs(LOG_TYPE_INFO, $msg);
    return json_encode([
        'status' => 'success',
        'message' => $msg
    ]);
}

function inputTemplatesPostActionForDeactivateEvent($groups_id, $selected_data_list)
{

    if ($groups_id == null) {
        $msg = "Exception - " . UI_ITEM_INPUT_TEMPLATE . " - 無効化アクション：" . UI_ITEM_GROUPS_ID . "がnullです";
        createLogs(LOG_TYPE_ERROR, $msg);
        throw new Exception($msg);
    }

    if ($selected_data_list == null) {
        $msg = "Exception - " . UI_ITEM_INPUT_TEMPLATE . " - 無効化アクション：" . "選択リストがnullです";
        createLogs(LOG_TYPE_ERROR, $msg);
        throw new Exception($msg);
    }

    if ($_SERVER['REQUEST_METHOD'] !== 'POST') {
        $msg = UI_ITEM_INPUT_TEMPLATE . " - 無効化アクション：POSTメソッドではないため、実行されませんでした。 Method = " . $_SERVER['REQUEST_METHOD'];
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
        $post_template_no = $array[0];
        $post_template_id = $array[1];
        $post_template_name = $array[2];
        $post_member_id = $array[3];
        $post_category_id = $array[4];
        $post_type = $array[5];
        $post_payment = $array[6];
        $post_settlement = $array[7];
        $post_amount = $array[8];
        $post_remarks = $array[9];
        $post_use = $array[10];
        $post_deleted = $array[11];

        // 使用状態の場合
        if ($post_use == "1") {
            $err_cnt++;
            continue;
        }

        // 既に無効状態の場合
        if ($post_deleted == "1") {
            $err_cnt++;
            continue;
        }

        $result = apiCallInputTemplateDisable($_SESSION['user_groups_id'], $post_template_id);
        if ($result['status'] == "error") {
            $err_cnt++;
            $msg = UI_ITEM_INPUT_TEMPLATE . " - 無効化アクション：更新処理失敗 template_id=" . $post_template_id;
            createLogs(LOG_TYPE_INFO, $msg);
            continue;
        }
        $suc_cnt++;
    }

    $msg = $suc_cnt . "件の" . UI_ITEM_INPUT_TEMPLATE . "無効状態に変更しました<br>" . $err_cnt . "件の" . UI_ITEM_INPUT_TEMPLATE . "をスキップしました";
    createLogs(LOG_TYPE_INFO, $msg);
    return json_encode([
        'status' => 'success',
        'message' => $msg
    ]);
}

function inputTemplatesPostActionForDeleteEvent($groups_id, $selected_data_list)
{

    if ($groups_id == null) {
        $msg = "Exception - " . UI_ITEM_INPUT_TEMPLATE . " - 削除アクション：" . UI_ITEM_GROUPS_ID . "がnullです";
        createLogs(LOG_TYPE_ERROR, $msg);
        throw new Exception($msg);
    }

    if ($selected_data_list == null) {
        $msg = "Exception - " . UI_ITEM_INPUT_TEMPLATE . " - 削除アクション：" . "選択リストがnullです";
        createLogs(LOG_TYPE_ERROR, $msg);
        throw new Exception($msg);
    }

    if ($_SERVER['REQUEST_METHOD'] !== 'POST') {
        $msg = UI_ITEM_INPUT_TEMPLATE . " - 削除アクション：POSTメソッドではないため、実行されませんでした。 Method = " . $_SERVER['REQUEST_METHOD'];
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
        $post_template_no = $array[0];
        $post_template_id = $array[1];
        $post_template_name = $array[2];
        $post_member_id = $array[3];
        $post_category_id = $array[4];
        $post_type = $array[5];
        $post_payment = $array[6];
        $post_settlement = $array[7];
        $post_amount = $array[8];
        $post_remarks = $array[9];
        $post_use = $array[10];
        $post_deleted = $array[11];

        // 有効状態の場合
        if ($post_deleted == "0") {
            $err_cnt++;
            continue;
        }

        $result = apiCallInputTemplateDelete($_SESSION['user_groups_id'], $post_template_id);
        if ($result['status'] == "error") {
            $err_cnt++;
            $msg = UI_ITEM_INPUT_TEMPLATE . " - 削除アクション：更新処理失敗 template_id=" . $post_template_id;
            createLogs(LOG_TYPE_INFO, $msg);
            continue;
        }
        $suc_cnt++;
    }

    $msg = $suc_cnt . "件の" . UI_ITEM_INPUT_TEMPLATE . "を削除しました<br>" . $err_cnt . "件の" . UI_ITEM_INPUT_TEMPLATE . "をスキップしました";
    createLogs(LOG_TYPE_INFO, $msg);
    return json_encode([
        'status' => 'success',
        'message' => $msg
    ]);
}

function inputTemplatePostActionForEntryEvent($groups_id, $template_id, $template_name, $member_id, $category_id, $type, $payment, $settlement, $amount, $remarks, $use)
{

    // テンプレートID重複チェック
    if (isDuplicationForInputTemplate($groups_id, $template_id)) {
        $msg = UI_ITEM_INPUT_TEMPLATE . " - 登録アクション：登録処理失敗 既に登録済みの" . UI_ITEM_INPUT_TEMPLATE . "ID groups_id={$groups_id} template_id={$template_id}";
        $data = [
            "status" => "error",
            "message" => UI_ITEM_INPUT_TEMPLATE . "の登録に失敗しました"
        ];
        createLogs(LOG_TYPE_ERROR, $msg);
        return json_encode($data);
    };

    // 新規登録テンプレート番号生成
    $new_no = getMaxInputTemplateNo($groups_id) + 1;

    // 登録テンプレート作成
    $result = apiCallInputTemplateCreate($groups_id, $new_no, $template_id, $template_name, $member_id, $category_id, $type, $payment, $settlement, $amount, $remarks, $use);

    // API処理エラー
    if ($result['status'] == "error") {
        $msg = UI_ITEM_INPUT_TEMPLATE . " - 登録アクション：登録処理失敗 APIエラー groups_id={$groups_id} template_id={$template_id}";
        $data = [
            "status" => "error",
            "message" => UI_ITEM_INPUT_TEMPLATE . "の登録に失敗しました"
        ];
        createLogs(LOG_TYPE_ERROR, $msg);
        return json_encode($data);
    }

    $msg = UI_ITEM_INPUT_TEMPLATE . " - 登録アクション：登録処理成功 groups_id={$groups_id} template_no={$new_no} template_id={$template_id}";
    $data = [
        "status" => "success",
        "message" => UI_ITEM_INPUT_TEMPLATE . "を登録しました"
    ];
    createLogs(LOG_TYPE_INFO, $msg);
    return json_encode($data);
}

function inputTemplatePostActionForUpdateEvent($groups_id, $template_id, $template_no, $template_name, $member_id, $category_id, $type, $payment, $settlement, $amount, $remarks, $use, $status)
{

    // 登録テンプレート更新
    $result = apiCallInputTemplateUpdate($groups_id, $template_id, $template_no, $template_name, $member_id, $category_id, $type, $payment, $settlement, $amount, $remarks, $use);

    // API処理エラー
    if ($result['status'] == "error") {
        $msg = UI_ITEM_INPUT_TEMPLATE . " - 更新アクション：更新処理失敗 APIエラー groups_id={$groups_id} template_id={$template_id}";
        createLogs(LOG_TYPE_ERROR, $msg);
        return json_encode([
            "status" => "error",
            "message" => UI_ITEM_INPUT_TEMPLATE . "の更新に失敗しました"
        ]);
    }

   if($status == 0){
       $result_un_disable = apiCallInputTemplateUnDisable($groups_id, $template_id);
       if ($result_un_disable['status'] == "error") {
           $msg = UI_ITEM_INPUT_TEMPLATE . " - 更新アクション：有効化処理失敗 APIエラー groups_id={$groups_id} template_id={$template_id}";
           createLogs(LOG_TYPE_ERROR, $msg);
           return json_encode([
               "status" => "error",
               "message" => UI_ITEM_INPUT_TEMPLATE . "の有効化に失敗しました"
           ]);
       }
   } else{
       $result_disable = apiCallInputTemplateDisable($groups_id, $template_id);
       if ($result_disable['status'] == "error") {
           $msg = UI_ITEM_INPUT_TEMPLATE . " - 更新アクション：無効化処理失敗 APIエラー groups_id={$groups_id} template_id={$template_id}";
           createLogs(LOG_TYPE_ERROR, $msg);
           return json_encode([
               "status" => "error",
               "message" => UI_ITEM_INPUT_TEMPLATE . "の無効化に失敗しました"
           ]);
       }
   }

    $msg = UI_ITEM_INPUT_TEMPLATE . " - 更新アクション：更新処理成功 groups_id={$groups_id} template_id={$template_id}";
    createLogs(LOG_TYPE_INFO, $msg);
    return json_encode([
        "status" => "success",
        "message" => UI_ITEM_INPUT_TEMPLATE . "を更新しました"
    ]);
}