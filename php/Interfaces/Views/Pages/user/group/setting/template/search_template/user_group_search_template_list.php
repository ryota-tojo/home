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

require_once $_SERVER['DOCUMENT_ROOT'] . '/Interfaces/Views/Partials/member/get_member.php';
require_once $_SERVER['DOCUMENT_ROOT'] . '/Interfaces/Views/Partials/category/get_category.php';
require_once $_SERVER['DOCUMENT_ROOT'] . '/Interfaces/Views/Partials/shopping/get_shopping_data.php';
require_once $_SERVER['DOCUMENT_ROOT'] . '/Interfaces/Views/Partials/template/import_search_template.php';

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
    <link rel="stylesheet" href="/Interfaces/Assets/CSS/search_form.css">
    <link rel="stylesheet" href="/Interfaces/Assets/CSS/message.css">
    <link rel="stylesheet" href="/Interfaces/Assets/CSS/table_form.css">
    <link rel="stylesheet" href="/Interfaces/Assets/CSS/button_form.css">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha3/dist/css/bootstrap.min.css" rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha3/dist/js/bootstrap.bundle.min.js"></script>

    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script src="https://code.jquery.com/ui/1.13.2/jquery-ui.min.js"></script>
    <link rel="stylesheet" href="https://code.jquery.com/ui/1.13.2/themes/base/jquery-ui.css">
    <style>
        .sortable-placeholder {
            background-color: #f0f0f0;
            border: 2px dashed #aaa;
            height: 40px;
        }
    </style>
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
$search_button_click_flg = False;
$message = "";
$search_error = False;

// マスター設定
// なし

// ボタン押下時の処理
if (isset($_POST['change_btn'])) {
    $search_button_click_flg = True;

    if (isset($_POST['selected_datas'])) {

        $result_data = searchTemplatesPostActionForSortEvent($_SESSION['user_groups_id'], $_POST['selected_datas']);
        $result = json_decode($result_data, True);
        if ($result['status'] == 'error') {
            $search_error = true;
        }
        $message = $result['message'];

    } else {
        $message = UI_ITEM_SEARCH_TEMPLATE . "が選択されていません";
        $search_error = true;
        createLogs(LOG_TYPE_ERROR, UI_ITEM_SEARCH_TEMPLATE . "入替 - " . UI_ITEM_SEARCH_TEMPLATE . "未選択");
    }
}

if (isset($_POST['use_btn'])) {
    $search_button_click_flg = True;

    if (isset($_POST['selected_datas'])) {
        $result_data = searchTemplatesPostActionForUsageEvent($_SESSION['user_groups_id'], $_POST['selected_datas']);
        $result = json_decode($result_data, True);
        if ($result['status'] == 'error') {
            $search_error = true;
        }
        $message = $result['message'];
    } else {
        $message = UI_ITEM_SEARCH_TEMPLATE . "が選択されていません";
        $search_error = true;
        createLogs(LOG_TYPE_ERROR, UI_ITEM_SEARCH_TEMPLATE . "使用化 - " . UI_ITEM_SEARCH_TEMPLATE . "未選択");
    }
}

if (isset($_POST['un_use_btn'])) {
    $search_button_click_flg = True;

    if (isset($_POST['selected_datas'])) {
        $result_data = searchTemplatesPostActionForUnusageEvent($_SESSION['user_groups_id'], $_POST['selected_datas']);
        $result = json_decode($result_data, True);
        if ($result['status'] == 'error') {
            $search_error = true;
        }
        $message = $result['message'];
    } else {
        $message = UI_ITEM_SEARCH_TEMPLATE . "が選択されていません";
        $search_error = true;
        createLogs(LOG_TYPE_ERROR, UI_ITEM_SEARCH_TEMPLATE . "不使用化 - " . UI_ITEM_SEARCH_TEMPLATE . "未選択");
    }
}

if (isset($_POST['on_btn'])) {
    $search_button_click_flg = True;

    if (isset($_POST['selected_datas'])) {
        $result_data = searchTemplatesPostActionForActivateEvent($_SESSION['user_groups_id'], $_POST['selected_datas']);
        $result = json_decode($result_data, True);
        if ($result['status'] == 'error') {
            $search_error = true;
        }
        $message = $result['message'];
    } else {
        $message = UI_ITEM_SEARCH_TEMPLATE . "が選択されていません";
        $search_error = true;
        createLogs(LOG_TYPE_ERROR, UI_ITEM_SEARCH_TEMPLATE . "有効化 - " . UI_ITEM_SEARCH_TEMPLATE . "未選択");
    }
}

