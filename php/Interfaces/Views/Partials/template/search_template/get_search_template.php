<?php

if (session_status() === PHP_SESSION_NONE) {
    session_start();
}

require_once $_SERVER['DOCUMENT_ROOT'] . '/Config/config.php';
require_once $_SERVER['DOCUMENT_ROOT'] . '/Config/log_config.php';
require_once $_SERVER['DOCUMENT_ROOT'] . '/Config/ui_items_config.php';
require_once $_SERVER['DOCUMENT_ROOT'] . '/Application/Services/ApiService.php';
require_once $_SERVER['DOCUMENT_ROOT'] . '/Interfaces/Views/Partials/requireApi.php';

function isExistSearchTemplate($groups_id = null, $template_id = null){
    $result = apiCallSearchTemplateRefer($groups_id, $template_id);
    if ($result['status'] == "success") {
        return True;
    }
    return False;
}

function getSearchTemplate($groups_id,$template_id)
{

    if($groups_id == null){
        $msg = "Exception - " . UI_ITEM_SEARCH_TEMPLATE . "取得：" . UI_ITEM_GROUPS_ID . "がnullです";
        createLogs(LOG_TYPE_ERROR, $msg);
        throw new Exception($msg);
    }
    if($template_id == null){
        $msg = "Exception - " . UI_ITEM_SEARCH_TEMPLATE . "取得：" . UI_ITEM_SEARCH_TEMPLATE . "IDがnullです";
        createLogs(LOG_TYPE_ERROR, $msg);
        throw new Exception($msg);
    }

    $result = apiCallSearchTemplateRefer($groups_id, $template_id);
    if ($result['status'] == "error") {
        $msg = UI_ITEM_SEARCH_TEMPLATE . "取得：" . UI_ITEM_SEARCH_TEMPLATE . "情報の取得に失敗しました";
        createLogs(LOG_TYPE_ERROR, $msg);
        return [
            'groups_id' => UI_ITEM_LABEL_NULL,
            'template_no' => UI_ITEM_LABEL_NULL,
            'template_id' => UI_ITEM_LABEL_NULL,
            'template_name' => UI_ITEM_LABEL_NULL,
            'member_id' => UI_ITEM_LABEL_NULL,
            'category_id' => UI_ITEM_LABEL_NULL,
            'type' => UI_ITEM_LABEL_NULL,
            'payment' => UI_ITEM_LABEL_NULL,
            'settlement' => UI_ITEM_LABEL_NULL,
            'min_amount' => UI_ITEM_LABEL_NULL,
            'max_amount' => UI_ITEM_LABEL_NULL,
            'remarks' => UI_ITEM_LABEL_NULL,
            'use' => UI_ITEM_LABEL_NULL,
            'deleted' => UI_ITEM_LABEL_NULL,
        ];
    }

    $data_list = $result['data']['template_list'];
    $first = $data_list[0];
    return [
        'groups_id' => $first['groups_id'],
        'template_no' => $first['template_no'],
        'template_id' => $first['template_id'],
        'template_name' => $first['template_name'],
        'member_id' => $first['member_id'],
        'category_id' => $first['category_id'],
        'type' => $first['type'],
        'payment' => $first['payment'],
        'settlement' => $first['settlement'],
        'min_amount' => $first['min_amount'],
        'max_amount' => $first['max_amount'],
        'remarks' => $first['remarks'],
        'use' => $first['use'],
        'deleted' => $first['deleted'],
    ];
}

function getSearchTemplateList($groups_id)
{

    if($groups_id == null){
        $msg = "Exception - " . UI_ITEM_SEARCH_TEMPLATE . "取得：" . UI_ITEM_GROUPS_ID . "がnullです";
        createLogs(LOG_TYPE_ERROR, $msg);
        throw new Exception($msg);
    }

    $result = apiCallSearchTemplateRefer($groups_id, null);
    if ($result['status'] == "error") {
        $msg = UI_ITEM_SEARCH_TEMPLATE . "取得：" . UI_ITEM_SEARCH_TEMPLATE . "情報の取得に失敗しました";
        createLogs(LOG_TYPE_ERROR, $msg);
        return [];
    }
    return $result['data']['template_list'];
}

function getSearchTemplateCount($groups_id)
{
    $cnt = 0;
    $result = apiCallSearchTemplateRefer($groups_id, null);

    if (isset($result['status']) && $result['status'] != "error"
        && isset($result['data']['template_list'])
        && is_array($result['data']['template_list'])) {

        $datas = $result['data']['template_list'];
        foreach ($datas as $data) {
            $cnt++;
        }
    }
    return $cnt;
}

function getMaxSearchTemplateNo($groups_id)
{
    $template_max_no=0;
    $result = apiCallSearchTemplateRefer($groups_id);
    if ($result['status'] == "error") {
        return 0;
    }
    $datas = $result['data']['template_list'];
    foreach ($datas as $data){
        if((Int)$data['template_no'] == 999){
            continue;
        }
        if($template_max_no<(Int)$data['template_no']){
            $template_max_no=(Int)$data['template_no'];
        }
    }
    return $template_max_no;
}