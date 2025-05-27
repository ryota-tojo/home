<?php
if (session_status() === PHP_SESSION_NONE) {
    session_start();
}
require $_SERVER['DOCUMENT_ROOT'] . '/config/config.php';
require $_SERVER['DOCUMENT_ROOT'] . '/Application/Services/api_service.php';
require $_SERVER['DOCUMENT_ROOT'] . '/Interfaces/Views/Partials/requireApi.php';


$screen_title = "通知設定";

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
    <link rel="stylesheet" href="/Interfaces/Assets/CSS/setting_form.css">
    <link rel="stylesheet" href="/Interfaces/Assets/CSS/message.css">
    <link rel="stylesheet" href="/Interfaces/Assets/CSS/button_form.css">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha3/dist/css/bootstrap.min.css" rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha3/dist/js/bootstrap.bundle.min.js"></script>
</head>

<body class="<?php if($admin_flag == 1){echo 'admin-body';}else{echo 'body';}?>">
<header>
    <?php require $_SERVER['DOCUMENT_ROOT'] . '/Interfaces/Views/Partials/nav.php'; ?>
</header>

<?php
ob_flush();
flush();

// 変数初期化
$send_button_click_flg = False;
$entry_button_click_flg = False;
$message = "";
$entry_error = False;
$send_error = 0;

// ボタン押下時の処理
if (isset($_POST['send_test'])) {
    $send_button_click_flg = True;
    $send_error = -1;
    $message = "通知機能が実装されていません";
}
if (isset($_POST['entry'])) {
    $entry_button_click_flg = True;

    $status = "success";
    $notification_send_flg = isset($_POST['notification_send_flg']) ? "1" : "0";
    $notification_url = filter_var($_POST['notification_url'] ?? '', FILTER_SANITIZE_URL);
    $notification_token = htmlspecialchars($_POST['notification_token'] ?? '', ENT_QUOTES, 'UTF-8');

    $results = [
        'notification_send_flg' => $notification_send_flg,
        'notification_url' => $notification_url,
        'notification_token' => $notification_token
    ];

    $all_success = true;
    $error_keys = [];

    foreach ($results as $setting_name => $value) {
        $result = apiCallMasterSettingUpdate($setting_name, $value);

        // エラーチェック
        if (isset($result['status']) && $result['status'] === 'error') {
            $all_success = false;
            $error_keys[] = $setting_name;
        }
    }

    if (!$all_success) {
        $error_keys_str = implode(', ', $error_keys);
        $message = "設定の更新に失敗しました。<br>エラーが発生した設定: " . $error_keys_str;
        $entry_error = true;
    } else {
        $message = "設定が正常に更新されました。";
    }
}

// マスター設定
$master_setting_api_refer_result = apiCallMasterSettingRefer();
$master_settings = [];
foreach ($master_setting_api_refer_result['data']['setting_list'] as $setting) {
    $master_settings[$setting['setting_key']] = $setting['setting_value'];
}

$notification_send_flg = $master_settings['notification_send_flg'] ?? null;
$notification_url = $master_settings['$notification_url'] ?? null;
$notification_token = $master_settings['$notification_token'] ?? null;

?>

<main>
    <div class="title-area">
        <h2 class="title"><?php echo $screen_title; ?></h2>
    </div>
    <div class="summary-area">
        <div class="summary">
            通知関連の設定を管理する
        </div>
    </div>

    <?php
    if($send_button_click_flg == True) {
        if ($send_error == -1) {
            echo "<div class='message-fields warning-message'>$message</div>";
        }
        if ($send_error == 0) {
            echo "<div class='message-fields success-message'>$message</div>";
        }
        if ($send_error == 1) {
            echo "<div class='message-fields error-message'>$message</div>";
        }
    }

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
                <div class="settings">
                    <form action="" method="post">
                        <div class="settings-section">
                            <h4 class="settings-title">通知設定</h4>
                            <hr>

                            <!-- 通知送信設定 -->
                            <div class="settings-form">
                                <div class="settings-label-container">
                                    <div class="settings-label">
                                        通知送信設定
                                        <div class="optional-comment">※任意</div>
                                    </div>
                                </div>
                                <div class="settings-input-container">
                                    <div class="settings-input">
                                        <input type="checkbox" name="notification_send_flg"
                                            <?php echo (isset($master_settings['notification_send_flg']) && $master_settings['notification_send_flg'] == 1) ? 'checked' : ''; ?>>
                                        <label for="notification_send_flg">通知を送信する</label>
                                    </div>
                                </div>
                            </div>

                            <!-- 通知URL -->
                            <div class="settings-form">
                                <div class="settings-label-container">
                                    <div class="settings-label">
                                        通知URL
                                        <div class="optional-comment">※任意</div>
                                    </div>
                                </div>
                                <div class="settings-input-container">
                                    <div class="settings-input">
                                        <input type="url" class="form-control" name="notification_url"
                                               value="<?php echo $master_settings['notification_url'] ?? ''; ?>" placeholder="https://default">
                                    </div>
                                </div>
                            </div>

                            <!-- 通知トークン -->
                            <div class="settings-form">
                                <div class="settings-label-container">
                                    <div class="settings-label">
                                        通知トークン
                                        <div class="optional-comment">※任意</div>
                                    </div>
                                </div>
                                <div class="settings-input-container">
                                    <div class="settings-input">
                                        <input type="text" class="form-control" name="notification_token"
                                               value="<?php echo $master_settings['notification_token'] ?? ''; ?>" placeholder="通知トークンを入力">
                                    </div>
                                </div>
                            </div>

                            <!-- テスト通知 -->
                            <div class="settings-form">
                                <div class="settings-label-container">
                                    <div class="settings-label">
                                        テスト通知
                                    </div>
                                </div>
                                <div class="settings-input-container">
                                    <div class="settings-input">
                                        <button type="submit" class="btn btn-primary" name="send_test">テスト通知を送信する</button>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <!-- 更新ボタン -->
                        <div class="btn-area">
                            <div class="btn-center-area">
                                <div class="btn-item">
                                    <div class="settings-submit">
                                        <button type="submit" class="btn btn-primary" name="entry">更新</button>
                                    </div>
                                </div>
                            </div>
                        </div>

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