if (isset($_POST['off_btn'])) {
    $search_button_click_flg = True;

    if (isset($_POST['selected_datas'])) {
        $result_data = searchTemplatesPostActionFordeactivateEvent($_SESSION['user_groups_id'], $_POST['selected_datas']);
        $result = json_decode($result_data, True);
        if ($result['status'] == 'error') {
            $search_error = true;
        }
        $message = $result['message'];
    } else {
        $message = UI_ITEM_SEARCH_TEMPLATE . "が選択されていません";
        $search_error = true;
        createLogs(LOG_TYPE_ERROR, UI_ITEM_SEARCH_TEMPLATE . "無効化 - " . UI_ITEM_SEARCH_TEMPLATE . "未選択");
    }
}

if (isset($_POST['delete_btn'])) {
    $search_button_click_flg = True;

    if (isset($_POST['selected_datas'])) {
        $result_data = searchTemplatesPostActionForDeleteEvent($_SESSION['user_groups_id'], $_POST['selected_datas']);
        $result = json_decode($result_data, True);
        if ($result['status'] == 'error') {
            $search_error = true;
        }
        $message = $result['message'];
    } else {
        $message = UI_ITEM_SEARCH_TEMPLATE . "が選択されていません";
        $search_error = true;
        createLogs(LOG_TYPE_ERROR, UI_ITEM_SEARCH_TEMPLATE . "削除 - " . UI_ITEM_SEARCH_TEMPLATE . "未選択");
    }
}

// 無効化処理
$auto_deactivate_result_data = autoDeactivateForSearchTemplate($_SESSION['user_groups_id']);
$auto_deactivate_result = json_decode($auto_deactivate_result_data, True);
if ($auto_deactivate_result['status'] == "success") {
    $search_button_click_flg = True;
    $search_error = True;
    $message = $auto_deactivate_result['message'];
}
// 有効化処理
$auto_activate_result_data = autoActivateForSearchTemplate($_SESSION['user_groups_id']);
$auto_activate_result = json_decode($auto_activate_result_data, True);
if ($auto_activate_result['status'] == "success") {
    if ($message == "") {
        $search_button_click_flg = True;
        $message = $auto_activate_result['message'];
    }
}

