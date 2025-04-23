<?php

require $_SERVER['DOCUMENT_ROOT'] . '/config/config.php';
require $_SERVER['DOCUMENT_ROOT'] . '/Application/Services/ApiService.php';
require $_SERVER['DOCUMENT_ROOT'] . '/Interfaces/Views/Partials/requireApi.php';

session_start();

$screen_title = "ユーザー新規登録申請";

// 変数初期化
$entry = False;
$entry_error = False;
$error_msg = "";
$password_details = "※パスワードは以下の条件を満たす必要があります
                    ・8文字以上
                    ・小文字の英字（a～z）を1文字以上含む
                    ・大文字の英字（A～Z）を1文字以上含む
                    ・数字（0～9）を1文字以上含む
                    ・特殊文字（@, $, !, %, *, ?, &）を1文字以上含む
                    ・空白文字（スペース、タブ、改行）は含めないでください";
$master_setting_maintenance = 0;

// マスター設定
$master_setting_api_result = apiCallMasterSettingRefer();
foreach ($master_setting_api_result['data']['setting_list'] as $setting) {
    if ($setting['setting_key'] == 'maintenance') {
        $master_setting_maintenance = $setting['setting_value'];
    }
}
if($master_setting_maintenance == "1"){
    echo "<script>window.location.href = 'maintenance.php';</script>";
}

// ボタン押下時の処理
if (isset($_POST['back-btn'])) {
    echo "<script>window.location.href = 'login.php';</script>";
}
if (isset($_POST['application-btn'])) {

    $user_name = $_POST['user-name'] ?? '';
    $password = $_POST['password'] ?? '';
    $re_password = $_POST['re-password'] ?? '';

    if($password == $re_password){
        $user_create_api_result = apiCallUserCreate($user_name,$password);
        $user_create_result = $user_create_api_result['status'];

        if($user_create_result=='success'){
            $entry = True;
        }else{
            $error_msg = str_replace("データ","ユーザー",$user_create_api_result['data']['message']);
            $entry_error = True;
        }
    }else{
        $error_msg = "入力したパスワードが一致しません";
        $entry_error = True;
    }
}

?>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><?php echo $screen_title; ?></title>
    <link rel="stylesheet" href="/Interfaces/Assets/CSS/font.css">
    <link rel="stylesheet" href="/Interfaces/Assets/CSS/control.css">
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
                            <div class="entry-form">
                                <div class="form-area">
                                    <div class="login-form-label">
                                        ユーザー名
                                    </div>
                                    <div class="login-form-input">
                                        <input type="text" required minlength="8" maxlength="32" oninput="this.value = this.value.replace(/,/g, '');" class="form-control" name="user-name"
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
                                        <input type="password" required minlength="8" maxlength="64" pattern="^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[^\s]{8,}$" oninput="this.value = this.value.replace(/,/g, '');" class="form-control" name="password" id="password"
                                               placeholder="パスワードを入力してください" title="<?php echo $password_details; ?>"
                                            <?php $password = $_POST['password'] ?? '';
                                            echo "value='{$password}'"; ?>
                                        >
                                    </div>
                                </div>

                                <div class="form-area">
                                    <div class="login-form-label">
                                        確認用パスワード
                                    </div>
                                    <div class="login-form-input">
                                        <input type="password" required minlength="8" maxlength="64" pattern="^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[^\s]{8,}$" oninput="this.value = this.value.replace(/,/g, '');" class="form-control" name="re-password" id="re-password"
                                               placeholder="確認用パスワードを入力してください"
                                            <?php $re_password = $_POST['re-password'] ?? '';
                                            echo "value='{$re_password}'"; ?>
                                        >
                                    </div>
                                </div>

                                <div class="login-msg">
                                    <?php
                                    if($entry_error == True){
                                        echo "<div class='msg err_msg'>{$error_msg}</div>";
                                    }
                                    if ($entry == True) {
                                        echo "<div class='msg suc_msg'>ユーザーを登録しました。<br>承認されるまでお待ちください。</div>";
                                    }
                                    ?>
                                </div>

                                <div class="form-area">
                                    <div class="login-form-btn">
                                        <button type="submit" class="btn btn-primary date-btn-item" name="application-btn">
                                            申請する
                                        </button>
                                    </div>
                                </div>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
            <div class="right">
            </div>
        </div>
    </div>
</main>
<form action="" method="post">
    <div class="control">
        <div class="control-item">
            <div class="control-btn">
                <button type="submit" class="btn btn-primary date-btn-item" name="back-btn">
                    戻る
                </button>
            </div>
        </div>
    </div>
</form>
<footer>
    <?php require $_SERVER['DOCUMENT_ROOT'] . '/Interfaces/Views/Layouts/footer.php'; ?>
</footer>
<script>

</script>
</body>
</html>



