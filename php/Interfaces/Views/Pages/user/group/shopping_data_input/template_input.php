<?php
if (session_status() === PHP_SESSION_NONE) {
    session_start();
}

require_once $_SERVER['DOCUMENT_ROOT'] . '/Config/config.php';
require_once $_SERVER['DOCUMENT_ROOT'] . '/Config/log_config.php';
require_once $_SERVER['DOCUMENT_ROOT'] . '/Application/Services/ApiService.php';
require_once $_SERVER['DOCUMENT_ROOT'] . '/Interfaces/Views/Partials/requireApi.php';

require_once $_SERVER['DOCUMENT_ROOT'] . '/Interfaces/Views/Partials/systems/logs/create_logs.php';
require_once $_SERVER['DOCUMENT_ROOT'] . '/Interfaces/Views/Partials/systems/screen/get_screen.php';

require_once $_SERVER['DOCUMENT_ROOT'] . '/Interfaces/Views/Partials/util/replace.php';

require_once $_SERVER['DOCUMENT_ROOT'] . '/Interfaces/Views/Partials/group/import_group.php';
require_once $_SERVER['DOCUMENT_ROOT'] . '/Interfaces/Views/Partials/member/get_member.php';
require_once $_SERVER['DOCUMENT_ROOT'] . '/Interfaces/Views/Partials/category/get_category.php';
require_once $_SERVER['DOCUMENT_ROOT'] . '/Interfaces/Views/Partials/shopping/import_shopping_data.php';
require_once $_SERVER['DOCUMENT_ROOT'] . '/Interfaces/Views/Partials/template/import_entry_template.php';
require_once $_SERVER['DOCUMENT_ROOT'] . '/Interfaces/Views/Partials/fixed/import_fixed.php';

$screen_items = getScreen(basename(__FILE__));
$screen_title = $screen_items['name'];
$screen_remarks = $screen_items['remarks'];

// 管理者判定
$admin_flag = 0;

// 所属グループ判定
if ($_SESSION['user_groups_id'] == null or $_SESSION['user_group_approval_flg'] == 0) {
    echo "<script>window.location.href = '/Interfaces/Views/Partials/home.php';</script>";
}

ob_start();
?>

<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><?php echo $screen_title; ?></title>
    <link rel="stylesheet" href="/Interfaces/Assets/CSS/font.css">
    <link rel="stylesheet" href="/Interfaces/Assets/CSS/home.css">
    <link rel="stylesheet" href="/Interfaces/Assets/CSS/shopping_input_form.css">
    <link rel="stylesheet" href="/Interfaces/Assets/CSS/budgets_input_form.css">
    <link rel="stylesheet" href="/Interfaces/Assets/CSS/message.css">
    <link rel="stylesheet" href="/Interfaces/Assets/CSS/table_form.css">
    <link rel="stylesheet" href="/Interfaces/Assets/CSS/button_form.css">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha3/dist/css/bootstrap.min.css" rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha3/dist/js/bootstrap.bundle.min.js"></script>
</head>

<body class="<?php if ($admin_flag == 1) {
    echo 'admin-body';
} else {
    echo 'body';
} ?>">
<header>
    <?php require $_SERVER['DOCUMENT_ROOT'] . '/Interfaces/Views/Partials/nav.php'; ?>
</header>

<?php
ob_flush();
flush();

// 変数初期化
$entry_button_click_flg = False;
$message = "";
$entry_error = False;

// マスター設定
// なし

// 所属グループ取得
$group_data = getGroup($_SESSION['user_groups_id']);
$group = json_decode($group_data, true);
if ($group['status'] == 'error') {
    $_SESSION['access_error'] = 1;
    echo "<script>window.location.href = '/Interfaces/Views/Pages/access_error.php';</script>";
}

// 対象年月設定
if (!isset($_SESSION['target_yyyy'])) {
    $_SESSION['target_yyyy'] = $group['data']['group_setting']['display_year'];
}
if (!isset($_SESSION['target_mm'])) {
    $_SESSION['target_mm'] = date("m");
}

// ボタン押下時の処理
if (isset($_POST['apply'])) {
    $entry_button_click_flg = True;
    $_SESSION['target_mm'] = $_POST['month'];
    $message = "対象月を変更しました";
}

