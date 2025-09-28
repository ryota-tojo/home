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

function isExistBudget($groups_id, $yyyy, $mm, $category_id = null)
{
    if ($groups_id == null) {
        $msg = "Exception - " . UI_ITEM_BUDGETS . " - 存在チェック：" . UI_ITEM_GROUPS_ID . "がnullです";
        createLogs(LOG_TYPE_ERROR, $msg);
        throw new Exception($msg);
    }

    if ($mm < 1 or 12 < $mm) {
        $msg = "Exception - " . UI_ITEM_BUDGETS . " - 存在チェック：" . $mm . "が不正値です";
        createLogs(LOG_TYPE_ERROR, $msg);
        throw new Exception($msg);
    }

    $budgets_refer_result = apiCallBudgetsRefer($groups_id, $yyyy, $mm, $category_id);
    if ($budgets_refer_result['status'] == "error") {
        return False;
    }
    return True;
}

function getBudgetsForCategoryAndAmount($groups_id, $yyyy, $mm)
{

    if ($groups_id == null) {
        $msg = "Exception - " . UI_ITEM_BUDGETS . " - リスト取得：" . UI_ITEM_GROUPS_ID . "がnullです";
        createLogs(LOG_TYPE_ERROR, $msg);
        throw new Exception($msg);
    }

    if ($mm < 1 or 12 < $mm) {
        $msg = "Exception - " . UI_ITEM_BUDGETS . " - リスト取得：" . $mm . "が不正値です";
        createLogs(LOG_TYPE_ERROR, $msg);
        throw new Exception($msg);
    }

    // 予算を参照する
    $budgets_refer_result = apiCallBudgetsRefer($groups_id, $yyyy, $mm);

    $budgets_list = [];
    // 存在しない場合
    if ($budgets_refer_result['status'] == 'error') {
        //既存カテゴリー（有効のみ）/ 金額０ のリストを返す
        $category_refer_result = apiCallCategoryRefer(null, $groups_id);
        if ($category_refer_result['status'] == "success") {
            foreach ($category_refer_result['data']['category_list'] as $category_refer) {
                if ($category_refer['delete_flag'] == 0) {
                    $budgets_list[] = [
                        'budgets_category_type' => "新規",
                        'category_id' => $category_refer['category_id'],
                        'category_name' => $category_refer['category_name'],
                        'amount' => 0
                    ];
                }
            }
        }
        return $budgets_list;

        // 存在する場合
    } else {
        // 確定データを参照
        $fixed = getFixed($groups_id, $yyyy, $mm);

        // 確定している場合
        if ($fixed != False) {
            // 予算データをそのまま（確定後に削除されたカテゴリーは「削除済みのカテゴリー」として表示）
            if ($budgets_refer_result['status'] == "success") {
                foreach ($budgets_refer_result['data']['budgets_list'] as $budgets_refer) {

                    $category_name = "削除済みのカテゴリー";
                    $category = getCategory($budgets_refer['category_id'], $groups_id);
                    if ($category != null) {
                        $category_name = $category['category_name'];
                    }

                    $budgets_list[] = [
                        'budgets_category_type' => "確定済",
                        'category_id' => $budgets_refer['category_id'],
                        'category_name' => $category_name,
                        'amount' => $budgets_refer['amount']
                    ];
                }
            }
            return $budgets_list;

            // 確定していない場合
        } else {
            // 有効のカテゴリー / 該当する金額を表示
            $exist_category_id_list = [];
            $active_category_list = getCategoryListForActive($groups_id);
            foreach ($active_category_list as $active_category) {
                $exist_flag = false;
                foreach ($budgets_refer_result['data']['budgets_list'] as $budgets_refer) {
                    if ($active_category['category_id'] == $budgets_refer['category_id']) {

                        $category_name = "カテゴリーの取得に失敗";
                        $category = getCategory($active_category['category_id'], $groups_id);
                        if ($category != null) {
                            $category_name = $category['category_name'];
                        }

                        $budgets_list[] = [
                            'budgets_category_type' => "未確定 有効カテゴリー 予算あり",
                            'category_id' => $active_category['category_id'],
                            'category_name' => $category_name,
                            'amount' => $budgets_refer['amount']
                        ];
                        $exist_category_id_list[] = $active_category['category_id'];
                        $exist_flag = True;
                    }
                }
                if ($exist_flag == False) {
                    $category_name = "カテゴリーの取得に失敗";
                    $category = getCategory($active_category['category_id'], $groups_id);
                    if ($category != null) {
                        $category_name = $category['category_name'];
                    }

                    $budgets_list[] = [
                        'budgets_category_type' => "未確定 有効カテゴリー 予算なし",
                        'category_id' => $active_category['category_id'],
                        'category_name' => $category_name,
                        'amount' => 0
                    ];
                    $exist_category_id_list[] = $active_category['category_id'];
                }
            }

            // 無効のカテゴリー / 予算金額が0円以外のもの
            $deactive_category_list = getCategoryListForDeactive($groups_id);
            foreach ($deactive_category_list as $deactive_category) {
                foreach ($budgets_refer_result['data']['budgets_list'] as $budgets_refer) {
                    if ($deactive_category['category_id'] == $budgets_refer['category_id']) {

                        $category_name = "カテゴリーの取得に失敗";
                        $category = getCategory($deactive_category['category_id'], $groups_id);
                        if ($category != null) {
                            $category_name = $category['category_name'];
                        }

                        if ($budgets_refer['amount'] != 0) {
                            $budgets_list[] = [
                                'budgets_category_type' => "未確定 無効カテゴリー 予算あり",
                                'category_id' => $deactive_category['category_id'],
                                'category_name' => $category_name,
                                'amount' => $budgets_refer['amount']
                            ];
                        }
                        $exist_category_id_list[] = $deactive_category['category_id'];
                    }
                }
            }

            // 削除済みのカテゴリー / 予算金額が0円以外のもの（確定⇒カテゴリー削除⇒確定解除すると出来上がるデータ）
            // 予算に存在してカテゴリーに存在しないもの
            foreach ($budgets_refer_result['data']['budgets_list'] as $budgets_refer) {
                if (in_array($budgets_refer['category_id'], $exist_category_id_list)) {
                    continue;
                }
                if ($budgets_refer['amount'] != 0) {
                    $budgets_list[] = [
                        'budgets_category_type' => "未確定 削除済カテゴリー",
                        'category_id' => $budgets_refer['category_id'],
                        'category_name' => "削除済みのカテゴリー",
                        'amount' => $budgets_refer['amount']
                    ];
                }
            }
            return $budgets_list;
        }
    }
}

