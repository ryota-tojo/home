<?php

if (session_status() === PHP_SESSION_NONE) {
    session_start();
}

require_once $_SERVER['DOCUMENT_ROOT'] . '/Config/config.php';
require_once $_SERVER['DOCUMENT_ROOT'] . '/Config/log_config.php';
require_once $_SERVER['DOCUMENT_ROOT'] . '/Application/Services/ApiService.php';
require_once $_SERVER['DOCUMENT_ROOT'] . '/Interfaces/Views/Partials/requireApi.php';

function isExistChoiceType($item_no){
    $result = apiCallMasterChoicesRefer();
    if ($result['status'] == "error") {
        throw new InvalidArgumentException("選択肢存在チェック - 選択肢の取得に失敗しました");
    }
    $data_list = $result['data']['choices_list'];
    foreach ($data_list as $data){
        if($data['item_type'] == 'type' and $data['item_no'] == $item_no){
            return true;
        }
    }
    return false;
}

function isExistChoicePayment($item_no){
    $result = apiCallMasterChoicesRefer();
    if ($result['status'] == "error") {
        throw new InvalidArgumentException("選択肢存在チェック - 選択肢の取得に失敗しました");
    }
    $data_list = $result['data']['choices_list'];
    foreach ($data_list as $data){
        if($data['item_type'] == 'payment' and $data['item_no'] == $item_no){
            return true;
        }
    }
    return false;
}

function isExistChoiceSettlement($item_no){
    $result = apiCallMasterChoicesRefer();
    if ($result['status'] == "error") {
        throw new InvalidArgumentException("選択肢存在チェック - 選択肢の取得に失敗しました");
    }
    $data_list = $result['data']['choices_list'];
    foreach ($data_list as $data){
        if($data['item_type'] == 'settlement' and $data['item_no'] == $item_no){
            return true;
        }
    }
    return false;
}

function getChoiceTypeList()
{
    $item_list=[];

    $data_list = [];
    $result = apiCallMasterChoicesRefer();
    if ($result['status'] != "error") {
        $data_list = $result['data']['choices_list'];
    }
    foreach ($data_list as $data){
        if($data['item_type'] == 'type'){
            $item_list[$data['item_no']] = $data['item_name_pc'];
        }
    }
    return array_reverse($item_list, true);
}

function getChoicePaymentList()
{
    $item_list=[];

    $data_list = [];
    $result = apiCallMasterChoicesRefer();
    if ($result['status'] != "error") {
        $data_list = $result['data']['choices_list'];
    }
    foreach ($data_list as $data){
        if($data['item_type'] == 'payment'){
            $item_list[$data['item_no']] = $data['item_name_pc'];
        }
    }

    return $item_list;
}

function getChoiceSettlementList()
{
    $item_list=[];

    $data_list = [];
    $result = apiCallMasterChoicesRefer();
    if ($result['status'] != "error") {
        $data_list = $result['data']['choices_list'];
    }
    foreach ($data_list as $data){
        if($data['item_type'] == 'settlement'){
            $item_list[$data['item_no']] = $data['item_name_pc'];
        }
    }

    return $item_list;
}
