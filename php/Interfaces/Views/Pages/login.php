<?php

use Application\Services\LoginCheckService;
use Domain\Models\SettingMaster;
use Domain\Models\UserInfo;
use Infrastructure\Persistence\Database;

require $_SERVER['DOCUMENT_ROOT'] . '/config/config.php';
require $_SERVER['DOCUMENT_ROOT'] . '/Infrastructure/Persistence/Database.php';
require $_SERVER['DOCUMENT_ROOT'] . '/Application/Services/LoginCheckService.php';
require $_SERVER['DOCUMENT_ROOT'] . '/Application/Services/base64Service.php';
require $_SERVER['DOCUMENT_ROOT'] . '/Domain/Models/UserInfo.php';
require $_SERVER['DOCUMENT_ROOT'] . '/Domain/Models/SettingMaster.php';

session_start();

//画面名
$screen_title = "ログイン";

//DB接続
$db_config = new Database(DB_HOST, DB_PORT, DB_USER, DB_PASSWORD, DB_NAME);
$db_config->connect();

// マスター設定
$setting_master = new SettingMaster($db_config);

// セッション初期設定
$_SESSION['error_cnt'] = $_SESSION['error_cnt'] ?? 0;
$_SESSION['account_lockout'] = $_SESSION['account_lockout'] ?? False;

// 変数初期化
$login_error = False;
$lockout = False;
$unapproved = False;

// http://localhost:8080/src/View/login.php?groups_id=bWFpbnRlbmFuY2U=&user_name=YWRtaW4=&password=YWRtaW5hZG1pbg==

// ボタン押下時の処理
if(isset($_GET['groups_id']) and isset($_GET['user_name']) and isset($_GET['password'])){
    $groups_id = $_GET['groups_id'];
    if(isBase64($_GET['groups_id'])){
        $groups_id = base64_decode($_GET['groups_id']);
    }
    $user_name = $_GET['user_name'];
    if (isBase64($_GET['user_name'])) {
        $user_name = base64_decode($_GET['user_name']);
    }
    $password = $_GET['password'];
    if (isBase64($_GET['password'])) {
        $password = base64_decode($_GET['password']);
    }

    $login_info = new LoginCheckService($db_config, $groups_id,$user_name,$password);
    $certification_results = $login_info->certification();

    if ($certification_results == -1) {
        $login_error = True;
        $_SESSION['error_cnt'] += 1;
    }elseif ($certification_results == 0) {
        $unapproved = True;
        $login_error = True;
        $_SESSION['error_cnt'] += 1;
    }else{
        unset($_SESSION['error_cnt']);
        $user_info = new UserInfo($db_config, $certification_results);
        $_SESSION['user_id'] = $user_info->getUserId();
        $_SESSION['user_name'] = $user_info->getUserName();
        $_SESSION['password'] = $user_info->getPassword();
        $_SESSION['permission'] = $user_info->getPermission();
        $_SESSION['approval_flg'] = $user_info->getApprovalFlg();
        $_SESSION['delete_flg'] = $user_info->getDeleteFlg();
        $_SESSION['create_date'] = $user_info->getCreateDate();
        $_SESSION['update_date'] = $user_info->getUpdateDate();
        $_SESSION['approval_date'] = $user_info->getApprovalDate();
        $_SESSION['delete_date'] = $user_info->getDeleteDate();
        echo "<script>window.location.href = 'home_input_tmp.php';</script>";
    }
}
if (isset($_POST['login-btn'])) {
    $groups_id = $_POST['groups-id'] ?? '';
    $user_name = $_POST['user-name'] ?? '';
    $password = $_POST['password'] ?? '';
    $login_info = new LoginCheckService($db_config, $groups_id,$user_name,$password);
    $certification_results = $login_info->certification();

    if ($certification_results == -1) {
        $login_error = True;
        $_SESSION['error_cnt'] += 1;
    }elseif ($certification_results == 0) {
        $unapproved = True;
        $login_error = True;
        $_SESSION['error_cnt'] += 1;
    }else{
        unset($_SESSION['error_cnt']);
        $user_info = new UserInfo($db_config, $certification_results);
        $_SESSION['user_id'] = $user_info->getUserId();
        $_SESSION['user_name'] = $user_info->getUserName();
        $_SESSION['password'] = $user_info->getPassword();
        $_SESSION['permission'] = $user_info->getPermission();
        $_SESSION['approval_flg'] = $user_info->getApprovalFlg();
        $_SESSION['delete_flg'] = $user_info->getDeleteFlg();
        $_SESSION['create_date'] = $user_info->getCreateDate();
        $_SESSION['update_date'] = $user_info->getUpdateDate();
        $_SESSION['approval_date'] = $user_info->getApprovalDate();
        $_SESSION['delete_date'] = $user_info->getDeleteDate();
        echo "<script>window.location.href = 'home_input_tmp.php';</script>";
    }
}
if (isset($_POST['group-entry-btn'])) {
    echo "<script>window.location.href = 'group_entry.php';</script>";
}
if (isset($_POST['user-entry-btn'])) {
    echo "<script>window.location.href = 'user_entry.php';</script>";
}

// アカウントロック判定
$disabled = "";
if ($_SESSION['error_cnt'] >= $setting_master->getAccountLockoutCount()) {
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
                                        所属グループID
                                    </div>
                                    <div class="login-form-input">
                                        <input type="text" required <?php echo $disabled; ?> maxlength="64"
                                               class="form-control" name="groups-id"
                                               placeholder="所属グループIDを入力してください"
                                            <?php $groups_id = $_POST['groups-id'] ?? '';
                                            echo "value='{$groups_id}'"; ?>
                                        >
                                    </div>
                                </div>

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
                                    if ($_SESSION['account_lockout']) {
                                        echo "<div class='msg'>ログインの試行回数が許容値({$setting_master->getAccountLockoutCount()}回)を超えました。<br>ブラウザを閉じて再度最初からお試しください。</div>";
                                    } else {
                                        if($unapproved){
                                            echo "<div class='msg'>ログインに失敗しました。({$_SESSION['error_cnt']}回目)<br>承認されていないユーザーです。</div>";
                                        }else{
                                            if ($login_error) {
                                                echo "<div class='msg'>ログインに失敗しました。({$_SESSION['error_cnt']}回目)<br>入力内容をご確認ください。</div>";
                                            }
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
                                        はじめての方は所属グループと、そのグループに紐づくユーザーを登録してください。<br>
                                    </p>
                                </div>
                            </div>

                            <div class="form-area">
                                <div class="login-form-btn">
                                    <button type="submit" <?php echo $disabled; ?> class="btn btn-primary date-btn-item"
                                            name="group-entry-btn">
                                        グループ新規登録
                                    </button>
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
    $('#sample1').datepicker();
</script>
</body>
</html>

<?php
$db_config->close();
?>

