<?php

if (session_status() === PHP_SESSION_NONE) {
    session_start();
}

require_once $_SERVER['DOCUMENT_ROOT'] . '/Config/config.php';
require_once $_SERVER['DOCUMENT_ROOT'] . '/Config/log_config.php';
require_once $_SERVER['DOCUMENT_ROOT'] . '/Config/ui_items_config.php';
require_once $_SERVER['DOCUMENT_ROOT'] . '/Application/Services/ApiService.php';
require_once $_SERVER['DOCUMENT_ROOT'] . '/Interfaces/Views/Partials/requireApi.php';
require_once $_SERVER['DOCUMENT_ROOT'] . '/Interfaces/Views/Partials/category/get_category.php';
require_once $_SERVER['DOCUMENT_ROOT'] . '/Interfaces/Views/Partials/fixed/get_fixed.php';


function budgetsPostActionForNormalEntryEvent($groups_id, $yyyy, $mm, $budgets_list)
{

    // group_id不正
    if($groups_id == null){
        $msg = "Exception - " . UI_ITEM_BUDGETS . " - 通常登録アクション：" . UI_ITEM_GROUPS_ID . "がnullです";
        createLogs(LOG_TYPE_ERROR, $msg);
        throw new Exception($msg);
    }

    // yyyy 不正
    if($yyyy == null){
        $msg = "Exception - " . UI_ITEM_BUDGETS . " - 通常登録アクション：年がnullです";
        createLogs(LOG_TYPE_ERROR, $msg);
        throw new Exception($msg);
    }
    if($yyyy < 1900 OR 2200 < $yyyy){
        $msg = "Exception - " . UI_ITEM_BUDGETS . " - 通常登録アクション：年が1900～2200範囲外です value ='{$yyyy}'";
        createLogs(LOG_TYPE_ERROR, $msg);
        throw new Exception($msg);
    }

    // mm 不正
    if($mm == null){
        $msg = "Exception - " . UI_ITEM_BUDGETS . " - 通常登録アクション：月がnullです";
        createLogs(LOG_TYPE_ERROR, $msg);
        throw new Exception($msg);
    }
    if($mm < 1 OR 12 < $mm){
        $msg = "Exception - " . UI_ITEM_BUDGETS . " - 通常登録アクション：月が1～12範囲外です value ='{$mm}'";
        createLogs(LOG_TYPE_ERROR, $msg);
        throw new Exception($msg);
    }

    // 確定済み
    if(getFixed($groups_id, $yyyy, $mm)){
        $msg = "Exception - " . UI_ITEM_BUDGETS . " - 通常登録アクション：確定済みの年月です value ='{$yyyy}{$mm}'";
        createLogs(LOG_TYPE_ERROR, $msg);
        throw new Exception($msg);
    }

    // 画面側予算データリストが存在しない
    if($budgets_list == null){
        $msg = "Exception - " . UI_ITEM_BUDGETS . " - 通常登録アクション：画面で入力したデータが正常に読み込めませんでした";
        createLogs(LOG_TYPE_ERROR, $msg);
        throw new Exception($msg);
    }

    // 画面側予算データリストをループ
    foreach ($budgets_list as $budgets){

        $array = explode("\t", $budgets);
        $post_category_id = $array[0];
        $post_amount = $array[1];

        entryBudgets($groups_id, $yyyy, $mm, $post_category_id,$post_amount);
    }
    $msg = UI_ITEM_BUDGETS . " - 通常登録アクション：処理成功 groups_id={$groups_id} yyyy={$yyyy} mm={$mm}";
    createLogs(LOG_TYPE_INFO, $msg);
    return json_encode([
        "status" => "success",
        "message" => UI_ITEM_BUDGETS . "を登録しました"
    ]);
}

