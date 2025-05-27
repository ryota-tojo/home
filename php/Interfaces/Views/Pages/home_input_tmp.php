<?php
if (session_status() === PHP_SESSION_NONE) {
    session_start();
}
require $_SERVER['DOCUMENT_ROOT'] . '/config/config.php';
require $_SERVER['DOCUMENT_ROOT'] . '/Application/Services/api_service.php';
require $_SERVER['DOCUMENT_ROOT'] . '/Interfaces/Views/Partials/requireApi.php';


$screen_title = "タイトル";

// 管理者判定
$admin_flag = 0;
if ($_SESSION['user_permission'] == 2) {
    $admin_flag = 1;
}
if($admin_flag == 0){
    $_SESSION['access_error'] = 1;
    echo "<script>window.location.href = 'access_error.php';</script>";
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
$master_setting_001 = "";
$master_setting_002 = "";
$master_setting_003 = "";

// マスター設定
$master_setting_api_refer_result = apiCallMasterSettingRefer();
$master_settings = [];
foreach ($master_setting_api_refer_result['data']['setting_list'] as $setting) {
    $master_settings[$setting['setting_key']] = $setting['setting_value'];
}
$master_setting_001 = $master_settings['XXXXXX'] ?? null;
$master_setting_002 = $master_settings['XXXXXX'] ?? null;
$master_setting_003 = $master_settings['XXXXXX'] ?? null;

// ボタン押下時の処理
if (isset($_POST['entry'])) {
    $entry_button_click_flg = True;


    $status = "error";
    if ($status != "success") {

        $entry_error = True;
        $message = "データの登録に失敗しました";
    } else {
        $message = "データを登録しました";
    }
}

?>

<main>
    <div class="title-area">
        <h2 class="title"><?php echo $screen_title; ?></h2>
    </div>
    <div class="summary-area">
        <div class="summary">
            XXXXXX<br>
            XXXXXX
        </div>
    </div>

    <?php
    if($entry_button_click_flg == True){
        if($entry_error == True){
            echo "<div class='message-fields error-message'>$message</div>";
        }else{
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
                                    <button type="button" class="btn btn-secondary" name="">button1</button>
                                    <button type="button" class="btn btn-secondary" name="">button2</button>
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
                                        if($entry_error == True){ echo "value='{$input_date}'";} ?>
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
                                    <option value="1">購入者１</option>
                                    <option value="2">購入者２</option>
                                    <option value="3">購入者３</option>
                                </select>
                            </div>
                        </div>

                        <div class="pc-form">
                            <!-- 分類 -->
                            <div class="form-item category-item">
                                <div class="form-item-label">
                                    <label class="item-label">分類</label>
                                </div>
                                <div class="input-group form-item">
                                    <select required class="form-select" name="category">
                                        <option selected>選択してください</option>
                                        <option value="1">分類１</option>
                                        <option value="2">分類２</option>
                                        <option value="3">分類３</option>
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
                                        <option selected>選択してください</option>
                                        <option value="1">出費</option>
                                        <option value="2">収入</option>
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
                                        <option selected>選択してください</option>
                                        <option value="1">現金</option>
                                        <option value="2">カード</option>
                                        <option value="3">振込</option>
                                        <option value="4">引落し</option>
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
                                        <option selected>選択してください</option>
                                        <option value="1">未精算</option>
                                        <option value="2">精算済</option>
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
                                        if($entry_error == True){ echo "value='{$input_amount}'";} ?>
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
                                        if($entry_error == True){ echo "value='{$input_remarks}'";} ?>
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

<footer>
    <?php require $_SERVER['DOCUMENT_ROOT'] . '/Interfaces/Views/Layouts/footer.php'; ?>
</footer>

<?php
require $_SERVER['DOCUMENT_ROOT'] . '/Interfaces/Assets/JS/basic_js.php';
?>

</body>
</html>



