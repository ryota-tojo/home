<?php

if (session_status() === PHP_SESSION_NONE) {
    session_start();
}

require_once $_SERVER['DOCUMENT_ROOT'] . '/Config/config.php';
require_once $_SERVER['DOCUMENT_ROOT'] . '/Config/log_config.php';
require_once $_SERVER['DOCUMENT_ROOT'] . '/Config/ui_items_config.php';
require_once $_SERVER['DOCUMENT_ROOT'] . '/Application/Services/ApiService.php';
require_once $_SERVER['DOCUMENT_ROOT'] . '/Interfaces/Views/Partials/requireApi.php';

function getFixed($groups_id, $yyyy, $mm)
{

    if ($groups_id == null) {
        $msg = "Exception - " . UI_ITEM_FIXED . " - 取得：" . UI_ITEM_GROUPS_ID . "がnullです";
        createLogs(LOG_TYPE_ERROR, $msg);
        throw new Exception($msg);
    }

    if ($yyyy == null) {
        $msg = "Exception - " . UI_ITEM_FIXED . " - 取得：yyyyがnullです";
        createLogs(LOG_TYPE_ERROR, $msg);
        throw new Exception($msg);
    }

    if ($mm == null) {
        $msg = "Exception - " . UI_ITEM_FIXED . " - 取得：mmがnullです";
        createLogs(LOG_TYPE_ERROR, $msg);
        throw new Exception($msg);
    }

    $result = apiCallFixedRefer($groups_id, $yyyy);

    if ($result['status'] != "success") {
        $msg = "Exception - " . UI_ITEM_FIXED . " - 取得：APIの実行に失敗しました";
        createLogs(LOG_TYPE_ERROR, $msg);
        throw new Exception($msg);
    }

    $fixed="";
    switch ($mm) {
        case 1:
            $fixed = $result['data']['fixed_result']['month_01'];
            break;
        case 2:
            $fixed = $result['data']['fixed_result']['month_02'];
            break;
        case 3:
            $fixed = $result['data']['fixed_result']['month_03'];
            break;
        case 4:
            $fixed = $result['data']['fixed_result']['month_04'];
            break;
        case 5:
            $fixed = $result['data']['fixed_result']['month_05'];
            break;
        case 6:
            $fixed = $result['data']['fixed_result']['month_06'];
            break;
        case 7:
            $fixed = $result['data']['fixed_result']['month_07'];
            break;
        case 8:
            $fixed = $result['data']['fixed_result']['month_08'];
            break;
        case 9:
            $fixed = $result['data']['fixed_result']['month_09'];
            break;
        case 10:
            $fixed = $result['data']['fixed_result']['month_10'];
            break;
        case 11:
            $fixed = $result['data']['fixed_result']['month_11'];
            break;
        case 12:
            $fixed = $result['data']['fixed_result']['month_12'];
            break;

        default:
            $msg = "Exception - " . UI_ITEM_BUDGETS . " - 確定データ参照結果から対象月を抽出：月の指定方法が不正です";
            createLogs(LOG_TYPE_ERROR, $msg);
            throw new Exception($msg);
            break;

    }
    return $fixed;
}
