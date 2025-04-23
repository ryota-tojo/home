<?php

require $_SERVER['DOCUMENT_ROOT'] . '/config/config.php';
require $_SERVER['DOCUMENT_ROOT'] . '/Application/Services/ApiService.php';
require $_SERVER['DOCUMENT_ROOT'] . '/Application/Services/base64Service.php';
require $_SERVER['DOCUMENT_ROOT'] . '/Interfaces/Views/Partials/requireApi.php';

require $_SERVER['DOCUMENT_ROOT'] . '/Interfaces/Views/Partials/initialization.php';

session_start();

//画面名
$screen_title = "ログイン";

// 初期設定
initialization();

// セッション初期設定
$_SESSION['error_cnt'] = $_SESSION['error_cnt'] ?? 0;
$_SESSION['account_lockout'] = $_SESSION['account_lockout'] ?? False;

// 変数初期化
$login_error = False;
$lockout = False;
$login_check_message = '';
$master_setting_login_failure_limit = null;
$master_setting_maintenance = 0;

// マスター設定
$master_setting_api_result = apiCallMasterSettingRefer();
foreach ($master_setting_api_result['data']['setting_list'] as $setting) {
    if ($setting['setting_key'] == 'login_failure_limit') {
        $master_setting_login_failure_limit = $setting['setting_value'];
    }
    if ($setting['setting_key'] == 'maintenance') {
        $master_setting_maintenance = $setting['setting_value'];
    }
}

// URLパラメータによる処理
if (isset($_GET['user_name']) and isset($_GET['password'])) {
    $user_name = $_GET['user_name'];
    if (isBase64($_GET['user_name'])) {
        $user_name = base64_decode($_GET['user_name']);
    }
    $password = $_GET['password'];
    if (isBase64($_GET['password'])) {
        $password = base64_decode($_GET['password']);
    }

    $login_check_api_result = apiCallLoginCheck($user_name, $password);
    $login_check_result = $login_check_api_result['status'];

    if ($login_check_result != 'success') {
        $login_check_message = $login_check_api_result['data']['message'];
        $_SESSION['error_cnt'] += 1;
        $login_error = True;
    } else {
        unset($_SESSION['error_cnt']);
        unset($_SESSION['account_lockout']);
        $user_refer_api_result = apiCallUserRefer(null, $user_name);

        $_SESSION['user_id'] = null;
        $_SESSION['user_name'] = null;
        $_SESSION['user_password'] = null;
        $_SESSION['user_permission'] = null;
        $_SESSION['user_approval_flg'] = null;
        $_SESSION['user_delete_flg'] = null;

        $_SESSION['user_settings'] = null;
        $_SESSION['user_groups_id'] = null;
        $_SESSION['user_group_leader'] = null;
        $_SESSION['user_group_approval_flg'] = null;

        foreach ($user_refer_api_result['data']['user'] as $user) {
            $userInfo = $user['user_info'];
            $userSettings = $user['user_setting'];
            $groupInfoList = $user['group_info'];

            $_SESSION['user_id'] = $userInfo['user_id'];
            $_SESSION['user_name'] = $userInfo['user_name'];
            $_SESSION['user_password'] = $userInfo['password'];
            $_SESSION['user_permission'] = $userInfo['permission'];
            $_SESSION['user_approval_flg'] = $userInfo['approval'];
            $_SESSION['user_delete_flg'] = $userInfo['delete'];

            $_SESSION['user_setting'] = $userSettings;

            foreach ($groupInfoList as $groupInfo) {
                $_SESSION['user_groups_id'] = $groupInfo['groups_id'];
                $_SESSION['user_group_leader'] = $groupInfo['leader'];
                $_SESSION['user_group_approval_flg'] = $groupInfo['approval'];
            }
        }

        if($master_setting_maintenance == "1"){
            if($_SESSION['user_permission'] != 2){
                echo "<script>window.location.href = 'maintenance.php';</script>";
            }
        }

        echo "<script>window.location.href = 'home.php';</script>";
    }
}

