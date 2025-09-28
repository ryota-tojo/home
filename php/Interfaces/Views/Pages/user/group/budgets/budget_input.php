<?php
if (session_status() === PHP_SESSION_NONE) {
    session_start();
}
require_once $_SERVER['DOCUMENT_ROOT'] . '/Config/config.php';
require_once $_SERVER['DOCUMENT_ROOT'] . '/Config/log_config.php';
require_once $_SERVER['DOCUMENT_ROOT'] . '/Application/Services/ApiService.php';
require_once $_SERVER['DOCUMENT_ROOT'] . '/Interfaces/Views/Partials/requireApi.php';
require_once $_SERVER['DOCUMENT_ROOT'] . '/Interfaces/Views/Partials/group/import_group.php';
require_once $_SERVER['DOCUMENT_ROOT'] . '/Interfaces/Views/Partials/budgets/import_budgets.php';
require_once $_SERVER['DOCUMENT_ROOT'] . '/Interfaces/Views/Partials/fixed/import_fixed.php';

require_once $_SERVER['DOCUMENT_ROOT'] . '/Interfaces/Views/Partials/systems/logs/create_logs.php';
require_once $_SERVER['DOCUMENT_ROOT'] . '/Interfaces/Views/Partials/systems/screen/get_screen.php';

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

    $entry_mode = $_POST['entry-mode'];
    $category_id_list = $_POST['category_id_list'];
    $amount_list = $_POST['amount_list'];
    $budgets_list = [];
    foreach ($category_id_list as $index => $category_id) {
        $budgets_list[] = "{$category_id_list[$index]}\t{$amount_list[$index]}";
    }

    // 通常登録
    if ($entry_mode == 0) {
        $result = budgetsPostActionForNormalEntryEvent($_SESSION['user_groups_id'], $_SESSION['target_yyyy'], $_SESSION['target_mm'], $budgets_list);
        $data = json_decode($result, true);
    }
    // 前月コピー登録
    if ($entry_mode == 1) {
        $result = budgetsPostActionForCopyEntryEvent($_SESSION['user_groups_id'], $_SESSION['target_yyyy'], $_SESSION['target_mm']);
        $data = json_decode($result, true);
    }
    // 一括登録
    if ($entry_mode == 2) {
        $result = budgetsPostActionForAllEntryEvent($_SESSION['user_groups_id'], $_SESSION['target_yyyy'], $budgets_list);
        $data = json_decode($result, true);
    }

    if ($data['status'] == 'error') {
        $entry_error = true;
    }
    $message = $data['message'];
}

if (isset($_POST['delete'])) {
    $entry_button_click_flg = True;
    $entry_mode = $_POST['entry-mode'];

    if ($entry_mode == 0 OR $entry_mode == 1) {
        $result = budgetsPostActionForDeleteEvent($_SESSION['user_groups_id'], $_SESSION['target_yyyy'], $_SESSION['target_mm']);
        $data = json_decode($result, true);
    }else{
        $result = budgetsPostActionForAllDeleteEvent($_SESSION['user_groups_id'], $_SESSION['target_yyyy']);
        $data = json_decode($result, true);
    }

    if ($data['status'] == 'error') {
        $entry_error = true;
    }
    $message = $data['message'];

}