function budgetsPostActionForCopyEntryEvent($groups_id, $yyyy, $mm)
{

    // group_id不正
    if($groups_id == null){
        $msg = "Exception - " . UI_ITEM_BUDGETS . " - 前月コピー登録アクション：" . UI_ITEM_GROUPS_ID . "がnullです";
        createLogs(LOG_TYPE_ERROR, $msg);
        throw new Exception($msg);
    }

    // yyyy 不正
    if($yyyy == null){
        $msg = "Exception - " . UI_ITEM_BUDGETS . " - 前月コピー登録アクション：年がnullです";
        createLogs(LOG_TYPE_ERROR, $msg);
        throw new Exception($msg);
    }
    if($yyyy < 1900 OR 2200 < $yyyy){
        $msg = "Exception - " . UI_ITEM_BUDGETS . " - 前月コピー登録アクション：年が1900～2200範囲外です value ='{$yyyy}'";
        createLogs(LOG_TYPE_ERROR, $msg);
        throw new Exception($msg);
    }

    // mm 不正
    if($mm == null){
        $msg = "Exception - " . UI_ITEM_BUDGETS . " - 前月コピー登録アクション：月がnullです";
        createLogs(LOG_TYPE_ERROR, $msg);
        throw new Exception($msg);
    }
    if($mm < 1 OR 12 < $mm){
        $msg = "Exception - " . UI_ITEM_BUDGETS . " - 前月コピー登録アクション：月が1～12範囲外です value ='{$mm}'";
        createLogs(LOG_TYPE_ERROR, $msg);
        throw new Exception($msg);
    }

    // 確定済み
    if(getFixed($groups_id, $yyyy, $mm)){
        $msg = "Exception - " . UI_ITEM_BUDGETS . " - 前月コピー登録アクション：確定済みの年月です value ='{$yyyy}{$mm}'";
        createLogs(LOG_TYPE_ERROR, $msg);
        throw new Exception($msg);
    }

    // 前月を算出
    $prev = mktime(0, 0, 0, $mm - 1, 1, $yyyy);
    $prev_yyyy = date("Y", $prev);
    $prev_mm   = date("m", $prev);

    // 前月の予算を取得
    $budgets_refer_result = apiCallBudgetsRefer($groups_id, $prev_yyyy, $prev_mm);

    // 前月の予算が存在しない場合
    if ($budgets_refer_result['status'] == "error") {
        $msg = "Exception - " . UI_ITEM_BUDGETS . " - 前月コピー登録アクション：確定済みの年月です value ='{$prev_yyyy}{$prev_mm}'";
        createLogs(LOG_TYPE_ERROR, $msg);
        return json_encode([
            "status" => "error",
            "message" => "前月の" . UI_ITEM_BUDGETS . "を取得できませんでした"
        ]);
    }

    // 前月の予算をループ
    foreach ($budgets_refer_result['data']['budgets_list'] as $budgets_refer) {
        entryBudgets($groups_id, $yyyy, $mm, $budgets_refer['category_id'],$budgets_refer['amount']);
    }

    $msg = UI_ITEM_BUDGETS . " - 前月コピー登録アクション：処理成功 groups_id={$groups_id} yyyy={$yyyy} mm={$mm}";
    createLogs(LOG_TYPE_INFO, $msg);
    return json_encode([
        "status" => "success",
        "message" => $prev_yyyy . "年" . $prev_mm . "月の" . UI_ITEM_BUDGETS . "をコピーしました"
    ]);
}