if (isset($_POST['entry'])) {
    $entry_button_click_flg = True;

    if (isset($_POST['selected_datas'])) {

        $selected_datas = [];
        foreach ($_POST['selected_datas'] as $index => $data) {
            $array = explode("\t", $data);
            $template_no = $array[0];

            $replace_item = $_POST['replace-item'][$index] ?? '';
            $remarks = $replace_item !== '' ? replacement($array[9], $replace_item) : $array[9];

            $newArray = [
                $template_no,
                $array[1], // template_id
                $array[2], // template_name
                $array[3], // member_id
                $array[4], // category_id
                $array[5], // type
                $array[6], // payment
                $array[7], // settlement
                $array[8], // amount
                $remarks,
                $array[10], // use
                $array[11]  // deleted
            ];

            $newString = implode("\t", $newArray);
            $selected_datas[] = $newString;
        }

        $result_data = shoppingDataPostActionForEntryTemplateInputEvent($_SESSION['user_groups_id'],$_SESSION['user_id'],$_POST['target_date'],$selected_datas);
        $result = json_decode($result_data,True);
        if($result['status'] == 'error'){
            $entry_error = true;
        }
        $message = $result['message'];

    } else {
        $message = UI_ITEM_ENTRY_TEMPLATE . "が選択されていません";
        $entry_error = true;
        createLogs(LOG_TYPE_ERROR, UI_ITEM_ENTRY_TEMPLATE . "入替 - " . UI_ITEM_ENTRY_TEMPLATE . "未選択");
    }
}

// 確定判定
$fixed_label = "";
$fixed = getFixed($_SESSION['user_groups_id'], $_SESSION['target_yyyy'], $_SESSION['target_mm']);
if ($fixed == True) {
    $fixed_label = " disabled ";
}

$shopping_item_list = getShoppingItemList($_SESSION['user_groups_id']);
$member_list = $shopping_item_list['member_list'];
$category_list = $shopping_item_list['category_list'];
$type_list = $shopping_item_list['type_list'];
$payment_list = $shopping_item_list['payment_list'];
$settlement_list = $shopping_item_list['settlement_list'];
$template_data_list = getEntryTemplateListForActive($_SESSION['user_groups_id']);

?>

<main>
    <div class="title-area">
        <h2 class="title"><?php echo $screen_title; ?></h2>
    </div>
    <div class="summary-area">
        <div class="summary">
            <?php echo $screen_remarks; ?>
        </div>
    </div>

    <?php
    if ($entry_button_click_flg == True) {
        if ($entry_error == True) {
            echo "<div class='message-fields error-message'>$message</div>";
        } else {
            echo "<div class='message-fields success-message'>$message</div>";
        }
    }
    ?>

    <div class="contents">
        <div class="content-row">
            <div class="left">
            </div>
            <div class="center">
                <form action="" method="post">
                    <div class="form-area">

                        <!-- 日付 -->
                        <div class="form-item">
                            <div class="form-item-label">
                                <label class="item-label">対象月</label>
                            </div>
                            <div class="input-group form-item">
                                <div class="year-form">
                                    <select disabled required class="form-select" name="">
                                        <option value=<?php echo $_SESSION['target_yyyy']; ?>><?php echo $_SESSION['target_yyyy']; ?>
                                            年
                                        </option>
                                    </select>
                                </div>

                                <div class="month-form" style="margin-left: 10px">
                                    <select required class="form-select" name="month">
                                        <?php
                                        for ($i = 1; $i <= 12; $i++) {
                                            $selected_month = "";
                                            if ($_SESSION['target_mm'] == $i) {
                                                $selected_month = " selected ";
                                            }
                                            echo "<option {$selected_month} value='{$i}'>{$i}月</option>";
                                        }
                                        ?>
                                    </select>
                                </div>

                                <div class="date-btn-form">
                                    <button type="submit" class="btn btn-primary date-btn-item" name="apply">
                                        適用
                                    </button>
                                </div>
                            </div>

                            <div style="display: flex; justify-content: center; margin-top: 15px;">
                                <div class="info_msg" style="font-size: 120%">
                                    <?php
                                    if ($fixed == True) {
                                        echo "＊＊＊＊＊ {$_SESSION['target_yyyy']}年 {$_SESSION['target_mm']}月のデータは確定されています ＊＊＊＊＊";
                                    }
                                    ?>
                                </div>
                            </div>
                        </div>
                    </div>
                </form>
            </div>
            <div class="right">
            </div>
        </div>
    </div>
