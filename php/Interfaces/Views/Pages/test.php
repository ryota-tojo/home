<?php
if (session_status() === PHP_SESSION_NONE) {
    session_start();
}
require_once $_SERVER['DOCUMENT_ROOT'] . '/Config/config.php';
require_once $_SERVER['DOCUMENT_ROOT'] . '/Application/Services/ApiService.php';
require_once $_SERVER['DOCUMENT_ROOT'] . '/Interfaces/Views/Partials/requireApi.php';
require_once $_SERVER['DOCUMENT_ROOT'] . '/Interfaces/Views/Partials/systems/screen/get_screen.php';

require_once $_SERVER['DOCUMENT_ROOT'] . '/Interfaces/Views/Partials/budgets/get_budgets.php';


$screen_items = getScreen(basename(__FILE__));
$screen_title = $screen_items['name'];
$screen_remarks = $screen_items['remarks'];

// 管理者判定
$admin_flag = 0;
if ($_SESSION['user_permission'] == 2) {
    $admin_flag = 1;
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
    <div class="contents">
        <div class="content-row">
            <div class="left">
            </div>
            <div class="center">
                <?php

                require_once $_SERVER['DOCUMENT_ROOT'] . '/Interfaces/Views/Partials/shopping/import_shopping_data.php';

                // 正常系
                $groups_id = $_SESSION['user_groups_id'];
                $user_id = $_SESSION['user_id'];
                $shopping_date = "2025-01-01";
                $member_id = "1";
                $category_id = "1";
                $type = "1";
                $payment = "1";
                $settlement = "1";
                $amount = 1000;
                $remarks = "aaaa";
                if(isShoppingDataCreatable($groups_id, $user_id, $shopping_date, $member_id, $category_id, $type, $payment, $settlement, $amount, $remarks)){
                    echo "OK<br>";
                }else{
                    echo "NG<br>";
                }

                // 異常系
                // 所属グループID
                //   - 必須チェック
                $groups_id = null;
                if(!isShoppingDataCreatable($groups_id, $user_id, $shopping_date, $member_id, $category_id, $type, $payment, $settlement, $amount, $remarks)){
                    echo "OK<br>";
                }else{
                    echo "NG<br>";
                }
                //   - 空欄チェック
                $groups_id = "";
                if(!isShoppingDataCreatable($groups_id, $user_id, $shopping_date, $member_id, $category_id, $type, $payment, $settlement, $amount, $remarks)){
                    echo "OK<br>";
                }else{
                    echo "NG<br>";
                }
                //   - 存在チェック
                $groups_id = "hoge";
                if(!isShoppingDataCreatable($groups_id, $user_id, $shopping_date, $member_id, $category_id, $type, $payment, $settlement, $amount, $remarks)){
                    echo "OK<br>";
                }else{
                    echo "NG<br>";
                }
                $groups_id = $_SESSION['user_groups_id'];

                // ユーザーID
                //   - 必須チェック
                $user_id = null;
                if(!isShoppingDataCreatable($groups_id, $user_id, $shopping_date, $member_id, $category_id, $type, $payment, $settlement, $amount, $remarks)){
                    echo "OK<br>";
                }else{
                    echo "NG<br>";
                }
                //   - 空欄チェック
                $user_id = "";
                if(!isShoppingDataCreatable($groups_id, $user_id, $shopping_date, $member_id, $category_id, $type, $payment, $settlement, $amount, $remarks)){
                    echo "OK<br>";
                }else{
                    echo "NG<br>";
                }
                //   - 形式チェック（数値）
                $user_id = "abcd";
                if(!isShoppingDataCreatable($groups_id, $user_id, $shopping_date, $member_id, $category_id, $type, $payment, $settlement, $amount, $remarks)){
                    echo "OK<br>";
                }else{
                    echo "NG<br>";
                }
                //   - 存在チェック
                $user_id = "999999";
                if(!isShoppingDataCreatable($groups_id, $user_id, $shopping_date, $member_id, $category_id, $type, $payment, $settlement, $amount, $remarks)){
                    echo "OK<br>";
                }else{
                    echo "NG<br>";
                }
                $user_id = $_SESSION['user_id'];

                // 購入日
                //   - 必須チェック
                $shopping_date = null;
                if(!isShoppingDataCreatable($groups_id, $user_id, $shopping_date, $member_id, $category_id, $type, $payment, $settlement, $amount, $remarks)){
                    echo "OK<br>";
                }else{
                    echo "NG<br>";
                }
                //   - 空欄チェック
                $shopping_date = "";
                if(!isShoppingDataCreatable($groups_id, $user_id, $shopping_date, $member_id, $category_id, $type, $payment, $settlement, $amount, $remarks)){
                    echo "OK<br>";
                }else{
                    echo "NG<br>";
                }
                //   - 形式チェック（yyyy-MM-dd）
                $shopping_date = "2025/01/01";
                if(!isShoppingDataCreatable($groups_id, $user_id, $shopping_date, $member_id, $category_id, $type, $payment, $settlement, $amount, $remarks)){
                    echo "OK<br>";
                }else{
                    echo "NG<br>";
                }
                $shopping_date = "2025-01-01";

                // メンバーID
                //   - 必須チェック
                $member_id = null;
                if(!isShoppingDataCreatable($groups_id, $user_id, $shopping_date, $member_id, $category_id, $type, $payment, $settlement, $amount, $remarks)){
                    echo "OK<br>";
                }else{
                    echo "NG<br>";
                }
                //   - 空欄チェック
                $member_id = "";
                if(!isShoppingDataCreatable($groups_id, $user_id, $shopping_date, $member_id, $category_id, $type, $payment, $settlement, $amount, $remarks)){
                    echo "OK<br>";
                }else{
                    echo "NG<br>";
                }
                //   - 形式チェック（数値）
                $member_id = "abc";
                if(!isShoppingDataCreatable($groups_id, $user_id, $shopping_date, $member_id, $category_id, $type, $payment, $settlement, $amount, $remarks)){
                    echo "OK<br>";
                }else{
                    echo "NG<br>";
                }
                //   - 存在チェック
                $member_id = "999";
                if(!isShoppingDataCreatable($groups_id, $user_id, $shopping_date, $member_id, $category_id, $type, $payment, $settlement, $amount, $remarks)){
                    echo "OK<br>";
                }else{
                    echo "NG<br>";
                }
                $member_id = "1";

                // カテゴリーID
                //   - 必須チェック
                $category_id = null;
                if(!isShoppingDataCreatable($groups_id, $user_id, $shopping_date, $member_id, $category_id, $type, $payment, $settlement, $amount, $remarks)){
                    echo "OK<br>";
                }else{
                    echo "NG<br>";
                }
                //   - 空欄チェック
                $category_id = "";
                if(!isShoppingDataCreatable($groups_id, $user_id, $shopping_date, $member_id, $category_id, $type, $payment, $settlement, $amount, $remarks)){
                    echo "OK<br>";
                }else{
                    echo "NG<br>";
                }
                //   - 形式チェック（数値）
                $category_id = "abc";
                if(!isShoppingDataCreatable($groups_id, $user_id, $shopping_date, $member_id, $category_id, $type, $payment, $settlement, $amount, $remarks)){
                    echo "OK<br>";
                }else{
                    echo "NG<br>";
                }
                //   - 存在チェック
                $category_id = "999";
                if(!isShoppingDataCreatable($groups_id, $user_id, $shopping_date, $member_id, $category_id, $type, $payment, $settlement, $amount, $remarks)){
                    echo "OK<br>";
                }else{
                    echo "NG<br>";
                }
                $category_id = "1";

                // 種別
                //   - 必須チェック
                $type = null;
                if(!isShoppingDataCreatable($groups_id, $user_id, $shopping_date, $member_id, $category_id, $type, $payment, $settlement, $amount, $remarks)){
                    echo "OK<br>";
                }else{
                    echo "NG<br>";
                }
                //   - 空欄チェック
                $type = "";
                if(!isShoppingDataCreatable($groups_id, $user_id, $shopping_date, $member_id, $category_id, $type, $payment, $settlement, $amount, $remarks)){
                    echo "OK<br>";
                }else{
                    echo "NG<br>";
                }
                //   - 形式チェック（数値）
                $type = "abc";
                if(!isShoppingDataCreatable($groups_id, $user_id, $shopping_date, $member_id, $category_id, $type, $payment, $settlement, $amount, $remarks)){
                    echo "OK<br>";
                }else{
                    echo "NG<br>";
                }
                //   - 存在チェック
                $type = "999";
                if(!isShoppingDataCreatable($groups_id, $user_id, $shopping_date, $member_id, $category_id, $type, $payment, $settlement, $amount, $remarks)){
                    echo "OK<br>";
                }else{
                    echo "NG<br>";
                }
                $type = "1";

                // 支払い
                //   - 必須チェック
                $payment = null;
                if(!isShoppingDataCreatable($groups_id, $user_id, $shopping_date, $member_id, $category_id, $type, $payment, $settlement, $amount, $remarks)){
                    echo "OK<br>";
                }else{
                    echo "NG<br>";
                }
                //   - 空欄チェック
                $payment = "";
                if(!isShoppingDataCreatable($groups_id, $user_id, $shopping_date, $member_id, $category_id, $type, $payment, $settlement, $amount, $remarks)){
                    echo "OK<br>";
                }else{
                    echo "NG<br>";
                }
                //   - 形式チェック（数値）
                $payment = "abc";
                if(!isShoppingDataCreatable($groups_id, $user_id, $shopping_date, $member_id, $category_id, $type, $payment, $settlement, $amount, $remarks)){
                    echo "OK<br>";
                }else{
                    echo "NG<br>";
                }
                //   - 存在チェック
                $payment = "999";
                if(!isShoppingDataCreatable($groups_id, $user_id, $shopping_date, $member_id, $category_id, $type, $payment, $settlement, $amount, $remarks)){
                    echo "OK<br>";
                }else{
                    echo "NG<br>";
                }
                $payment = "1";

                // 精算
                //   - 必須チェック
                $settlement = null;
                if(!isShoppingDataCreatable($groups_id, $user_id, $shopping_date, $member_id, $category_id, $type, $payment, $settlement, $amount, $remarks)){
                    echo "OK<br>";
                }else{
                    echo "NG<br>";
                }
                //   - 空欄チェック
                $settlement = "";
                if(!isShoppingDataCreatable($groups_id, $user_id, $shopping_date, $member_id, $category_id, $type, $payment, $settlement, $amount, $remarks)){
                    echo "OK<br>";
                }else{
                    echo "NG<br>";
                }
                //   - 形式チェック（数値）
                $settlement = "abc";
                if(!isShoppingDataCreatable($groups_id, $user_id, $shopping_date, $member_id, $category_id, $type, $payment, $settlement, $amount, $remarks)){
                    echo "OK<br>";
                }else{
                    echo "NG<br>";
                }
                //   - 存在チェック
                $settlement = "999";
                if(!isShoppingDataCreatable($groups_id, $user_id, $shopping_date, $member_id, $category_id, $type, $payment, $settlement, $amount, $remarks)){
                    echo "OK<br>";
                }else{
                    echo "NG<br>";
                }
                $settlement = "1";

                // 金額
                //   - 必須チェック
                $amount = null;
                if(!isShoppingDataCreatable($groups_id, $user_id, $shopping_date, $member_id, $category_id, $type, $payment, $settlement, $amount, $remarks)){
                    echo "OK<br>";
                }else{
                    echo "NG<br>";
                }
                //   - 空欄チェック
                $amount = "";
                if(!isShoppingDataCreatable($groups_id, $user_id, $shopping_date, $member_id, $category_id, $type, $payment, $settlement, $amount, $remarks)){
                    echo "OK<br>";
                }else{
                    echo "NG<br>";
                }
                //   - 形式チェック（数値）
                $amount = "abc";
                if(!isShoppingDataCreatable($groups_id, $user_id, $shopping_date, $member_id, $category_id, $type, $payment, $settlement, $amount, $remarks)){
                    echo "OK<br>";
                }else{
                    echo "NG<br>";
                }
                //   - 数値範囲チェック
                $amount = "0";
                if(!isShoppingDataCreatable($groups_id, $user_id, $shopping_date, $member_id, $category_id, $type, $payment, $settlement, $amount, $remarks)){
                    echo "OK<br>";
                }else{
                    echo "NG<br>";
                }
                $amount = "1000";

                // 備考
                //   - 必須チェック
                $remarks = null;
                if(!isShoppingDataCreatable($groups_id, $user_id, $shopping_date, $member_id, $category_id, $type, $payment, $settlement, $amount, $remarks)){
                    echo "OK<br>";
                }else{
                    echo "NG<br>";
                }





                ?>

            </div>
            <div class="right">
            </div>
        </div>
    </div>


</main>

<footer>
    <?php require $_SERVER['DOCUMENT_ROOT'] . '/Interfaces/Views/Layouts/footer.php'; ?>
</footer>

<!-- bootstrap-datepickerのjavascriptコード -->
<script>
    $('#sample1').datepicker();
</script>
</body>
</html>