function budgetsPostActionForAllEntryEvent($groups_id, $yyyy, $budgets_list)
{

    // group_id不正
    if($groups_id == null){
        $msg = "Exception - " . UI_ITEM_BUDGETS . " - 一括登録アクション：" . UI_ITEM_GROUPS_ID . "がnullです";
        createLogs(LOG_TYPE_ERROR, $msg);
        throw new Exception($msg);
    }

    // yyyy 不正
    if($yyyy == null){
        $msg = "Exception - " . UI_ITEM_BUDGETS . " - 一括登録アクション：年がnullです";
        createLogs(LOG_TYPE_ERROR, $msg);
        throw new Exception($msg);
    }
    if($yyyy < 1900 OR 2200 < $yyyy){
        $msg = "Exception - " . UI_ITEM_BUDGETS . " - 一括登録アクション：年が1900～2200範囲外です value ='{$yyyy}'";
        createLogs(LOG_TYPE_ERROR, $msg);
        throw new Exception($msg);
    }

    // 画面側予算データリストが存在しない
    if($budgets_list == null){
        $msg = "Exception - " . UI_ITEM_BUDGETS . " - 一括登録アクション：画面で入力したデータが正常に読み込めませんでした";
        createLogs(LOG_TYPE_ERROR, $msg);
        throw new Exception($msg);
    }

    // 画面側予算データリストをループ
    foreach (range(1, 12) as $mm) {
        // 確定済み
        if(getFixed($groups_id, $yyyy, $mm)){
            $msg = UI_ITEM_BUDGETS . " - 一括登録アクション：確定済みのため{$mm}月の登録をスキップします";
            createLogs(LOG_TYPE_ERROR, $msg);
            continue;
        }

        foreach ($budgets_list as $budgets){

            $array = explode("\t", $budgets);
            $post_category_id = $array[0];
            $post_amount = $array[1];

            entryBudgets($groups_id, $yyyy, $mm, $post_category_id,$post_amount);
        }
    }

    $msg = UI_ITEM_BUDGETS . " - 一括登録アクション：処理成功 groups_id={$groups_id} yyyy={$yyyy} mm={$mm}";
    createLogs(LOG_TYPE_INFO, $msg);
    return json_encode([
        "status" => "success",
        "message" => $yyyy . "年の" . UI_ITEM_BUDGETS . "を一括登録しました"
    ]);
}

function entryBudgets($groups_id, $yyyy, $mm, $post_category_id,$post_amount)
{
    if(isExistBudget($groups_id, $yyyy, $mm, $post_category_id)){

        $update_result = apiCallBudgetsUpdate($groups_id, $yyyy, $mm, $post_category_id, $post_amount);
        if($update_result['status'] == "error"){
            $msg = "Exception - " . UI_ITEM_BUDGETS . " - ：登録処理失敗 APIエラー groups_id={$groups_id} yyyy={$yyyy} mm={$mm} category_id={$post_category_id} amount={$post_amount}";
            createLogs(LOG_TYPE_ERROR, $msg);
            throw new Exception($msg);
        }

    }else{

        $create_result = apiCallBudgetsCreate($groups_id, $yyyy, $mm, $post_category_id, $post_amount);
        if($create_result['status'] == "error"){
            $msg = "Exception - " . UI_ITEM_BUDGETS . " - ：更新処理失敗 APIエラー groups_id={$groups_id} yyyy={$yyyy} mm={$mm} category_id={$post_category_id} amount={$post_amount}";
            createLogs(LOG_TYPE_ERROR, $msg);
            throw new Exception($msg);
        }
    }
}