</main>

<form method="post" action="">

    <div class="btn-area">
        <div class="btn-left-area">

        </div>
        <div class="btn-right-area">
            <div class="btn-item">
                <input <?php echo $fixed_label; ?> type="date" class="form-select" name = "target_date"
                    <?php
                    $start_day = "{$_SESSION['target_yyyy']}-{$_SESSION['target_mm']}-01";
                    $date = new DateTime($start_day);
                    $last_day = "{$_SESSION['target_yyyy']}-{$_SESSION['target_mm']}-{$date->format('t')}";
                    echo "min='{$start_day}' max='{$last_day}'";

                    if($entry_button_click_flg and $entry_error and isset($_POST['target_date'])){
                        echo " value='{$_POST['target_date']}'";
                    }
                    ?>
                >
            </div>
            <div class="btn-item">
                <button <?php echo $fixed_label; ?> type="submit" class="btn btn-primary" name="entry"
                        onclick="return checkAllAndConfirm();">
                    一括登録
                </button>
            </div>
        </div>
    </div>

    <div class="table-main">
        <div class="table-area">
            <table class="table table-light table-striped table-bordered table-hover">
                <thead class="table-dark">
                <tr>
                    <th><input type="checkbox" id="select-all" onclick="toggleAll(this)"></th>
                    <th><?php echo UI_ITEM_ENTRY_TEMPLATE_NO; ?></th>
                    <th><?php echo UI_ITEM_ENTRY_TEMPLATE_ID; ?></th>
                    <th><?php echo UI_ITEM_ENTRY_TEMPLATE_NAME; ?></th>
                    <th><?php echo UI_ITEM_ENTRY_TEMPLATE_MEMBER; ?></th>
                    <th><?php echo UI_ITEM_ENTRY_TEMPLATE_CATEGORY; ?></th>
                    <th><?php echo UI_ITEM_ENTRY_TEMPLATE_TYPE; ?></th>
                    <th><?php echo UI_ITEM_ENTRY_TEMPLATE_PAYMENT; ?></th>
                    <th><?php echo UI_ITEM_ENTRY_TEMPLATE_SETTLEMENT; ?></th>
                    <th><?php echo UI_ITEM_ENTRY_TEMPLATE_AMOUNT; ?></th>
                    <th><?php echo UI_ITEM_ENTRY_TEMPLATE_REMARKS; ?></th>
                    <th><a href="#" data-bs-toggle="tooltip" title="<?php
                        echo UI_ITEM_SHOPPING_REPLACE . "は、" . UI_ITEM_ENTRY_TEMPLATE_REMARKS . "の %s を置き換えます。\n";
                        echo "例えば\n";
                        echo "・" . UI_ITEM_ENTRY_TEMPLATE_REMARKS . "：使用期間：%s～%s、単価:%s円\n";
                        echo "・" . UI_ITEM_SHOPPING_REPLACE . "：7/2,8/1,200\n";
                        echo "の場合、\n";
                        echo "「使用期間：7/2～8/1、単価:200円」になります。";
                        ?>"><?php echo UI_ITEM_SHOPPING_REPLACE; ?></a></th>
                </tr>
                </thead>
                <tbody id="sortable-table">
                <?php
                if (!isset($template_data_list)) {
                    $template_data_list = [];
                }
                foreach ($template_data_list as $template_data) {
                    $template_no = $template_data['template_no'];
                    $template_id = $template_data['template_id'];
                    $template_name = $template_data['template_name'];
                    $member_id = $template_data['member_id'];
                    $category_id = $template_data['category_id'];
                    $type = $template_data['type'];
                    $payment = $template_data['payment'];
                    $settlement = $template_data['settlement'];
                    $amount = $template_data['amount'];
                    $remarks = $template_data['remarks'];
                    $use_value = $template_data['use'];
                    $deleted_value = $template_data['deleted'];

                    $member_name = '';
                    $member_data = getMember($member_id, $_SESSION['user_groups_id']);
                    $member_name = $member_data['member_name'] ?? "";
                    $member_status = '';
                    if(isActiveMember($member_id, $_SESSION['user_groups_id']) == "isDeActive"){
                        continue;
                    }

                    $category_name = '';
                    $category_data = getCategory($category_id, $_SESSION['user_groups_id']);
                    $category_name = $category_data['category_name'] ?? "";
                    $category_status = '';
                    if(isActiveCategory($category_id, $_SESSION['user_groups_id']) == "isDeActive"){
                        continue;
                    }

                    $choices_type_item_name = '';
                    $choices_payment_item_name = '';
                    $choices_settlement_item_name = '';
                    $choices_api_refer_result = apiCallMasterChoicesRefer();
                    if ($choices_api_refer_result['status'] != "error") {
                        $choices_data = $choices_api_refer_result['data']['choices_list'];
                    }
                    foreach ($choices_data as $choices) {
                        if ($choices['item_type'] == 'type' and $choices['item_no'] == $type) {
                            $choices_type_item_name = $choices['item_name_pc'];
                        }
                        if ($choices['item_type'] == 'payment' and $choices['item_no'] == $payment) {
                            $choices_payment_item_name = $choices['item_name_pc'];
                        }
                        if ($choices['item_type'] == 'settlement' and $choices['item_no'] == $settlement) {
                            $choices_settlement_item_name = $choices['item_name_pc'];
                        }
                    }

                    $id_data = "{$template_id}";
                    if ($_SESSION['user_group_leader'] == 1) {
                        $id_data = "<a href='/Interfaces/Views/Pages/user/group/setting/template/entry_template/user_group_entry_template_update.php?template_id={$template_id}'>{$template_id}</a>";
                    }
                    $member_data = "$member_name{$member_status}";
                    if ($_SESSION['user_group_leader'] == 1 and $member_name != UI_ITEM_LABEL_NULL) {
                        $member_data = "<a href='/Interfaces/Views/Pages/user/group/setting/member/user_group_member_update.php?member_id={$member_id}'>$member_name{$member_status}</a>";
                    }
                    $category_data = "$category_name{$category_status}";
                    if ($_SESSION['user_group_leader'] == 1 and $category_name != UI_ITEM_LABEL_NULL) {
                        $category_data = "<a href='/Interfaces/Views/Pages/user/group/setting/category/user_group_category_update.php?category_id={$category_id}'>$category_name{$category_status}</a>";
                    }

                    echo "<tr>";
                    echo "<td onclick='event.stopPropagation(); toggleCheckbox(this)'>
        <input type='checkbox'
               name='selected_datas[]'
               value='{$template_no}\t{$template_id}\t{$template_name}\t{$member_id}\t{$category_id}\t{$type}\t{$payment}\t{$settlement}\t{$amount}\t{$remarks}\t{$use_value}\t{$deleted_value}
'
               style='pointer-events: none;'></td>";

                    echo "<td>$template_no</td>";
                    echo "<td>{$id_data}</td>";
                    echo "<td>$template_name</td>";
                    echo "<td>$member_data</td>";
                    echo "<td>$category_data</td>";
                    echo "<td>$choices_type_item_name</td>";
                    echo "<td>$choices_payment_item_name</td>";
                    echo "<td>$choices_settlement_item_name</td>";
                    echo "<td>$amount</td>";
                    echo "<td>$remarks</td>";
                    echo "<td><input type='text' class='form-control' name='replace-item[]'></td>";
                    echo "</tr>";
                }
                ?>
                </tbody>
            </table>
        </div>
    </div>
</form>

<footer>
    <?php require $_SERVER['DOCUMENT_ROOT'] . '/Interfaces/Views/Layouts/footer.php'; ?>
</footer>

<script>
    function toggleAllFromTh(th) {
        const checkbox = th.querySelector('#select-all');
        if (checkbox) {
            checkbox.checked = !checkbox.checked;
            toggleAll(checkbox);  // 全体に反映
        }
    }

    function toggleAll(source) {
        const checkboxes = document.querySelectorAll('input[name="selected_datas[]"]');
        checkboxes.forEach(cb => {
            cb.checked = source.checked;
        });
    }
</script>
<script>
    function toggleCheckbox(td) {
        const checkbox = td.querySelector('input[type="checkbox"]');
        if (checkbox) {
            checkbox.checked = !checkbox.checked;
        }
    }
</script>

<?php
require $_SERVER['DOCUMENT_ROOT'] . '/Interfaces/Assets/JS/basic_js.php';
?>

</body>
</html>



