<?php

if (session_status() === PHP_SESSION_NONE) {
    session_start();
}

require_once $_SERVER['DOCUMENT_ROOT'] . '/Config/config.php';
require_once $_SERVER['DOCUMENT_ROOT'] . '/Config/log_config.php';
require_once $_SERVER['DOCUMENT_ROOT'] . '/Config/ui_items_config.php';
require_once $_SERVER['DOCUMENT_ROOT'] . '/Application/Services/ApiService.php';
require_once $_SERVER['DOCUMENT_ROOT'] . '/Interfaces/Views/Partials/requireApi.php';

function isExistMember($member_id = null,$groups_id = null){
    $result = apiCallMemberRefer($member_id, $groups_id);
    if ($result['status'] == "success") {
        return True;
    }
    return False;
}

/*
 * 有効化のものを判定
 * isNotExist: 存在しないメンバー
 * isActive: 有効状態
 * isDeActive: 無効状態
 */
function isActiveMember($member_id,$groups_id){
    $result = apiCallMemberRefer($member_id, $groups_id);
    if ($result['status'] == "error") {
        $msg = UI_ITEM_MEMBER . "取得：" . UI_ITEM_MEMBER . "情報の取得に失敗しました";
        createLogs(LOG_TYPE_ERROR, $msg);
        return "isNotExist";
    }
    $data_list = $result['data']['member_list'];
    $first = $data_list[0];
    if($first['delete_flag'] == "1"){
        return "isDeActive";
    }
    return "isActive";
}

function getMember($member_id,$groups_id)
{
    if($member_id == null){
        $msg = "Exception - " . UI_ITEM_MEMBER . "取得：" . UI_ITEM_MEMBER . "IDがnullです";
        createLogs(LOG_TYPE_ERROR, $msg);
        throw new Exception($msg);
    }
    if($groups_id == null){
        $msg = "Exception - " . UI_ITEM_MEMBER . "取得：" . UI_ITEM_GROUPS_ID . "がnullです";
        createLogs(LOG_TYPE_ERROR, $msg);
        throw new Exception($msg);
    }

    $result = apiCallMemberRefer($member_id, $groups_id);
    if ($result['status'] == "error") {
        $msg = UI_ITEM_MEMBER . "取得：" . UI_ITEM_MEMBER . "情報の取得に失敗しました";
        createLogs(LOG_TYPE_ERROR, $msg);
        return [
            'member_id' => UI_ITEM_LABEL_NULL,
            'member_no' => UI_ITEM_LABEL_NULL,
            'member_name' => UI_ITEM_LABEL_NULL,
            'delete_flag' => UI_ITEM_LABEL_NULL
        ];
    }
    $data_list = $result['data']['member_list'];
    $first = $data_list[0];
    return [
        'member_id' => $first['member_id'],
        'member_no' => $first['member_no'],
        'member_name' => $first['member_name'],
        'delete_flag' => $first['delete_flag']
    ];
}

function getMemberList($groups_id)
{
    if($groups_id == null){
        $msg = "Exception - " . UI_ITEM_MEMBER . "取得：" . UI_ITEM_GROUPS_ID . "がnullです";
        createLogs(LOG_TYPE_ERROR, $msg);
        throw new Exception($msg);
    }

    $result = apiCallMemberRefer(null, $groups_id);
    if ($result['status'] == "error") {
        $msg = "Exception - " . UI_ITEM_MEMBER . "取得：" . UI_ITEM_MEMBER . "情報の取得に失敗しました";
        createLogs(LOG_TYPE_ERROR, $msg);
        return [];
    }
    return $result['data']['member_list'];
}

function getMemberIdToNameList($groups_id)
{
    if($groups_id == null){
        $msg = "Exception - " . UI_ITEM_MEMBER . "取得：" . UI_ITEM_GROUPS_ID . "がnullです";
        createLogs(LOG_TYPE_ERROR, $msg);
        throw new Exception($msg);
    }

    $member_list=[];

    $data_list = [];
    $result = apiCallMemberRefer(null, $groups_id);
    if ($result['status'] != "error") {
        $data_list = $result['data']['member_list'];
    }
    foreach ($data_list as $data){
        if($data['delete_flag'] == "0") {
            $member_list[$data['member_id']] = $data['member_name'];
        }
    }

    return $member_list;
}

function getMaxMemberNo($groups_id)
{
    $member_max_no=0;
    $result = apiCallMemberRefer(null, $groups_id);
    if ($result['status'] == "error") {
        return $member_max_no;
    }
    $datas = $result['data']['member_list'];
    foreach ($datas as $data){
        if((Int)$data['member_no'] == 999){
            continue;
        }
        if($member_max_no<(Int)$data['member_no']){
            $member_max_no=(Int)$data['member_no'];
        }
    }
    return $member_max_no;
}