function getBudgetsCategoryIdListForTargetYear($groups_id, $yyyy)
{
    // group_id不正
    if ($groups_id == null) {
        $msg = "Exception - " . UI_ITEM_BUDGETS . " - 一括取得アクション：" . UI_ITEM_GROUPS_ID . "がnullです";
        createLogs(LOG_TYPE_ERROR, $msg);
        throw new Exception($msg);
    }

    // yyyy 不正
    if ($yyyy == null) {
        $msg = "Exception - " . UI_ITEM_BUDGETS . " - 一括取得アクション：年がnullです";
        createLogs(LOG_TYPE_ERROR, $msg);
        throw new Exception($msg);
    }
    if ($yyyy < 1900 or 2200 < $yyyy) {
        $msg = "Exception - " . UI_ITEM_BUDGETS . " - 一括取得アクション：年が1900～2200範囲外です value ='{$yyyy}'";
        createLogs(LOG_TYPE_ERROR, $msg);
        throw new Exception($msg);
    }

    // ヘッダ
    $header = getBudgetsListHeader($groups_id, $yyyy);

    // ボディ
    $body = getBudgetsListBody($groups_id, $yyyy, $header);

    // footer
    $footer = getBudgetsListFooter($groups_id, $yyyy);

    return [
        "header" => $header,
        "body" => $body,
        "footer" => $footer
    ];
}