// ボタン押下時の処理
if (isset($_POST['login-btn'])) {

    $user_name = $_POST['user-name'] ?? '';
    $password = $_POST['password'] ?? '';

    $login_check_api_result = apiCallLoginCheck($user_name, $password);
    $login_check_result = $login_check_api_result['status'];

    if ($login_check_result != 'success') {
        $login_check_message = $login_check_api_result['data']['message'];
        $_SESSION['error_cnt'] += 1;
        $login_error = True;
    } else {
        unset($_SESSION['error_cnt']);
        unset($_SESSION['account_lockout']);
        $user_refer_api_result = apiCallUserRefer(null, $user_name);

        $_SESSION['user_id'] = null;
        $_SESSION['user_name'] = null;
        $_SESSION['user_password'] = null;
        $_SESSION['user_permission'] = null;
        $_SESSION['user_approval_flg'] = null;
        $_SESSION['user_delete_flg'] = null;

        $_SESSION['user_settings'] = null;
        $_SESSION['user_groups_id'] = null;
        $_SESSION['user_group_leader'] = null;
        $_SESSION['user_group_approval_flg'] = null;

        foreach ($user_refer_api_result['data']['user'] as $user) {
            $userInfo = $user['user_info'];
            $userSettings = $user['user_setting'];
            $groupInfoList = $user['group_info'];

            $_SESSION['user_id'] = $userInfo['user_id'];
            $_SESSION['user_name'] = $userInfo['user_name'];
            $_SESSION['user_password'] = $userInfo['password'];
            $_SESSION['user_permission'] = $userInfo['permission'];
            $_SESSION['user_approval_flg'] = $userInfo['approval'];
            $_SESSION['user_delete_flg'] = $userInfo['delete'];

            $_SESSION['user_setting'] = $userSettings;

            foreach ($groupInfoList as $groupInfo) {
                $_SESSION['user_groups_id'] = $groupInfo['groups_id'];
                $_SESSION['user_group_leader'] = $groupInfo['leader'];
                $_SESSION['user_group_approval_flg'] = $groupInfo['approval'];
            }
        }

        if($master_setting_maintenance == "1"){
            if($_SESSION['user_permission'] != 2){
                echo "<script>window.location.href = 'maintenance.php';</script>";
            }
        }

        echo "<script>window.location.href = 'home.php';</script>";
    }
}
if (isset($_POST['user-entry-btn'])) {
    echo "<script>window.location.href = 'user_entry.php';</script>";
}

// アカウントロック判定
$disabled = "";
if ($_SESSION['error_cnt'] >= $master_setting_login_failure_limit) {
    $_SESSION['account_lockout'] = True;
    $disabled = "disabled";
}
?>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><?php echo $screen_title; ?></title>
    <link rel="stylesheet" href="/Interfaces/Assets/CSS/font.css">
    <link rel="stylesheet" href="/Interfaces/Assets/CSS/login.css">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha3/dist/css/bootstrap.min.css" rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha3/dist/js/bootstrap.bundle.min.js"></script>
</head>
<body>
<header>
</header>
<main>
    <div class="title-area">
        <h2 class="title"><?php echo $screen_title; ?></h2>
    </div>
    <div class="contents">
        <div class="content-row">
            <div class="left">
            </div>
            <div class="center">
                <div class="form-menu-bl1">
                    <div class="form-menu-bl2">
                        <form action="" method="post">
                            <div class="login-form">
                                <div class="form-area">
                                    <div class="login-form-label">
                                        ユーザー名
                                    </div>
                                    <div class="login-form-input">
                                        <input type="text" required <?php echo $disabled; ?> maxlength="64"
                                               class="form-control" name="user-name"
                                               placeholder="ユーザー名を入力してください"
                                            <?php $user_name = $_POST['user-name'] ?? '';
                                            echo "value='{$user_name}'"; ?>
                                        >
                                    </div>
                                </div>

                                <div class="form-area">
                                    <div class="login-form-label">
                                        パスワード
                                    </div>
                                    <div class="login-form-input">
                                        <input type="password" required <?php echo $disabled; ?> maxlength="64"
                                               class="form-control"
                                               name="password"
                                               placeholder="パスワードを入力してください">
                                    </div>
                                </div>

                                <div class="login-msg err_msg">
                                    <?php
                                    if ($login_error == True) {
                                        if ($_SESSION['account_lockout'] == True) {
                                            echo "<div class='msg'>ログインの試行回数が許容値({$master_setting_login_failure_limit}回)を超えました。<br>ブラウザを閉じて再度最初からお試しください。</div>";
                                        } else {
                                            echo "<div class='msg'>ログインに失敗しました。({$_SESSION['error_cnt']}回目)<br>{$login_check_message}</div>";
                                        }
                                    }
                                    ?>
                                </div>

                                <div class="form-area">
                                    <div class="login-form-btn">
                                        <button type="submit" <?php echo $disabled; ?>
                                                class="btn btn-primary date-btn-item" name="login-btn">
                                            ログイン
                                        </button>
                                    </div>
                                </div>
                            </div>
                        </form>
                    </div>

                    <div class="form-menu-bl2">
                        <form action="" method="post">
                            <div class="form-area">
                                <div class="login-form-btn">
                                    <p class="login-form-title">新規登録フォーム</p>
                                </div>
                            </div>

                            <div class="form-area">
                                <div class="login-form-btn">
                                    <p class="login-form-summary">
                                        ユーザーはグループに所属する必要があります。<br>
                                        はじめての方はログイン後、グループに所属してください。<br>
                                    </p>
                                </div>
                            </div>

                            <div class="form-area">
                                <div class="login-form-btn">
                                    <button type="submit" <?php echo $disabled; ?> class="btn btn-primary date-btn-item"
                                            name="user-entry-btn">
                                        ユーザー新規登録申請
                                    </button>
                                </div>
                            </div>
                        </form>
                    </div>
                </div>
                <div class="support-bl1">
                    <div class="support-bl2">
                        ※ユーザーは承認されるまでログインすることができません。<br>
                        承認がおりるまでお待ちください。
                    </div>
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
<script>

</script>
</body>
</html>


