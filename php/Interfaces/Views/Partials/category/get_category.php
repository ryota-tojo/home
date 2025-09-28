<?php

if (session_status() === PHP_SESSION_NONE) {
    session_start();
}

require_once $_SERVER['DOCUMENT_ROOT'] . '/Config/config.php';
require_once $_SERVER['DOCUMENT_ROOT'] . '/Config/log_config.php';
require_once $_SERVER['DOCUMENT_ROOT'] . '/Config/ui_items_config.php';
require_once $_SERVER['DOCUMENT_ROOT'] . '/Application/Services/ApiService.php';
require_once $_SERVER['DOCUMENT_ROOT'] . '/Interfaces/Views/Partials/requireApi.php';

function isExistCategory($category_id = null,$groups_id = null){
    $result = apiCallCategoryRefer($category_id, $groups_id);
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
function isActiveCategory($category_id,$groups_id){
    $result = apiCallCategoryRefer($category_id, $groups_id);
    if ($result['status'] == "error") {
        $msg = UI_ITEM_CATEGORY . "取得：" . UI_ITEM_CATEGORY . "情報の取得に失敗しました";
        createLogs(LOG_TYPE_ERROR, $msg);
        return "isNotExist";
    }
    $data_list = $result['data']['category_list'];
    $first = $data_list[0];
    if($first['delete_flag'] == "1"){
        return "isDeActive";
    }
    return "isActive";
}

function getCategory($category_id,$groups_id)
{
    if($category_id == null){
        $msg = "Exception - " . UI_ITEM_CATEGORY . "取得：" . UI_ITEM_CATEGORY . "IDがnullです";
        createLogs(LOG_TYPE_ERROR, $msg);
        throw new Exception($msg);
    }
    if($groups_id == null){
        $msg = "Exception - " . UI_ITEM_CATEGORY . "取得：" . UI_ITEM_GROUPS_ID . "がnullです";
        createLogs(LOG_TYPE_ERROR, $msg);
        throw new Exception($msg);
    }

    $result = apiCallCategoryRefer($category_id, $groups_id);
    if ($result['status'] == "error") {
        $msg = UI_ITEM_CATEGORY . "取得：" . UI_ITEM_CATEGORY . "情報の取得に失敗しました";
        createLogs(LOG_TYPE_ERROR, $msg);
        return [
            'category_id' => UI_ITEM_LABEL_NULL,
            'category_no' => UI_ITEM_LABEL_NULL,
            'category_name' => UI_ITEM_LABEL_NULL,
            'delete_flag' => UI_ITEM_LABEL_NULL
        ];
    }
    $data_list = $result['data']['category_list'];
    $first = $data_list[0];
    return [
        'category_id' => $first['category_id'],
        'category_no' => $first['category_no'],
        'category_name' => $first['category_name'],
        'delete_flag' => $first['delete_flag']
    ];
}

function getCategoryList($groups_id)
{
    if($groups_id == null){
        $msg = "Exception - " . UI_ITEM_CATEGORY . "取得：" . UI_ITEM_GROUPS_ID . "がnullです";
        createLogs(LOG_TYPE_ERROR, $msg);
        throw new Exception($msg);
    }

    $result = apiCallCategoryRefer(null, $groups_id);
    if ($result['status'] == "error") {
        $msg = UI_ITEM_CATEGORY . "取得：" . UI_ITEM_CATEGORY . "情報の取得に失敗しました";
        createLogs(LOG_TYPE_ERROR, $msg);
        return [];
    }
    return $result['data']['category_list'];
}

function getCategoryListForActive($groups_id)
{
    if($groups_id == null){
        $msg = "Exception - " . UI_ITEM_CATEGORY . "取得：" . UI_ITEM_GROUPS_ID . "がnullです";
        createLogs(LOG_TYPE_ERROR, $msg);
        throw new Exception($msg);
    }

    $result = apiCallCategoryRefer(null, $groups_id);
    if ($result['status'] == "error") {
        $msg = UI_ITEM_CATEGORY . "取得：" . UI_ITEM_CATEGORY . "情報の取得に失敗しました";
        createLogs(LOG_TYPE_ERROR, $msg);
        return [];
    }

    $list = $result['data']['category_list'];
    $list = array_filter($list, function($category) {
        return $category['delete_flag'] != 1;
    });
    return array_values($list);
}

function getCategoryListForDeactive($groups_id)
{
    if($groups_id == null){
        $msg = "Exception - " . UI_ITEM_CATEGORY . "取得：" . UI_ITEM_GROUPS_ID . "がnullです";
        createLogs(LOG_TYPE_ERROR, $msg);
        throw new Exception($msg);
    }

    $result = apiCallCategoryRefer(null, $groups_id);
    if ($result['status'] == "error") {
        $msg = UI_ITEM_CATEGORY . "取得：" . UI_ITEM_CATEGORY . "情報の取得に失敗しました";
        createLogs(LOG_TYPE_ERROR, $msg);
        return [];
    }

    $list = $result['data']['category_list'];
    $list = array_filter($list, function($category) {
        return $category['delete_flag'] == 1;
    });
    return array_values($list);
}

function getCategoryIdToNameList($groups_id)
{
    if($groups_id == null){
        $msg = "Exception - " . UI_ITEM_CATEGORY . "取得：" . UI_ITEM_GROUPS_ID . "がnullです";
        createLogs(LOG_TYPE_ERROR, $msg);
        throw new Exception($msg);
    }
    $category_list=[];

    $result = apiCallCategoryRefer(null, $groups_id);
    if ($result['status'] == "error") {
        $msg = "Exception - " . UI_ITEM_CATEGORY . "取得：" . UI_ITEM_CATEGORY . "情報の取得に失敗しました";
        createLogs(LOG_TYPE_ERROR, $msg);
        return [];
    }
    $data_list = $result['data']['category_list'];
    foreach ($data_list as $data){
        if($data['delete_flag'] == "0"){
            $category_list[$data['category_id']] = $data['category_name'];
        }
    }
    
    return $category_list;
}

function getMaxCategoryNo($groups_id)
{
    $category_max_no=0;
    $result = apiCallCategoryRefer(null, $groups_id);
    if ($result['status'] == "error") {
        return $category_max_no;
    }
    $datas = $result['data']['category_list'];
    foreach ($datas as $data){
        if((Int)$data['category_no'] == 999){
            continue;
        }
        if($category_max_no<(Int)$data['category_no']){
            $category_max_no=(Int)$data['category_no'];
        }
    }
    return $category_max_no;
}