function getBudgetsListHeader($groups_id, $yyyy)
{
    // 返却値：（状態、カテゴリーID）のリスト

    // 有効なカテゴリーを取得（Active）
    $active_category_id_list = [];
    $active_category_status_and_id_list = [];
    $active_category_list = getCategoryListForActive($groups_id);
    foreach ($active_category_list as $active_category) {
        $active_category_id_list[] = $active_category['category_id'];
        $active_category_status_and_id_list[] = [
            "status" => "active",
            "category_id" => $active_category['category_id'],
            "category_name" => getCategory($active_category['category_id'], $groups_id)['category_name']
        ];
    }

    // 無効なカテゴリーを取得（DeActive）
    $de_active_category_id_list = [];
    $de_active_category_status_and_id_list = [];
    $de_active_category_list = getCategoryListForDeactive($groups_id);
    foreach ($de_active_category_list as $de_active_category) {
        $de_active_category_id_list[] = $de_active_category['category_id'];
        $de_active_category_status_and_id_list[] = [
            "status" => "de_active",
            "category_id" => $de_active_category['category_id'],
            "category_name" => getCategory($de_active_category['category_id'], $groups_id)['category_name']
        ];
    }

    // 年間の予算からカテゴリーIDを取得
    $budgets_for_category_status_and_id_list = [];
    foreach (range(1, 12) as $mm) {
        $budgets_list = getBudgetsForCategoryAndAmount($groups_id, $yyyy, $mm);
        foreach ($budgets_list as $budgets) {
            $budgets_for_category_status_and_id_list[] = [
                "status" => "deleted",
                "category_id" => $budgets['category_id'],
                "category_name" => getCategory($budgets['category_id'], $groups_id)['category_name']
            ];

        }
    }
    $budgets_for_category_status_and_id_list_duplication_deleted = array_map(
        "unserialize",
        array_unique(array_map("serialize", $budgets_for_category_status_and_id_list))
    );

    // 予算から取得したカテゴリーから有効カテゴリー、無効カテゴリーを除外する
    $delete_category_id_list = array_unique(array_merge($active_category_id_list, $de_active_category_id_list));
    $budgets_only_status_and_id_list = array_values(array_filter(
        $budgets_for_category_status_and_id_list_duplication_deleted,
        function ($item) use ($delete_category_id_list) {
            return !in_array($item["category_id"], $delete_category_id_list, true);
        }
    ));

    //配列を結合
    $category_id_list = [];
    $category_id_list = array_merge($category_id_list, $active_category_status_and_id_list);
    $category_id_list = array_merge($category_id_list, $de_active_category_status_and_id_list);
    $category_id_list = array_merge($category_id_list, $budgets_only_status_and_id_list);

    return $category_id_list;

}

function getBudgetsListBody($groups_id, $yyyy, $header)
{
    // [カテゴリーid],[1月予算]～[12月予算][平均][合計]の配列

    $budgets_data = [];
    foreach ($header as $header_data) {

        $monthly_amounts = [];
        foreach (range(1, 12) as $mm) {

            $result = apiCallBudgetsRefer($groups_id, $yyyy, $mm, $header_data['category_id']);
            $amount = 0;
            if($result['status'] == 'success'){
                $amount = $result['data']['budgets_list'][0]['amount'];
            }
            $monthly_amounts[sprintf('month%02d_amount', $mm)] = $amount;
        }

        $non_zero_values = array_filter($monthly_amounts, function($val) {
            return $val != 0;
        });
        $average = count($non_zero_values) > 0 ? array_sum($non_zero_values) / count($non_zero_values) : 0;

        $total = array_sum($monthly_amounts);
        $budgets_data[] = [
            "category_id" => $header_data['category_id'],
            "monthly" => $monthly_amounts,
            "average_amount" => $average,
            "total_amount" => $total
        ];
    }
    return $budgets_data;
}

function getBudgetsListFooter($groups_id, $yyyy)
{
    $budgets_data = [];
    $monthly_amounts = [];
    foreach (range(1, 12) as $mm) {
        $result = apiCallBudgetsRefer($groups_id, $yyyy, $mm);
        $amount = 0;
        if($result['status'] == 'success'){
            foreach ($result['data']['budgets_list'] as $budgets){
                $amount += $budgets['amount'];
            }
        }
        $monthly_amounts[sprintf('month%02d_amount', $mm)] = $amount;
    }
    $non_zero_values = array_filter($monthly_amounts, function($val) {
        return $val != 0;
    });
    $average = count($non_zero_values) > 0 ? array_sum($non_zero_values) / count($non_zero_values) : 0;

    $total = array_sum($monthly_amounts);
    $budgets_data[] = [
        "monthly" => $monthly_amounts,
        "average_amount" => $average,
        "total_amount" => $total
    ];
    return $budgets_data;
}