// 確定判定
$fixed_label = "";
$fixed = getFixed($_SESSION['user_groups_id'], $_SESSION['target_yyyy'], $_SESSION['target_mm']);
if ($fixed == True) {
    $fixed_label = " disabled ";
}
// 予算データ取得
$budgets_list = getBudgetsForCategoryAndAmount($_SESSION['user_groups_id'], $_SESSION['target_yyyy'], $_SESSION['target_mm']);

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

                <form action="" method="post">
                    <div class="form-area">
                        <div class="input-form">
                            <div class="form-item-label">
                                <label class="item-label"><?php echo UI_ITEM_BUDGETS; ?>入力</label>
                            </div>

                            <div class="table-main">
                                <div class="table-area">
                                    <table class="table table-striped table-bordered table-hover">
                                        <thead class="table-dark">
                                        <?php $debug = 0; ?>
                                        <tr>
                                            <th><?php echo UI_ITEM_BUDGETS_NO; ?></th>
                                            <?php if($debug == 1){ echo "<th>予算カテゴリー種別</th>"; } ?>
                                            <th><?php echo UI_ITEM_BUDGETS_CATEGORY; ?></th>
                                            <th><?php echo UI_ITEM_BUDGETS_AMOUNT; ?></th>
                                        </tr>
                                        </thead>
                                        <tbody id="sortable-table">

                                        <?php
                                        foreach ($budgets_list as $index => $budgets) {
                                            echo "<tr>";
                                            echo "<input style='display: none' type='text' name='category_id_list[]' value='{$budgets['category_id']}'>";
                                            $budgets_no = $index + 1;
                                            echo "    <td>{$budgets_no}</td>";
                                            if ($debug == 1) {
                                                echo "    <td>{$budgets['budgets_category_type']}</td>";
                                            }
                                            echo "    <td>{$budgets['category_name']}</td>";
                                            echo "    <td>";
                                            echo "        <div style='display: flex'>";
                                            echo "            <input {$fixed_label} type='number' class='form-control' name='amount_list[]' value='{$budgets['amount']}'>";
                                            echo "            <div class='table-input-box-unit'><div>円</div></div>";
                                            echo "        <div>";
                                            echo "    </td>";
                                            echo "</tr>";
                                        }

                                        ?>
                                        </tbody>
                                    </table>
                                </div>
                            </div>

                            <hr>

                            <div class="form-item">
                                <div class="form-item-label" style="margin-bottom: 10px">
                                    <label class="item-label"><?php echo "登録方法選択"; ?></label>
                                </div>

                                <div class="form-check">
                                    <input <?php echo "{$fixed_label}"; ?> class="form-check-input" type="radio"
                                                                           name="entry-mode"
                                                                           id="normal-mode" value="0" checked
                                    >
                                    <label class="form-check-label" for="normal-mode">
                                        通常登録：　選択中の月に予算を登録する
                                    </label>
                                </div>
                                <div class="form-check">
                                    <input <?php echo "{$fixed_label}"; ?> class="form-check-input" type="radio"
                                                                           name="entry-mode"
                                                                           id="copy-mode" value="1"
                                    >
                                    <label class="form-check-label" for="copy-mode">
                                        前月コピー登録：　選択中の月に前月の予算をコピーして登録する
                                    </label>
                                </div>
                                <div class="form-check">
                                    <input <?php echo "{$fixed_label}"; ?> class="form-check-input" type="radio"
                                                                           name="entry-mode"
                                                                           id="all-mode" value="2"
                                    >
                                    <label class="form-check-label" for="all-mode">
                                        一括登録：　すべての月に設定を適用する
                                    </label>
                                </div>
                            </div>
                            <div hidden class="hint_msg" id='copy-mode-remarks'>
                                <div style="margin-top: 20px;font-weight: bold">※前月コピー登録の場合※</div>
                                選択中の月に既に予算が登録されていて、前月と差分のある予算が存在する場合、そのデータはそのまま残ります。
                            </div>
                            <div hidden class="hint_msg" id='all-mode-remarks'>
                                <div style="margin-top: 20px;font-weight: bold">※一括登録の場合※</div>
                                登録先の月に既に予算が登録されていて、選択中の月と差分のある予算が存在する場合、そのデータはそのまま残ります。
                            </div>
                        </div>
                    </div>

                    <!-- 登録ボタン -->
                    <div style="width:100%; display: flex; justify-content: center;">
                        <div class="btn-area">
                            <div class="btn-center-area">
                                <div class="btn-item">
                                    <button <?php echo "{$fixed_label}"; ?> type="submit" class="btn btn-primary"
                                                                            name="entry">登録
                                    </button>
                                </div>
                                <div class="btn-item">
                                    <button <?php echo "{$fixed_label}"; ?> type="submit" class="btn btn-danger"
                                                                            name="delete" onclick="return confirm('本当に削除しますか？\n※登録方法選択で ”一括登録” を選択中の場合、年間の予算を削除します。')">削除
                                    </button>
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

<footer>
    <?php require $_SERVER['DOCUMENT_ROOT'] . '/Interfaces/Views/Layouts/footer.php'; ?>
</footer>

<?php
require $_SERVER['DOCUMENT_ROOT'] . '/Interfaces/Assets/JS/basic_js.php';
?>

<script>
    document.getElementById("normal-mode").addEventListener("change", function () {
        if (this.checked) {
            const copy_mode_remarks = document.getElementById("copy-mode-remarks")
            const all_mode_remarks = document.getElementById("all-mode-remarks")
            copy_mode_remarks.hidden = true
            all_mode_remarks.hidden = true
        }
    });
    document.getElementById("copy-mode").addEventListener("change", function () {
        if (this.checked) {
            const copy_mode_remarks = document.getElementById("copy-mode-remarks")
            const all_mode_remarks = document.getElementById("all-mode-remarks")
            copy_mode_remarks.hidden = false
            all_mode_remarks.hidden = true
        }
    });
    document.getElementById("all-mode").addEventListener("change", function () {
        if (this.checked) {
            const copy_mode_remarks = document.getElementById("copy-mode-remarks")
            const all_mode_remarks = document.getElementById("all-mode-remarks")
            copy_mode_remarks.hidden = true
            all_mode_remarks.hidden = false
        }
    });
</script>
</body>
</html>



