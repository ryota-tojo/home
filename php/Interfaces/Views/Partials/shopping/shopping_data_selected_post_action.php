<?php

if (session_status() === PHP_SESSION_NONE) {
    session_start();
}

require_once $_SERVER['DOCUMENT_ROOT'] . '/Config/config.php';
require_once $_SERVER['DOCUMENT_ROOT'] . '/Config/log_config.php';
require_once $_SERVER['DOCUMENT_ROOT'] . '/Config/ui_items_config.php';
require_once $_SERVER['DOCUMENT_ROOT'] . '/Application/Services/ApiService.php';
require_once $_SERVER['DOCUMENT_ROOT'] . '/Interfaces/Views/Partials/requireApi.php';
require_once $_SERVER['DOCUMENT_ROOT'] . '/Interfaces/Views/Partials/shopping/get_shopping_data.php';


// 購入データ登録（テンプレート）
function shoppingDataPostActionForEntryTemplateInputEvent($groups_id, $user_id, $target_date ,$selected_data_list)
{

    if($groups_id == null){
        $msg = "Exception - " . UI_ITEM_SHOPPING . " - 登録アクション：" . UI_ITEM_GROUPS_ID . "がnullです";
        createLogs(LOG_TYPE_ERROR, $msg);
        throw new Exception($msg);
    }

    if($user_id == null){
        $msg = "Exception - " . UI_ITEM_SHOPPING . " - 登録アクション：" . UI_ITEM_USER_ID . "がnullです";
        createLogs(LOG_TYPE_ERROR, $msg);
        throw new Exception($msg);
    }

    if($selected_data_list == null){
        $msg = "Exception - " . UI_ITEM_SHOPPING . " - 登録アクション：" . "選択リストがnullです";
        createLogs(LOG_TYPE_ERROR, $msg);
        throw new Exception($msg);
    }

    if ($_SERVER['REQUEST_METHOD'] !== 'POST') {
        $msg = UI_ITEM_SHOPPING . " - 登録アクション：POSTメソッドではないため、実行されませんでした。 Method = " . $_SERVER['REQUEST_METHOD'];
        createLogs(LOG_TYPE_INFO, $msg);
        return json_encode([
            'status' => 'skip',
            'message' => $msg
        ]);
    }
    if($target_date == null){
        $msg = UI_ITEM_SHOPPING . " - 登録アクション：登録日付が選択されていません";
        createLogs(LOG_TYPE_ERROR, $msg);
        return json_encode([
            'status' => 'error',
            'message' => "登録日付が選択されていません"
        ]);
    }

    foreach ($selected_data_list as $data) {

        $array = explode("\t", $data);
        $template_no = $array[0];
        $template_id = $array[1];
        $template_name = $array[2];
        $member_id = $array[3];
        $category_id = $array[4];
        $type = $array[5];
        $payment = $array[6];
        $settlement = $array[7];
        $amount = $array[8];
        $remarks = $array[9];
        $use = $array[10];
        $deleted = $array[11];

        if($use == 0 or $deleted == 1){
            $msg = UI_ITEM_SHOPPING . " - 登録アクション：無効なテンプレートが指定されています";
            createLogs(LOG_TYPE_ERROR, $msg);
            return json_encode([
                'status' => 'error',
                'message' => "無効なテンプレートが指定されています"
            ]);
        }

        if(!isShoppingDataCreatable($groups_id, $user_id, $target_date, $member_id, $category_id, $type, $payment, $settlement, $amount, $remarks)){
            $msg = UI_ITEM_SHOPPING . " - 登録アクション：不正な値が入力されています groups_id='{$groups_id}' user_id='{$user_id}' shopping_date='{$target_date}' member_id='{$member_id}' category_id='{$category_id}' type='{$type}' payment='{$payment}' settlement='{$settlement}' amount='{$amount}' remarks='{$remarks}'";
            createLogs(LOG_TYPE_ERROR, $msg);
            return json_encode([
                'status' => 'error',
                'message' => "不正な値が入力されています"
            ]);
        }
    }

    $suc_cnt = 0;
    $err_cnt = 0;
    $cnt = 0;
    foreach ($selected_data_list as $data) {

        $array = explode("\t", $data);
        $template_no = $array[0];
        $template_id = $array[1];
        $template_name = $array[2];
        $member_id = $array[3];
        $category_id = $array[4];
        $type = $array[5];
        $payment = $array[6];
        $settlement = $array[7];
        $amount = $array[8];
        $remarks = $array[9];

        // 購入データ登録
        $result = shoppingDataCreatable($groups_id, $user_id, $target_date, $member_id, $category_id, $type, $payment, $settlement, $amount, $remarks);
        $data = json_decode($result,true);
        if($data['status'] == 'error'){
            $err_cnt += 1;
            continue;
        }
        $suc_cnt += 1;
    }

    $msg = $suc_cnt . "件の" . UI_ITEM_SHOPPING . "を登録ました<br>" . $err_cnt . "件の" . UI_ITEM_SHOPPING . "をスキップしました";
    createLogs(LOG_TYPE_INFO, $msg);
    return json_encode([
        'status' => 'success',
        'message' => $msg
    ]);
}
