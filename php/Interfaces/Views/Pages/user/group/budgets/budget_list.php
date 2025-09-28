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

    // 年間予算一覧取得
    $budgets_data = getBudgetsCategoryIdListForTargetYear($_SESSION['user_groups_id'], $_SESSION['target_yyyy']);
    ?>

    <div class="contents">
        <div class="content-row">

            <div class="table-main">
                <div class="table-area">
                    <table class="table table-light table-striped table-bordered table-hover">
                        <thead class="table-dark">
                        <tr>
                            <th><?php echo "カテゴリー名"; ?></th>
                            <th><?php echo "状態"; ?></th>
                            <?php
                            foreach (range(1, 12) as $mm) {
                                echo "<th>{$mm}月</th>";
                            }
                            ?>
                            <th style="border-left: double;"><?php echo "平均"; ?></th>
                            <th><?php echo "合計"; ?></th>
                        </tr>
                        </thead>
                        <tbody id="sortable-table">

                        <?php
                        $month01_total_value = 0;
                        foreach ($budgets_data['header'] as $header) {
                            $status = "有効";
                            $th_class = "table-success";
                            if ($header['status'] == "de_active") {
                                $status = "無効";
                                $th_class = "table-secondary";
                            }
                            if ($header['status'] == "deleted") {
                                $status = "削除済";
                                $th_class = "table-warning";
                            }
                            echo "<tr class='{$th_class}'>";
                            echo "<td>{$header['category_name']}</td>";
                            echo "<td>{$status}</td>";

                            $filtered_data = array_filter($budgets_data['body'], function ($item) use ($header) {
                                return $item['category_id'] == $header['category_id'];
                            });
                            $filtered_data = array_values($filtered_data);

                            $monthly = $filtered_data[0]['monthly'];
                            foreach (range(1, 12) as $mm) {
                                $key = sprintf('month%02d_amount', $mm);
                                $amount_value = isset($monthly[$key]) ? $monthly[$key] : 0;
                                $amount = number_format($amount_value);
                                echo "<td class='td-num'>{$amount}</td>";

                                if ($mm == 1) {
                                    $month01_total_value += $amount_value;
                                }
                            }

                            $average_value = isset($filtered_data[0]['average_amount']) ? $filtered_data[0]['average_amount'] : 0;
                            $average = number_format($average_value);
                            $total_value = isset($filtered_data[0]['total_amount']) ? $filtered_data[0]['total_amount'] : 0;
                            $total = number_format($total_value);
                            echo "<td class='td-num' style='border-left: double;'>{$average}</td>";
                            echo "<td class='td-num'>{$total}</td>";

                            echo "</tr>";
                        }

                        echo "<tr style='border-top: double;'>";
                        echo "    <td>合計</td>";
                        echo "    <td>-</td>";

                        foreach ($budgets_data['footer'][0]['monthly'] as $month => $amount) {
                            echo "<td class='td-num'>" . number_format($amount) . "</td>";
                        }
                        echo "<td class='td-num' style='border-left: double;'>" . number_format($budgets_data['footer'][0]['average_amount'], 2) . "</td>";
                        echo "<td class='td-num'>" . number_format($budgets_data['footer'][0]['total_amount']) . "</td>";

                        echo "</tr>";

                        ?>
                        </tbody>
                    </table>
                </div>
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