function budgetsPostActionForDeleteEvent($groups_id, $yyyy, $mm)
{
    // group_id不正
    if($groups_id == null){
        $msg = "Exception - " . UI_ITEM_BUDGETS . " - 削除アクション：" . UI_ITEM_GROUPS_ID . "がnullです";
        createLogs(LOG_TYPE_ERROR, $msg);
        throw new Exception($msg);
    }

    // yyyy 不正
    if($yyyy == null){
        $msg = "Exception - " . UI_ITEM_BUDGETS . " - 削除アクション：年がnullです";
        createLogs(LOG_TYPE_ERROR, $msg);
        throw new Exception($msg);
    }
    if($yyyy < 1900 OR 2200 < $yyyy){
        $msg = "Exception - " . UI_ITEM_BUDGETS . " - 削除アクション：年が1900～2200範囲外です value ='{$yyyy}'";
        createLogs(LOG_TYPE_ERROR, $msg);
        throw new Exception($msg);
    }

    // mm 不正
    if($mm == null){
        $msg = "Exception - " . UI_ITEM_BUDGETS . " - 削除アクション：月がnullです";
        createLogs(LOG_TYPE_ERROR, $msg);
        throw new Exception($msg);
    }
    if($mm < 1 OR 12 < $mm){
        $msg = "Exception - " . UI_ITEM_BUDGETS . " - 削除アクション：月が1～12範囲外です value ='{$mm}'";
        createLogs(LOG_TYPE_ERROR, $msg);
        throw new Exception($msg);
    }

    // 確定済み
    if(getFixed($groups_id, $yyyy, $mm)){
        $msg = "Exception - " . UI_ITEM_BUDGETS . " - 削除アクション：確定済みの年月です value ='{$yyyy}{$mm}'";
        createLogs(LOG_TYPE_ERROR, $msg);
        throw new Exception($msg);
    }

    deleteBudgets($groups_id, $yyyy, $mm);

    $msg = UI_ITEM_BUDGETS . " - 削除アクション：処理成功 groups_id={$groups_id} yyyy={$yyyy} mm={$mm}";
    createLogs(LOG_TYPE_INFO, $msg);
    return json_encode([
        "status" => "success",
        "message" => $yyyy . "年の" . UI_ITEM_BUDGETS . "を削除しました"
    ]);

}
function budgetsPostActionForAllDeleteEvent($groups_id, $yyyy)
{
    // group_id不正
    if($groups_id == null){
        $msg = "Exception - " . UI_ITEM_BUDGETS . " - 一括削除アクション：" . UI_ITEM_GROUPS_ID . "がnullです";
        createLogs(LOG_TYPE_ERROR, $msg);
        throw new Exception($msg);
    }

    // yyyy 不正
    if($yyyy == null){
        $msg = "Exception - " . UI_ITEM_BUDGETS . " - 一括削除アクション：年がnullです";
        createLogs(LOG_TYPE_ERROR, $msg);
        throw new Exception($msg);
    }
    if($yyyy < 1900 OR 2200 < $yyyy){
        $msg = "Exception - " . UI_ITEM_BUDGETS . " - 一括削除アクション：年が1900～2200範囲外です value ='{$yyyy}'";
        createLogs(LOG_TYPE_ERROR, $msg);
        throw new Exception($msg);
    }

    foreach (range(1, 12) as $mm) {
        // 確定済み
        if(getFixed($groups_id, $yyyy, $mm)){
            $msg = UI_ITEM_BUDGETS . " - 一括削除アクション：確定済みのため{$mm}月の削除をスキップします";
            createLogs(LOG_TYPE_ERROR, $msg);
            continue;
        }

        if(!isExistBudget($groups_id, $yyyy, $mm)){
            $msg = UI_ITEM_BUDGETS . " - 一括削除アクション：データが存在しないため{$mm}月の削除をスキップします";
            createLogs(LOG_TYPE_ERROR, $msg);
            continue;
        }

        deleteBudgets($groups_id, $yyyy, $mm);
    }

    $msg = UI_ITEM_BUDGETS . " - 削除アクション：処理成功 groups_id={$groups_id} yyyy={$yyyy} mm={$mm}";
    createLogs(LOG_TYPE_INFO, $msg);
    return json_encode([
        "status" => "success",
        "message" => $yyyy . "年の" . UI_ITEM_BUDGETS . "を削除しました"
    ]);

}
function deleteBudgets($groups_id, $yyyy, $mm)
{
    $create_result = apiCallBudgetsDelete($groups_id, $yyyy, $mm);
    if($create_result['status'] == "error"){
        $msg = "Exception - " . UI_ITEM_BUDGETS . " - ：削除処理失敗 APIエラー groups_id={$groups_id} yyyy={$yyyy} mm={$mm}";
        createLogs(LOG_TYPE_ERROR, $msg);
        throw new Exception($msg);
    }
}