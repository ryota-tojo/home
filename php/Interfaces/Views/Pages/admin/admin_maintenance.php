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

$screen_items = getScreen( basename(__FILE__));
$screen_title = $screen_items['name'];
$screen_remarks = $screen_items['remarks'];

// 管理者判定
$admin_flag = 0;
if ($_SESSION['user_permission'] == 2) {
    $admin_flag = 1;
}
if ($admin_flag == 0) {
    $_SESSION['access_error'] = 1;
    echo "<script>window.location.href = 'access_error.php';</script>";
}

// 変数初期化
$entry_button_click_flg = False;
$message = "";
$entry_error = False;
$master_setting_maintenance = "";

// ボタン押下時の処理
if (isset($_POST['entry'])) {
    $entry_button_click_flg = True;

    $master_setting_update_api_result = apiCallMasterSettingUpdate("maintenance", "1", "メンテナンス判定");

    $status = $master_setting_update_api_result['status'];
    if ($status != "success") {

        $entry_error = True;
        $message = "メンテナンス状態への変更に失敗しました";
        createLogs(LOG_TYPE_ERROR, "メンテナンス状態の変更に失敗");
    } else {
        $message = "メンテナンス状態に変更しました";
        createLogs(LOG_TYPE_INFO, "メンテナンス状態に変更");
    }
}
if (isset($_POST['un_entry'])) {
    $entry_button_click_flg = True;

    $master_setting_update_api_result = apiCallMasterSettingUpdate("maintenance", "0", "メンテナンス判定");

    $status = $master_setting_update_api_result['status'];
    if ($status != "success") {

        $entry_error = True;
        $message = "メンテナンス状態の解除に失敗しました";
        createLogs(LOG_TYPE_ERROR, "メンテナンス状態の解除に失敗");
    } else {
        $message = "メンテナンス状態を解除しました";
        createLogs(LOG_TYPE_INFO, "メンテナンス状態を解除");
    }
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
    <link rel="stylesheet" href="/Interfaces/Assets/CSS/setting_form.css">
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
    <?php
    require $_SERVER['DOCUMENT_ROOT'] . '/Interfaces/Views/Partials/nav.php';
    ?>
</header>

<?php
ob_flush();
flush();

// マスター設定
if ($entry_error == False) {
    $master_setting_refer_api_result = apiCallMasterSettingRefer();
    foreach ($master_setting_refer_api_result['data']['setting_list'] as $setting) {
        if ($setting['setting_key'] == 'maintenance') {
            $master_setting_maintenance = $setting['setting_value'];
        }
    }
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
    ?>

    <div class="contents">
        <div class="content-row">
            <div class="left">
            </div>
            <div class="center">
                <div class="settings">
                    <form action="" method="post">

                        <?php if ($master_setting_maintenance == 0) { ?>
                            <!-- 登録ボタン -->
                            <div class="btn-area">
                                <div class="btn-center-area">
                                    <div class="btn-item">
                                        <button type="submit" class="btn btn-primary" name="entry">
                                            メンテナンス状態に変更する
                                        </button>
                                    </div>
                                </div>
                            </div>
                        <?php } ?>

                        <?php if ($master_setting_maintenance == 1) { ?>
                            <!-- 解除ボタン -->
                            <div class="btn-area">
                                <div class="btn-center-area">
                                    <div class="btn-item">
                                        <button type="submit" class="btn btn-success" name="un_entry">
                                            メンテナンス状態を解除する
                                        </button>
                                    </div>
                                </div>
                            </div>
                        <?php } ?>

                    </form>
                </div>
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



