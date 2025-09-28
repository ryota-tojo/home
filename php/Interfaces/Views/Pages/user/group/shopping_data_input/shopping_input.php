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
require_once $_SERVER['DOCUMENT_ROOT'] . '/Interfaces/Views/Partials/template/import_input_template.php';
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

        $result_data = shoppingDataPostActionForEntryTemplateInputEvent($_SESSION['user_groups_id'], $_SESSION['user_id'], $_POST['target_date'], $selected_datas);
        $result = json_decode($result_data, True);
        if ($result['status'] == 'error') {
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
$template_data_list = getInputTemplateListForActive($_SESSION['user_groups_id']);
$shopping_data_list = getShoppingDataHistoryList($_SESSION['user_groups_id']);

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

                        <h6 class="form-title">入力フォーム</h6>
                        <hr>

                        <!-- 入力パターン -->
                        <div class="form-item">
                            <div class="form-item-label">
                                <label class="item-label">入力テンプレート</label>
                            </div>
                            <div class="input-group form-item">
                                <div class="button-items">
                                    <?php
                                    $template_id_list = [];
                                    foreach ($template_data_list as $template_data) {
                                        $tmp_template_id = $template_data['template_id'];
                                        $tmp_member_id = $template_data['member_id'];
                                        $tmp_category_id = $template_data['category_id'];
                                        $tmp_type = $template_data['type'];
                                        $tmp_payment = $template_data['payment'];
                                        $tmp_settlement = $template_data['settlement'];
                                        $tmp_amount = $template_data['amount'];
                                        $tmp_remarks = $template_data['remarks'];
                                        $tmp_use = $template_data['use'];
                                        $tmp_deleted = $template_data['deleted'];

                                        if (!isExistMember($tmp_member_id, $_SESSION['user_groups_id'])) {
                                            continue;
                                        }
                                        if (!isExistCategory($tmp_category_id, $_SESSION['user_groups_id'])) {
                                            continue;
                                        }
                                        if (!isExistChoiceType($tmp_type) or !isExistChoicePayment($tmp_payment) or !isExistChoiceSettlement($tmp_settlement)) {
                                            continue;
                                        }
                                        if ($tmp_use != 1 or $tmp_deleted == 1) {
                                            continue;
                                        }
                                        $template_id_list[] = $tmp_template_id;
                                        echo "<button type='button' class='btn btn-secondary' id='{$template_data['template_id']}' value='{$tmp_member_id}\t{$tmp_category_id}\t{$tmp_type}\t{$tmp_payment}\t{$tmp_settlement}\t{$tmp_amount}\t{$tmp_remarks}'>{$template_data['template_name']}</button>";
                                    }
                                    ?>
                                </div>
                            </div>
                        </div>

                        <!-- 日付 -->
                        <div class="form-item">
                            <div class="form-item-label">
                                <label class="item-label">日付</label>
                            </div>
                            <div class="input-group form-item">
                                <div class="date-form">
                                    <input required type="date" class="form-control date-item" name="date"
                                        <?php $input_date = $_POST['date'] ?? '';
                                        if ($entry_error == True) {
                                            echo "value='{$input_date}'";
                                        } ?>
                                    >
                                </div>
                                <div class="date-btn-form">
                                    <button type="button" class="btn btn-primary date-btn-item" name="today-btn">
                                        当日
                                    </button>
                                </div>
                            </div>
                        </div>


                        <!-- 購入者 -->
                        <div class="form-item member-item">
                            <div class="form-item-label">
                                <label class="item-label">購入者</label>
                            </div>
                            <div class="input-group form-item">
                                <select required class="form-select" name="member">
                                    <option selected>選択してください</option>
                                    <?php
                                    foreach ($member_list as $member_id => $member_name){
                                        if(!isExistMember($member_id,$_SESSION['user_groups_id'])){
                                            continue;
                                        }
                                        echo "<option value='{$member_id}'>{$member_name}</option>";
                                    }
                                    ?>
                                </select>
                            </div>
                        </div>

                        <div class="pc-form">
                            <!-- カテゴリー -->
                            <div class="form-item category-item">
                                <div class="form-item-label">
                                    <label class="item-label">カテゴリー</label>
                                </div>
                                <div class="input-group form-item">
                                    <select required class="form-select" name="category">
                                        <option selected>選択してください</option>
                                        <?php
                                        foreach ($category_list as $category_id => $category_name){
                                            if(!isExistCategory($category_id,$_SESSION['user_groups_id'])){
                                                continue;
                                            }
                                            echo "<option value='{$category_id}'>{$category_name}</option>";
                                        }
                                        ?>
                                    </select>
                                </div>
                            </div>

                            <!-- 種別 -->
                            <div class="form-item type-item">
                                <div class="form-item-label">
                                    <label class="item-label">種別</label>
                                </div>
                                <div class="input-group form-item">
                                    <select required class="form-select" name="type">
                                        <?php
                                        foreach ($type_list as $item_no => $item_name){
                                            if(!isExistChoiceType($item_no)){
                                                continue;
                                            }
                                            echo "<option value='{$item_no}'>{$item_name}</option>";
                                        }
                                        ?>
                                    </select>
                                </div>
                            </div>

                            <!-- 支払 -->
                            <div class="form-item payment-item">
                                <div class="form-item-label">
                                    <label class="item-label">支払</label>
                                </div>
                                <div class="input-group form-item">
                                    <select required class="form-select" name="payment">
                                        <?php
                                        foreach ($payment_list as $item_no => $item_name){
                                            if(!isExistChoicePayment($item_no)){
                                                continue;
                                            }
                                            echo "<option value='{$item_no}'>{$item_name}</option>";
                                        }
                                        ?>
                                    </select>
                                </div>
                            </div>

                            <!-- 精算 -->
                            <div class="form-item settlement-item">
                                <div class="form-item-label">
                                    <label class="item-label">精算</label>
                                </div>
                                <div class="input-group form-item">
                                    <select required class="form-select" name="settlement">
                                        <?php
                                        foreach ($settlement_list as $item_no => $item_name){
                                            if(!isExistChoiceSettlement($item_no)){
                                                continue;
                                            }
                                            echo "<option value='{$item_no}'>{$item_name}</option>";
                                        }
                                        ?>
                                    </select>
                                </div>
                            </div>
                        </div>

                        <div class="pc-form">
                            <!-- 金額 -->
                            <div class="form-item amount-item">
                                <div class="form-item-label">
                                    <label class="item-label">金額</label>
                                </div>
                                <div class="input-group form-item">
                                    <input required type="number" class="form-control" name="amount"
                                        <?php $input_amount = $_POST['amount'] ?? '';
                                        if ($entry_error == True) {
                                            echo "value='{$input_amount}'";
                                        } ?>
                                    >
                                </div>
                            </div>

                            <!-- 備考 -->
                            <div class="form-item remarks-item">
                                <div class="form-item-label">
                                    <label class="item-label">備考</label>
                                </div>
                                <div class="input-group form-item">
                                    <input type="text" class="form-control" name="remarks"
                                        <?php $input_remarks = $_POST['remarks'] ?? '';
                                        if ($entry_error == True) {
                                            echo "value='{$input_remarks}'";
                                        } ?>
                                    >
                                </div>
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

<form method="post" action="">
    <div class="table-main">
        <p>購入データ入力履歴</p>
    </div>
    <div class="table-main">
        <div class="table-area">
            <table class="table table-light table-striped table-bordered table-hover">
                <thead class="table-dark">
                <tr>
                    <th>ID</th>
                    <th><?php echo UI_ITEM_SHOPPING_DATE; ?></th>
                    <th><?php echo UI_ITEM_SHOPPING_USER; ?></th>
                    <th><?php echo UI_ITEM_SHOPPING_MEMBER; ?></th>
                    <th><?php echo UI_ITEM_SHOPPING_CATEGORY; ?></th>
                    <th><?php echo UI_ITEM_SHOPPING_TYPE; ?></th>
                    <th><?php echo UI_ITEM_SHOPPING_PAYMENT; ?></th>
                    <th><?php echo UI_ITEM_SHOPPING_SETTLEMENT; ?></th>
                    <th><?php echo UI_ITEM_SHOPPING_AMOUNT; ?></th>
                    <th><?php echo UI_ITEM_SHOPPING_REMARKS; ?></th>
                </tr>
                </thead>
                <tbody id="sortable-table">
                <?php
                if (!isset($shopping_data_list)) {
                    $shopping_data_list = [];
                }
                foreach ($shopping_data_list as $shopping_data) {
                    $shopping_id = $shopping_data['shopping_id'];
                    $groups_id = $shopping_data['groups_id'];
                    $user_id = $shopping_data['user_id'];
                    $shopping_date = $shopping_data['shopping_date'];
                    $member_id = $shopping_data['member_id'];
                    $category_id = $shopping_data['category_id'];
                    $type = $shopping_data['type'];
                    $payment = $shopping_data['payment'];
                    $settlement = $shopping_data['settlement'];
                    $amount = $shopping_data['amount'];
                    $remarks = $shopping_data['remarks'];

                    $user_name = '';
                    $user_data = getUserByUserId($user_id);
                    $user_name = $user_data['user_name'] ?? "";
                    $user_status = '';

                    $member_name = '';
                    $member_data = getMember($member_id, $_SESSION['user_groups_id']);
                    $member_name = $member_data['member_name'] ?? "";
                    $member_status = '';
                    if (isActiveMember($member_id, $_SESSION['user_groups_id']) == "isDeActive") {
                        continue;
                    }

                    $category_name = '';
                    $category_data = getCategory($category_id, $_SESSION['user_groups_id']);
                    $category_name = $category_data['category_name'] ?? "";
                    $category_status = '';
                    if (isActiveCategory($category_id, $_SESSION['user_groups_id']) == "isDeActive") {
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

                    $member_data = "$member_name{$member_status}";
                    if ($_SESSION['user_group_leader'] == 1 and $member_name != UI_ITEM_LABEL_NULL) {
                        $member_data = "<a href='/Interfaces/Views/Pages/user/group/setting/member/user_group_member_update.php?member_id={$member_id}'>$member_name{$member_status}</a>";
                    }
                    $category_data = "$category_name{$category_status}";
                    if ($_SESSION['user_group_leader'] == 1 and $category_name != UI_ITEM_LABEL_NULL) {
                        $category_data = "<a href='/Interfaces/Views/Pages/user/group/setting/category/user_group_category_update.php?category_id={$category_id}'>$category_name{$category_status}</a>";
                    }

                    echo "<tr>";
                    echo "<td>{$shopping_id}</td>";
                    echo "<td>{$shopping_date}</td>";
                    echo "<td>{$user_name}</td>";
                    echo "<td>{$member_name}</td>";
                    echo "<td>{$category_name}</td>";
                    echo "<td>{$choices_type_item_name}</td>";
                    echo "<td>{$choices_payment_item_name}</td>";
                    echo "<td>{$choices_settlement_item_name}</td>";
                    echo "<td>{$amount}</td>";
                    echo "<td>{$remarks}</td>";
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
foreach ($template_id_list as $template_id) {
    echo "
    <script>
        document.getElementById('{$template_id}').addEventListener('click', function() {
            const template_value = document.getElementById('{$template_id}').value;
            const template_item_list = template_value.split('\t');
            
            const {$template_id}_member_id = template_item_list[0];
            const {$template_id}_category_id = template_item_list[1];
            const {$template_id}_type = template_item_list[2];
            const {$template_id}_payment = template_item_list[3];
            const {$template_id}_settlement = template_item_list[4];
            const {$template_id}_amount = template_item_list[5];
            const {$template_id}_remarks = template_item_list[6];
            
            const {$template_id}_elem_member = document.getElementsByName('member')[0];
            const {$template_id}_elem_category = document.getElementsByName('category')[0];
            const {$template_id}_elem_type = document.getElementsByName('type')[0];
            const {$template_id}_elem_payment = document.getElementsByName('payment')[0];
            const {$template_id}_elem_settlement = document.getElementsByName('settlement')[0];
            const {$template_id}_elem_amount = document.getElementsByName('amount')[0];
            const {$template_id}_elem_remarks = document.getElementsByName('remarks')[0];

            {$template_id}_elem_member.value = {$template_id}_member_id;
            {$template_id}_elem_category.value = {$template_id}_category_id;
            {$template_id}_elem_type.value = {$template_id}_type;
            {$template_id}_elem_payment.value = {$template_id}_payment;
            {$template_id}_elem_settlement.value = {$template_id}_settlement;
            {$template_id}_elem_amount.value = {$template_id}_amount;
            {$template_id}_elem_remarks.value = {$template_id}_remarks;

        });
    </script>
    ";
}
?>
<?php
require $_SERVER['DOCUMENT_ROOT'] . '/Interfaces/Assets/JS/basic_js.php';
?>


</body>
</html>



