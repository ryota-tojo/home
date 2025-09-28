<?php

if (session_status() === PHP_SESSION_NONE) {
    session_start();
}

require_once $_SERVER['DOCUMENT_ROOT'] . '/Config/config.php';
require_once $_SERVER['DOCUMENT_ROOT'] . '/Config/log_config.php';
require_once $_SERVER['DOCUMENT_ROOT'] . '/Config/ui_items_config.php';
require_once $_SERVER['DOCUMENT_ROOT'] . '/Application/Services/ApiService.php';
require_once $_SERVER['DOCUMENT_ROOT'] . '/Interfaces/Views/Partials/requireApi.php';

function isDuplicationForSearchTemplate($groups_id,$template_id)
{
    if($groups_id == null){
        $msg = "Exception - " . UI_ITEM_SEARCH_TEMPLATE . "重複チェック：" . UI_ITEM_GROUPS_ID . "がnullです";
        createLogs(LOG_TYPE_ERROR, $msg);
        throw new Exception($msg);
    }
    if($template_id == null){
        $msg = "Exception - " . UI_ITEM_SEARCH_TEMPLATE . "重複チェック：" . UI_ITEM_SEARCH_TEMPLATE . "IDがnullです";
        createLogs(LOG_TYPE_ERROR, $msg);
        throw new Exception($msg);
    }

    $result = apiCallSearchTemplateRefer($groups_id,$template_id);
    if ($result['status'] != "error") {
        return True;
    }
    return False;
}