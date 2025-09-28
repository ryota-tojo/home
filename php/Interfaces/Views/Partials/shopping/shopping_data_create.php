<?php

if (session_status() === PHP_SESSION_NONE) {
    session_start();
}

require_once $_SERVER['DOCUMENT_ROOT'] . '/Config/config.php';
require_once $_SERVER['DOCUMENT_ROOT'] . '/Config/log_config.php';
require_once $_SERVER['DOCUMENT_ROOT'] . '/Config/ui_items_config.php';
require_once $_SERVER['DOCUMENT_ROOT'] . '/Application/Services/ApiService.php';
require_once $_SERVER['DOCUMENT_ROOT'] . '/Interfaces/Views/Partials/requireApi.php';
require_once $_SERVER['DOCUMENT_ROOT'] . '/Interfaces/Views/Partials/util/validation.php';

require_once $_SERVER['DOCUMENT_ROOT'] . '/Interfaces/Views/Partials/group/get_group.php';
require_once $_SERVER['DOCUMENT_ROOT'] . '/Interfaces/Views/Partials/user/get_user.php';
require_once $_SERVER['DOCUMENT_ROOT'] . '/Interfaces/Views/Partials/member/get_member.php';
require_once $_SERVER['DOCUMENT_ROOT'] . '/Interfaces/Views/Partials/category/get_category.php';
require_once $_SERVER['DOCUMENT_ROOT'] . '/Interfaces/Views/Partials/master/get_choices.php';