$shopping_item_list = getShoppingItemList($_SESSION['user_groups_id']);
$member_list = $shopping_item_list['member_list'];
$category_list = $shopping_item_list['category_list'];
$type_list = $shopping_item_list['type_list'];
$payment_list = $shopping_item_list['payment_list'];
$settlement_list = $shopping_item_list['settlement_list'];
$template_data = getSearchTemplateList($_SESSION['user_groups_id']);

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

    <div class="btn-area">
        <div class="btn-center-area">
            <div class='btn-item'><a class='link-btn'
                                     href='/Interfaces/Views/Pages/user/group/setting/template/search_template/user_group_search_template_entry.php'><?php echo UI_ITEM_SEARCH_TEMPLATE; ?>
                    登録</a>
            </div>
        </div>
    </div>

    <?php
    if ($search_button_click_flg == True) {
        if ($search_error == True) {
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
                <div class="settings">


                </div>
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
                <button type="submit" class="btn btn-primary" name="change_btn"
                        onclick="return checkAllAndConfirm();">
                    入替
                </button>
            </div>
            <div class="btn-item">
                <button type="submit" class="btn btn-primary" name="use_btn"
                        onclick="return confirm('本当に実行しますか？\n以下の<?php echo UI_ITEM_SEARCH_TEMPLATE; ?>はスキップされます。\n・ 既に使用状態の<?php echo UI_ITEM_SEARCH_TEMPLATE; ?>\n・ 無効状態の<?php echo UI_ITEM_SEARCH_TEMPLATE; ?>')">
                    使用する
                </button>
            </div>
            <div class="btn-item">
                <button type="submit" class="btn btn-warning" name="un_use_btn"
                        onclick="return confirm('本当に実行しますか？\n以下の<?php echo UI_ITEM_SEARCH_TEMPLATE; ?>はスキップされます。\n・ 既に未使用状態の<?php echo UI_ITEM_SEARCH_TEMPLATE; ?>\n・ 無効状態の<?php echo UI_ITEM_SEARCH_TEMPLATE; ?>')">
                    使用しない
                </button>
            </div>
            <div class="btn-item">
                <button type="submit" class="btn btn-primary" name="on_btn"
                        onclick="return confirm('本当に実行しますか？\n以下の<?php echo UI_ITEM_SEARCH_TEMPLATE; ?>はスキップされます。\n・ 既に有効化の<?php echo UI_ITEM_SEARCH_TEMPLATE; ?>')">
                    有効化
                </button>
            </div>
            <div class="btn-item">
                <button type="submit" class="btn btn-warning" name="off_btn"
                        onclick="return confirm('本当に実行しますか？\n以下の<?php echo UI_ITEM_SEARCH_TEMPLATE; ?>はスキップされます。\n・ 既に無効化の<?php echo UI_ITEM_SEARCH_TEMPLATE; ?>')">
                    無効化
                </button>
            </div>
            <div class="btn-item">
                <button type="submit" class="btn btn-danger" name="delete_btn"
                        onclick="return confirm('本当に実行しますか？\n以下の<?php echo UI_ITEM_SEARCH_TEMPLATE; ?>はスキップされます。\n・ 有効化の<?php echo UI_ITEM_SEARCH_TEMPLATE; ?>')">
                    削除
                </button>
            </div>
        </div>
    </div>

    <div class="table-main">
        ※行をドラッグすると順番を入れ替えられます
    </div>
    <div class="table-main">
        <div class="table-area">
            <table class="table table-light table-striped table-bordered table-hover">
                <thead class="table-dark">
                <tr>
                    <th><input type="checkbox" id="select-all" onclick="toggleAll(this)"></th>
                    <th><?php echo UI_ITEM_SEARCH_TEMPLATE_NO; ?></th>
                    <th><?php echo UI_ITEM_SEARCH_TEMPLATE_ID; ?></th>
                    <th><?php echo UI_ITEM_SEARCH_TEMPLATE_NAME; ?></th>
                    <th><?php echo UI_ITEM_SEARCH_TEMPLATE_MEMBER; ?></th>
                    <th><?php echo UI_ITEM_SEARCH_TEMPLATE_CATEGORY; ?></th>
                    <th><?php echo UI_ITEM_SEARCH_TEMPLATE_TYPE; ?></th>
                    <th><?php echo UI_ITEM_SEARCH_TEMPLATE_PAYMENT; ?></th>
                    <th><?php echo UI_ITEM_SEARCH_TEMPLATE_SETTLEMENT; ?></th>
                    <th><?php echo UI_ITEM_SEARCH_TEMPLATE_MIN_AMOUNT; ?></th>
                    <th><?php echo UI_ITEM_SEARCH_TEMPLATE_MAX_AMOUNT; ?></th>
                    <th><?php echo UI_ITEM_SEARCH_TEMPLATE_REMARKS; ?></th>
                    <th><?php echo UI_ITEM_SEARCH_TEMPLATE_USE; ?></th>
                    <th><?php echo UI_ITEM_SEARCH_TEMPLATE_STATUS; ?></th>
                </tr>
                </thead>
                <tbody id="sortable-table">
                <?php
                if (!isset($template_data)) {
                    $template_data = [];
                }
                foreach ($template_data as $template) {
                    $template_no_value = $template['template_no'];
                    $template_id = $template['template_id'];
                    $template_name = $template['template_name'];
                    $member_id = $template['member_id'];
                    $category_id = $template['category_id'];
                    $type = $template['type'];
                    $payment = $template['payment'];
                    $settlement = $template['settlement'];
                    $min_amount = $template['min_amount'];
                    $max_amount = $template['max_amount'];
                    $remarks = $template['remarks'];
                    $use_value = $template['use'];
                    $deleted_value = $template['deleted'];

                    if ($deleted_value == 1) {
                        $template_no = "-";
                    } else {
                        $template_no = $template_no_value;
                    }

                    $member_name = UI_ITEM_LABEL_ALL;
                    $member_status = '';
                    if ($member_id != null) {
                        $member_data = getMember($member_id, $_SESSION['user_groups_id']);
                        $member_name = $member_data['member_name'] ?? "";
                        if (isActiveMember($member_id, $_SESSION['user_groups_id']) == "isDeActive") {
                            $member_status = '(無効)';
                        }
                    }

                    $category_name = UI_ITEM_LABEL_ALL;
                    $category_status = '';
                    if ($category_id != null) {
                        $category_data = getCategory($category_id, $_SESSION['user_groups_id']);
                        $category_name = $category_data['category_name'] ?? "";
                        if (isActiveCategory($category_id, $_SESSION['user_groups_id']) == "isDeActive") {
                            $category_status = '(無効)';
                        }
                    }

                    $choices_type_item_name = UI_ITEM_LABEL_ALL;
                    $choices_payment_item_name = UI_ITEM_LABEL_ALL;
                    $choices_settlement_item_name = UI_ITEM_LABEL_ALL;
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

                    if ($use_value == 1) {
                        $use = "使用する";
                    } else {
                        $use = "-";
                    }

                    if ($deleted_value == 0) {
                        $deleted = "有効";
                    } else {
                        $deleted = "無効";
                    }

                    echo "<tr>";
                    echo "<td onclick='event.stopPropagation(); toggleCheckbox(this)'>
        <input type='checkbox'
               name='selected_datas[]'
               value='{$template_no_value}\t{$template_id}\t{$template_name}\t{$member_id}\t{$category_id}\t{$type}\t{$payment}\t{$settlement}\t{$min_amount}\t{$max_amount}\t{$remarks}\t{$use_value}\t{$deleted_value}
'
               style='pointer-events: none;'></td>";

                    echo "<td>$template_no</td>";
                    echo "
                <td><a href='/Interfaces/Views/Pages/user/group/setting/template/search_template/user_group_search_template_update.php?template_id={$template_id}'>$template_id</a></td>
                ";
                    echo "<td>$template_name</td>";
                    if ($member_name == UI_ITEM_LABEL_NULL or $member_name == UI_ITEM_LABEL_ALL) {
                        echo "<td>$member_name{$member_status}</td>";
                    } else {
                        echo "<td><a href='/Interfaces/Views/Pages/user/group/setting/member/user_group_member_update.php?member_id={$member_id}'>$member_name{$member_status}</a></td>";
                    }
                    if ($category_name == UI_ITEM_LABEL_NULL or $category_name == UI_ITEM_LABEL_ALL) {
                        echo "<td>$category_name{$category_status}</td>";
                    } else {
                        echo "<td><a href='/Interfaces/Views/Pages/user/group/setting/category/user_group_category_update.php?category_id={$category_id}'>$category_name{$category_status}</a></td>";
                    }

                    echo "<td>$choices_type_item_name</td>";
                    echo "<td>$choices_payment_item_name</td>";
                    echo "<td>$choices_settlement_item_name</td>";
                    echo "<td>$min_amount</td>";
                    echo "<td>$max_amount</td>";
                    echo "<td>$remarks</td>";
                    echo "<td>$use</td>";
                    echo "<td>$deleted</td>";
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
<script>
    $("#sortable-table").sortable({
        helper: function (e, tr) {
            const $originals = tr.children();
            const $helper = tr.clone();
            $helper.children().each(function (index) {
                $(this).width($originals.eq(index).width());
            });
            return $helper;
        },
        cursor: "move",
        placeholder: "sortable-placeholder",
        items: "tr",
        update: function (event, ui) {
            // 並び順変更後の確認
            console.log("New order:");
            $("#sortable-table tr").each(function (index) {
                const categoryId = $(this).find("input[type=checkbox]").val().split('\t')[0];
                console.log(index + 1 + ": " + categoryId);
            });
        }
    }).disableSelection();
</script>

<script>
    function checkAllAndConfirm() {
        const checkboxes = document.querySelectorAll('input[name="selected_datas[]"]');
        checkboxes.forEach(cb => cb.checked = true);
        const ok = confirm('順番を入れ替えますか？\n※入替を行うと番号が連番になるように整形されます');
        if (!ok) {
            return false;
        }
        return true;
    }
</script>

<?php
require $_SERVER['DOCUMENT_ROOT'] . '/Interfaces/Assets/JS/basic_js.php';
?>

</body>
</html>



