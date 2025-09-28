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
require_once $_SERVER['DOCUMENT_ROOT'] . '/Interfaces/Views/Partials/category/get_category.php';
require_once $_SERVER['DOCUMENT_ROOT'] . '/Interfaces/Views/Partials/master/get_choices.php';

function getShoppingItemList($groups_id)
{
    if($groups_id == null){
        $msg = "Exception - " . UI_ITEM_SHOPPING . "項目取得：" . UI_ITEM_GROUPS_ID . "がnullです";
        createLogs(LOG_TYPE_ERROR, $msg);
        throw new Exception($msg);
    }
    $member_list = getMemberIdToNameList($groups_id);
    $category_list = getCategoryIdToNameList($groups_id);
    $choices_type_list = getChoiceTypeList();
    $choices_payment_list = getChoicePaymentList();
    $choices_settlement_list = getChoiceSettlementList();

    return [
        "member_list" => $member_list,
        "category_list" => $category_list,
        "type_list" => $choices_type_list,
        "payment_list" => $choices_payment_list,
        "settlement_list" => $choices_settlement_list
    ];
}
function getShoppingDataHistoryList($groups_id)
{

    if($groups_id == null){
        $msg = "Exception - " . UI_ITEM_SHOPPING . "項目取得：" . UI_ITEM_GROUPS_ID . "がnullです";
        createLogs(LOG_TYPE_ERROR, $msg);
        throw new Exception($msg);
    }
    $result = apiCallShoppingRefer(null, $groups_id,null,null,null,null,null,null,null,null,null,null,0,10);

    if($result['status'] == 'error'){
        return [];
    }

    return $result['data']['shopping_list'];
}