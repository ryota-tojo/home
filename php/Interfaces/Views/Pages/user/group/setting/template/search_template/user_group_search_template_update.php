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

$template_id = $_GET['template_id'];
if (!isExistSearchTemplate($_SESSION['user_groups_id'], $template_id)) {
    echo "<script>window.location.href = '/Interfaces/Views/Pages/access_error.php';</script>";
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

if (isset($_POST['entry'])) {
    $entry_button_click_flg = True;

    $post_template_id = $_POST['template_id'];
    $post_template_no = $_POST['template_no'];
    $post_template_name = $_POST['template_name'];
    $post_member_id = $_POST['member'] !== "" ? $_POST['member'] : null;
    $post_category_id = $_POST['category'] !== "" ? $_POST['category'] : null;
    $post_type_no = $_POST['type'] !== "" ? $_POST['type'] : null;
    $post_payment_no = $_POST['payment'] !== "" ? $_POST['payment'] : null;
    $post_settlement_no = $_POST['settlement'] !== "" ? $_POST['settlement'] : null;
    $post_min_amount = $_POST['min_amount'] !== "" ? $_POST['min_amount'] : null;
    $post_max_amount = $_POST['max_amount'] !== "" ? $_POST['max_amount'] : null;
    $post_remarks = $_POST['remarks'] !== "" ? $_POST['remarks'] : null;
    $post_use = $_POST['use'] ?? '';
    $post_status = $_POST['status'] ?? '';

    $result = searchTemplatePostActionForUpdateEvent(
        $_SESSION['user_groups_id'],
        $post_template_id,
        $post_template_no,
        $post_template_name,
        $post_member_id,
        $post_category_id,
        $post_type_no,
        $post_payment_no,
        $post_settlement_no,
        $post_min_amount,
        $post_max_amount,
        $post_remarks,
        $post_use,
        $post_status
    );

    $data = json_decode($result, True);
    $message = $data['message'];

    if ($data['status'] != 'error') {
        createLogs(LOG_TYPE_INFO, UI_ITEM_SEARCH_TEMPLATE . "更新 - " . UI_ITEM_SEARCH_TEMPLATE . "更新");
    } else {
        $entry_error = true;
        createLogs(LOG_TYPE_ERROR, UI_ITEM_SEARCH_TEMPLATE . "更新 - " . UI_ITEM_SEARCH_TEMPLATE . "更新失敗");
    }
}

$shopping_item_list = getShoppingItemList("user_group");
$member_list = $shopping_item_list['member_list'];
$category_list = $shopping_item_list['category_list'];
$type_list = $shopping_item_list['type_list'];
$payment_list = $shopping_item_list['payment_list'];
$settlement_list = $shopping_item_list['settlement_list'];

$template_data = getSearchTemplate($_SESSION['user_groups_id'], $template_id);

$template_no = $template_data['template_no'];
$template_name = $template_data['template_name'];
$template_member_id = $template_data['member_id'];
$template_category_id = $template_data['category_id'];
$template_type = $template_data['type'];
$template_payment = $template_data['payment'];
$template_settlement = $template_data['settlement'];
$template_min_amount = $template_data['min_amount'];
$template_max_amount = $template_data['max_amount'];
$template_remarks = $template_data['remarks'];
$template_use = $template_data['use'];
$template_deleted = $template_data['deleted'];

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
                                     href='/Interfaces/Views/Pages/user/group/setting/template/search_template/user_group_search_template_list.php'><?php echo UI_ITEM_SEARCH_TEMPLATE; ?>
                    一覧</a>
            </div>
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

                        <h6 class="form-title">入力フォーム</h6>
                        <hr>

                        <!-- テンプレートID -->
                        <div class="form-item">
                            <div class="form-item-label">
                                <label class="item-label"><?php echo UI_ITEM_SEARCH_TEMPLATE; ?>ID</label>
                            </div>
                            <div class="input-group form-item" style="display: none">
                                <input required type="text" class="form-control" name="template_id"
                                    <?php echo "value='{$template_id}'"; ?>
                                >
                            </div>
                            <div class="input-group form-item">
                                <input disabled type="text" class="form-control" name=""
                                    <?php echo "value='{$template_id}'"; ?>
                                >
                            </div>
                        </div>

                        <!-- テンプレートNo -->
                        <div class="form-item">
                            <div class="form-item-label">
                                <label class="item-label"><?php echo UI_ITEM_SEARCH_TEMPLATE_NO; ?></label>
                            </div>
                            <div class="input-group form-item" style="display: none">
                                <input required type="text" class="form-control" name="template_no"
                                    <?php echo "value='{$template_no}'"; ?>
                                >
                            </div>
                            <div class="input-group form-item">
                                <input disabled type="text" class="form-control" name=""
                                    <?php echo "value='{$template_no}'"; ?>
                                >
                            </div>
                        </div>

                        <!-- テンプレート名 -->
                        <div class="form-item">
                            <div class="form-item-label">
                                <label class="item-label"><?php echo UI_ITEM_SEARCH_TEMPLATE_NAME; ?></label>
                            </div>
                            <div class="input-group form-item">
                                <input required type="text" class="form-control" name="template_name"
                                    <?php echo "value='{$template_name}'"; ?>
                                >
                            </div>
                        </div>

                        <hr>

                        <!-- 購入者 -->
                        <div class="form-item member-item">
                            <div class="form-item-label">
                                <label class="item-label"><?php echo UI_ITEM_SEARCH_TEMPLATE_MEMBER; ?></label>
                            </div>
                            <div class="input-group form-item">
                                <select class="form-select" name="member">
                                    <?php
                                    echo "<option selected value=''>" . UI_ITEM_LABEL_ALL . "</option>";
                                    foreach ($member_list as $member_id => $member_name) {
                                        $member_selected = "";
                                        if ($member_id == $template_member_id) {
                                            $member_selected = " selected ";
                                        }
                                        echo "<option {$member_selected} value='{$member_id}'>{$member_name}</option>";
                                    }
                                    ?>
                                </select>
                            </div>
                        </div>

                        <div class="pc-form">
                            <!-- カテゴリー -->
                            <div class="form-item category-item">
                                <div class="form-item-label">
                                    <label class="item-label"><?php echo UI_ITEM_SEARCH_TEMPLATE_CATEGORY; ?></label>
                                </div>
                                <div class="input-group form-item">
                                    <select class="form-select" name="category">
                                        <?php
                                        echo "<option selected value=''>" . UI_ITEM_LABEL_ALL . "</option>";
                                        foreach ($category_list as $category_id => $category_name) {
                                            $category_selected = "";
                                            if ($category_id == $template_category_id) {
                                                $category_selected = " selected ";
                                            }
                                            echo "<option {$category_selected} value='{$category_id}'>{$category_name}</option>";
                                        }
                                        ?>
                                    </select>
                                </div>
                            </div>

                            <!-- 種別 -->
                            <div class="form-item type-item">
                                <div class="form-item-label">
                                    <label class="item-label"><?php echo UI_ITEM_SEARCH_TEMPLATE_TYPE; ?></label>
                                </div>
                                <div class="input-group form-item">
                                    <select class="form-select" name="type">
                                        <?php
                                        echo "<option selected value=''>" . UI_ITEM_LABEL_ALL . "</option>";
                                        foreach ($type_list as $type_no => $type_name) {
                                            $type_selected = "";
                                            if ($type_no == $template_type) {
                                                $type_selected = " selected ";
                                            }
                                            echo "<option {$type_selected} value='{$type_no}'>{$type_name}</option>";
                                        }
                                        ?>
                                    </select>
                                </div>
                            </div>

                            <!-- 支払 -->
                            <div class="form-item payment-item">
                                <div class="form-item-label">
                                    <label class="item-label"><?php echo UI_ITEM_SEARCH_TEMPLATE_PAYMENT; ?></label>
                                </div>
                                <div class="input-group form-item">
                                    <select class="form-select" name="payment">
                                        <?php
                                        echo "<option selected value=''>" . UI_ITEM_LABEL_ALL . "</option>";
                                        foreach ($payment_list as $payment_no => $payment_name) {
                                            $payment_selected = "";
                                            if ($payment_no == $template_payment) {
                                                $payment_selected = " selected ";
                                            }
                                            echo "<option {$payment_selected} value='{$payment_no}'>{$payment_name}</option>";
                                        }
                                        ?>
                                    </select>
                                </div>
                            </div>

                            <!-- 精算 -->
                            <div class="form-item settlement-item">
                                <div class="form-item-label">
                                    <label class="item-label"><?php echo UI_ITEM_SEARCH_TEMPLATE_SETTLEMENT; ?></label>
                                </div>
                                <div class="input-group form-item">
                                    <select class="form-select" name="settlement">
                                        <?php
                                        echo "<option selected value=''>" . UI_ITEM_LABEL_ALL . "</option>";
                                        foreach ($settlement_list as $settlement_no => $settlement_name) {
                                            $settlement_selected = "";
                                            if ($settlement_no == $template_settlement) {
                                                $settlement_selected = " selected ";
                                            }
                                            echo "<option {$settlement_selected} value='{$settlement_no}'>{$settlement_name}</option>";
                                        }
                                        ?>
                                    </select>
                                </div>
                            </div>
                        </div>

                        <div class="pc-form">
                            <!-- 最小金額 -->
                            <div class="form-item min-amount-item">
                                <div class="form-item-label">
                                    <label class="item-label"><?php echo UI_ITEM_SEARCH_TEMPLATE_MIN_AMOUNT; ?></label>
                                </div>
                                <div class="input-group form-item">
                                    <input type="number" min="1" class="form-control" name="min_amount"
                                        <?php echo "value='{$template_min_amount}'"; ?>
                                    >
                                </div>
                            </div>

                            <!-- 最大金額 -->
                            <div class="form-item max-amount-item">
                                <div class="form-item-label">
                                    <label class="item-label"><?php echo UI_ITEM_SEARCH_TEMPLATE_MAX_AMOUNT; ?></label>
                                </div>
                                <div class="input-group form-item">
                                    <input type="number" min="1" class="form-control" name="max_amount"
                                        <?php echo "value='{$template_max_amount}'"; ?>
                                    >
                                </div>
                            </div>
                        </div>

                        <!-- 備考 -->
                        <div class="form-item remarks-item">
                            <div class="form-item-label">
                                <label class="item-label"><?php echo UI_ITEM_SEARCH_TEMPLATE_REMARKS; ?></label>
                            </div>
                            <div class="input-group form-item">
                                <input type="text" class="form-control" name="remarks"
                                    <?php echo "value='{$template_remarks}'"; ?>
                                >
                            </div>
                        </div>

                        <div class="form-item">
                            <div class="form-item-label">
                                <label class="item-label"><?php echo UI_ITEM_SEARCH_TEMPLATE_USE; ?></label>
                            </div>
                            <div class="form-check">
                                <input class="form-check-input" type="radio" name="use"
                                       id="useButton1" value="1"
                                    <?php
                                    if ($template_use == "1") {
                                        echo "checked";
                                    }
                                    ?>
                                >
                                <label class="form-check-label" for="useButton1">
                                    使用する
                                </label>
                            </div>
                            <div class="form-check">
                                <input class="form-check-input" type="radio" name="use"
                                       id="useButton2" value="0"
                                    <?php
                                    if ($template_use == "0") {
                                        echo "checked";
                                    }
                                    ?>
                                >
                                <label class="form-check-label" for="useButton2">
                                    使用しない
                                </label>
                            </div>
                        </div>

                        <div class="form-item">
                            <div class="form-item-label">
                                <label class="item-label"><?php echo UI_ITEM_SEARCH_TEMPLATE_STATUS; ?></label>
                            </div>
                            <div class="form-check">
                                <input class="form-check-input" type="radio" name="status"
                                       id="deleteButton1" value="0"
                                    <?php
                                    if ($template_deleted == "0") {
                                        echo "checked";
                                    }
                                    ?>
                                >
                                <label class="form-check-label" for="deleteButton1">
                                    未削除状態
                                </label>
                            </div>
                            <div class="form-check">
                                <input class="form-check-input" type="radio" name="status"
                                       id="deleteButton2" value="1"
                                    <?php
                                    if ($template_deleted == "1") {
                                        echo "checked";
                                    }
                                    ?>
                                >
                                <label class="form-check-label" for="deleteButton2">
                                    削除状態
                                </label>
                            </div>
                        </div>
                    </div>

                    <!-- 登録ボタン -->
                    <div class="btn-area">
                        <div class="btn-center-area">
                            <div class="btn-item">
                                <button type="submit" class="btn btn-primary" name="entry">登録</button>
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

</body>
</html>