function isShoppingDataCreatable($groups_id, $user_id, $shopping_date, $member_id, $category_id, $type, $payment, $settlement, $amount, $remarks)
{

    // ＊＊＊ 所属グループID ＊＊＊
    // 必須チェック
    if(!validateRequiredCheck($groups_id)){
        $msg = UI_ITEM_SHOPPING . "登録可否チェック - 必須チェック：" . UI_ITEM_GROUPS_ID . "がnullです";
        createLogs(LOG_TYPE_ERROR, $msg);
        return false;
    }
    // 空欄チェック
    if(!validateBlankCheck($groups_id)){
        $msg = UI_ITEM_SHOPPING . "登録可否チェック - 空欄チェック：" . UI_ITEM_GROUPS_ID . "が空欄です";
        createLogs(LOG_TYPE_ERROR, $msg);
        return false;
    }
    // 存在チェック
    if(!isExistGroupsId($groups_id)){
        $msg = UI_ITEM_SHOPPING . "登録可否チェック - 存在チェック：存在しない" . UI_ITEM_GROUPS_ID . "です";
        createLogs(LOG_TYPE_ERROR, $msg);
        return false;
    }


    // ＊＊＊ ユーザーID ＊＊＊
    // 必須チェック
    if(!validateRequiredCheck($user_id)){
        $msg = UI_ITEM_SHOPPING . "登録可否チェック - 必須チェック：" . UI_ITEM_SHOPPING_USER . "がnullです";
        createLogs(LOG_TYPE_ERROR, $msg);
        return false;
    }
    // 空欄チェック
    if(!validateBlankCheck($user_id)){
        $msg = UI_ITEM_SHOPPING . "登録可否チェック - 空欄チェック：" . UI_ITEM_SHOPPING_USER . "が空欄です";
        createLogs(LOG_TYPE_ERROR, $msg);
        return false;
    }
    // 形式チェック（数値）
    if(!validateNumericCheck($user_id)){
        $msg = UI_ITEM_SHOPPING . "登録可否チェック - 形式チェック：" . UI_ITEM_SHOPPING_USER . "が数値ではありません";
        createLogs(LOG_TYPE_ERROR, $msg);
        return false;
    }
    // 存在チェック
    if(!isExistUser($user_id)){
        $msg = UI_ITEM_SHOPPING . "登録可否チェック - 存在チェック：存在しない" . UI_ITEM_SHOPPING_USER . "です";
        createLogs(LOG_TYPE_ERROR, $msg);
        return false;
    }


    // ＊＊＊ 購入日 ＊＊＊
    // 必須チェック
    if(!validateRequiredCheck($shopping_date)){
        $msg = UI_ITEM_SHOPPING . "登録可否チェック - 必須チェック：" . UI_ITEM_SHOPPING_DATE . "がnullです";
        createLogs(LOG_TYPE_ERROR, $msg);
        return false;
    }
    // 空欄チェック
    if(!validateBlankCheck($shopping_date)){
        $msg = UI_ITEM_SHOPPING . "登録可否チェック - 空欄チェック：" . UI_ITEM_SHOPPING_DATE . "が空欄です";
        createLogs(LOG_TYPE_ERROR, $msg);
        return false;
    }
    // 形式チェック（yyyy-MM-dd）
    if(!validateDateCheck($shopping_date)){
        $msg = UI_ITEM_SHOPPING . "登録可否チェック - 形式チェック：" . UI_ITEM_SHOPPING_DATE . "が [yyyy-MM-dd] 形式ではありません";
        createLogs(LOG_TYPE_ERROR, $msg);
        return false;
    }

    // ＊＊＊ メンバーID ＊＊＊
    // 必須チェック
    if(!validateRequiredCheck($member_id)){
        $msg = UI_ITEM_SHOPPING . "登録可否チェック - 必須チェック：" . UI_ITEM_MEMBER . "IDがnullです";
        createLogs(LOG_TYPE_ERROR, $msg);
        return false;
    }
    // 空欄チェック
    if(!validateBlankCheck($member_id)){
        $msg = UI_ITEM_SHOPPING . "登録可否チェック - 空欄チェック：" . UI_ITEM_MEMBER . "IDが空欄です";
        createLogs(LOG_TYPE_ERROR, $msg);
        return false;
    }
    // 形式チェック（数値）
    if(!validateNumericCheck($member_id)){
        $msg = UI_ITEM_SHOPPING . "登録可否チェック - 形式チェック：" . UI_ITEM_MEMBER . "IDが数値ではありません";
        createLogs(LOG_TYPE_ERROR, $msg);
        return false;
    }
    // 存在チェック
    if(!isExistMember($member_id,$groups_id)){
        $msg = UI_ITEM_SHOPPING . "登録可否チェック - 存在チェック：存在しない" . UI_ITEM_MEMBER . "です";
        createLogs(LOG_TYPE_ERROR, $msg);
        return false;
    }


    // ＊＊＊ カテゴリーID ＊＊＊
    // 必須チェック
    if(!validateRequiredCheck($category_id)){
        $msg = UI_ITEM_SHOPPING . "登録可否チェック - 必須チェック：" . UI_ITEM_CATEGORY . "IDがnullです";
        createLogs(LOG_TYPE_ERROR, $msg);
        return false;
    }
    // 空欄チェック
    if(!validateBlankCheck($category_id)){
        $msg = UI_ITEM_SHOPPING . "登録可否チェック - 空欄チェック：" . UI_ITEM_CATEGORY . "IDが空欄です";
        createLogs(LOG_TYPE_ERROR, $msg);
        return false;
    }
    // 形式チェック（数値）
    if(!validateNumericCheck($category_id)){
        $msg = UI_ITEM_SHOPPING . "登録可否チェック - 形式チェック：" . UI_ITEM_CATEGORY . "IDが数値ではありません";
        createLogs(LOG_TYPE_ERROR, $msg);
        return false;
    }
    // 存在チェック
    if(!isExistCategory($category_id,$groups_id)){
        $msg = UI_ITEM_SHOPPING . "登録可否チェック - 存在チェック：存在しない" . UI_ITEM_CATEGORY . "です";
        createLogs(LOG_TYPE_ERROR, $msg);
        return false;
    }


    // ＊＊＊ 種別 ＊＊＊
    // 必須チェック
    if(!validateRequiredCheck($type)){
        $msg = UI_ITEM_SHOPPING . "登録可否チェック - 必須チェック：" . UI_ITEM_SHOPPING_TYPE . "がnullです";
        createLogs(LOG_TYPE_ERROR, $msg);
        return false;
    }
    // 空欄チェック
    if(!validateBlankCheck($type)){
        $msg = UI_ITEM_SHOPPING . "登録可否チェック - 空欄チェック：" . UI_ITEM_SHOPPING_TYPE . "が空欄です";
        createLogs(LOG_TYPE_ERROR, $msg);
        return false;
    }
    // 形式チェック（数値）
    if(!validateNumericCheck($type)){
        $msg = UI_ITEM_SHOPPING . "登録可否チェック - 形式チェック：" . UI_ITEM_SHOPPING_TYPE . "が数値ではありません";
        createLogs(LOG_TYPE_ERROR, $msg);
        return false;
    }
    // 存在チェック
    if(!isExistChoiceType($type)){
        $msg = UI_ITEM_SHOPPING . "登録可否チェック - 存在チェック：存在しない" . UI_ITEM_SHOPPING_TYPE . "です";
        createLogs(LOG_TYPE_ERROR, $msg);
        return false;
    }


    // ＊＊＊ 支払い方法 ＊＊＊
    // 必須チェック
    if(!validateRequiredCheck($payment)){
        $msg = UI_ITEM_SHOPPING . "登録可否チェック - 必須チェック：" . UI_ITEM_SHOPPING_PAYMENT . "がnullです";
        createLogs(LOG_TYPE_ERROR, $msg);
        return false;
    }
    // 空欄チェック
    if(!validateBlankCheck($payment)){
        $msg = UI_ITEM_SHOPPING . "登録可否チェック - 空欄チェック：" . UI_ITEM_SHOPPING_PAYMENT . "が空欄です";
        createLogs(LOG_TYPE_ERROR, $msg);
        return false;
    }
    // 形式チェック（数値）
    if(!validateNumericCheck($payment)){
        $msg = UI_ITEM_SHOPPING . "登録可否チェック - 形式チェック：" . UI_ITEM_SHOPPING_PAYMENT . "が数値ではありません";
        createLogs(LOG_TYPE_ERROR, $msg);
        return false;
    }
    // 存在チェック
    if(!isExistChoicePayment($payment)){
        $msg = UI_ITEM_SHOPPING . "登録可否チェック - 存在チェック：存在しない" . UI_ITEM_SHOPPING_PAYMENT . "です";
        createLogs(LOG_TYPE_ERROR, $msg);
        return false;
    }


    // ＊＊＊ 精算 ＊＊＊
    // 必須チェック
    if(!validateRequiredCheck($settlement)){
        $msg = UI_ITEM_SHOPPING . "登録可否チェック - 必須チェック：" . UI_ITEM_SHOPPING_SETTLEMENT . "がnullです";
        createLogs(LOG_TYPE_ERROR, $msg);
        return false;
    }
    // 空欄チェック
    if(!validateBlankCheck($settlement)){
        $msg = UI_ITEM_SHOPPING . "登録可否チェック - 空欄チェック：" . UI_ITEM_SHOPPING_SETTLEMENT . "が空欄です";
        createLogs(LOG_TYPE_ERROR, $msg);
        return false;
    }
    // 形式チェック（数値）
    if(!validateNumericCheck($settlement)){
        $msg = UI_ITEM_SHOPPING . "登録可否チェック - 形式チェック：" . UI_ITEM_SHOPPING_SETTLEMENT . "が数値ではありません";
        createLogs(LOG_TYPE_ERROR, $msg);
        return false;
    }
    // 存在チェック
    if(!isExistChoiceSettlement($settlement)){
        $msg = UI_ITEM_SHOPPING . "登録可否チェック - 存在チェック：存在しない" . UI_ITEM_SHOPPING_SETTLEMENT . "です";
        createLogs(LOG_TYPE_ERROR, $msg);
        return false;
    }


    // ＊＊＊ 金額 ＊＊＊
    // 必須チェック
    if(!validateRequiredCheck($amount)){
        $msg = UI_ITEM_SHOPPING . "登録可否チェック - 必須チェック：" . UI_ITEM_SHOPPING_AMOUNT . "がnullです";
        createLogs(LOG_TYPE_ERROR, $msg);
        return false;
    }
    // 空欄チェック
    if(!validateBlankCheck($amount)){
        $msg = UI_ITEM_SHOPPING . "登録可否チェック - 空欄チェック：" . UI_ITEM_SHOPPING_AMOUNT . "が空欄です";
        createLogs(LOG_TYPE_ERROR, $msg);
        return false;
    }
    // 形式チェック（数値）
    if(!validateNumericCheck($amount)){
        $msg = UI_ITEM_SHOPPING . "登録可否チェック - 形式チェック：" . UI_ITEM_SHOPPING_AMOUNT . "が数値ではありません";
        createLogs(LOG_TYPE_ERROR, $msg);
        return false;
    }
    // 数値範囲チェック
    if(!validateMinValue($amount, 1)){
        $msg = UI_ITEM_SHOPPING . "登録可否チェック - 範囲チェック：" . UI_ITEM_SHOPPING_AMOUNT . "が1以上ではありません";
        createLogs(LOG_TYPE_ERROR, $msg);
        return false;
    }


    // ＊＊＊ 備考 ＊＊＊
    // 必須チェック
    if(!validateRequiredCheck($remarks)){
        $msg = UI_ITEM_SHOPPING . "登録可否チェック - 必須チェック：" . UI_ITEM_SHOPPING_REMARKS . "がnullです";
        createLogs(LOG_TYPE_ERROR, $msg);
        return false;
    }

    return true;
}
function shoppingDataCreatable($groups_id, $user_id, $shopping_date, $member_id, $category_id, $type, $payment, $settlement, $amount, $remarks, $error_check = 0)
{
    if($error_check == 1){
        if(!isShoppingDataCreatable($groups_id, $user_id, $shopping_date, $member_id, $category_id, $type, $payment, $settlement, $amount, $remarks)){
            $msg = UI_ITEM_SHOPPING . " - 登録アクション：不正な値が入力されています groups_id='{$groups_id}' user_id='{$user_id}' shopping_date='{$shopping_date}' member_id='{$member_id}' category_id='{$category_id}' type='{$type}' payment='{$payment}' settlement='{$settlement}' amount='{$amount}' remarks='{$remarks}'";
            createLogs(LOG_TYPE_ERROR, $msg);
            return json_encode([
                'status' => 'error',
                'message' => "不正な値が入力されています"
            ]);
        }
    }
    $result = apiCallShoppingCreate($groups_id, $user_id, $shopping_date, $member_id, $category_id, $type, $payment, $settlement, $amount, $remarks);
    if($result['status'] == 'error'){
        $msg = UI_ITEM_SHOPPING . " - 登録アクション：登録処理失敗 APIエラー groups_id='{$groups_id}' user_id='{$user_id}' shopping_date='{$shopping_date}' member_id='{$member_id}' category_id='{$category_id}' type='{$type}' payment='{$payment}' settlement='{$settlement}' amount='{$amount}' remarks='{$remarks}'";
        createLogs(LOG_TYPE_ERROR, $msg);
        return json_encode([
            'status' => 'error',
            'message' => UI_ITEM_SHOPPING . "の登録に失敗しました"
        ]);
    }

    return json_encode([
        'status' => 'success',
        'message' => UI_ITEM_SHOPPING . "を登録しました"
    ]);

}