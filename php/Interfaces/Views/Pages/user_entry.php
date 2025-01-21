<?php

use Application\Services\UserManagementService;
use Infrastructure\Persistence\Database;

require $_SERVER['DOCUMENT_ROOT'] . '/config/config.php';
require $_SERVER['DOCUMENT_ROOT'] . '/Infrastructure/Persistence/Database.php';
require $_SERVER['DOCUMENT_ROOT'] . '/Application/Services/UserManagementService.php';
require $_SERVER['DOCUMENT_ROOT'] . '/Domain/Models/UserRepository.php';

session_start();

$screen_title = "ユーザー新規登録申請";

//DB接続
$db_config = new Database(DB_HOST, DB_PORT, DB_USER, DB_PASSWORD, DB_NAME);
$db_config->connect();

// 変数初期化
$entry = False;
$entry_error = False;
$group_id_invalid_err = False;
$group_id_exist_err = False;
$user_name_invalid_err = False;
$user_name_duplication_err = False;
$password_mismatch_err = False;
$password_details = "※パスワードは以下の条件を満たす必要があります
                    ・8文字以上
                    ・小文字の英字（a～z）を1文字以上含む
                    ・大文字の英字（A～Z）を1文字以上含む
                    ・数字（0～9）を1文字以上含む
                    ・特殊文字（@, $, !, %, *, ?, &）を1文字以上含む
                    ・空白文字（スペース、タブ、改行）は含めないでください";

if (isset($_POST['back-btn'])) {
    echo "<script>window.location.href = 'login.php';</script>";
}
if (isset($_POST['application-btn'])) {

    $groups_id = $_POST['groups-id'] ?? '';
    $user_name = $_POST['user-name'] ?? '';
    $password = $_POST['password'] ?? '';
    $re_password = $_POST['re-password'] ?? '';

    $user_management_repository = new UserManagementService($db_config, $groups_id, $user_name, $password, $re_password);

    $certification_result = $user_management_repository->certification();
    if($certification_result == "unavailable group id"){
        $group_id_invalid_err = True;
    }else if($certification_result == "groupid not found"){
        $group_id_exist_err = True;
    }else if($certification_result == "unavailable user name"){
        $user_name_invalid_err = True;
    }else if($certification_result == "user name is duplication"){
        $user_name_duplication_err = True;
    }else if($certification_result == "password mismatch"){
        $password_mismatch_err = True;
    }else{

        $user_id = $user_management_repository->creation();

        echo $user_id;

        $entry = True;
    }
    if($group_id_invalid_err or $group_id_exist_err or $user_name_invalid_err or $user_name_duplication_err or $password_mismatch_err){
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
                                        所属グループ名ID
                                    </div>
                                    <div class="login-form-input">
                                        <input type="text" required class="form-control" name="groups-id"
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
                                        <input type="password" required minlength="8" maxlength="64" pattern="^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[^\s]{8,}$" oninput="this.value = this.value.replace(/,/g, '');" class="form-control" name="password"
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
                                        <input type="password" required minlength="8" maxlength="64" pattern="^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[^\s]{8,}$" oninput="this.value = this.value.replace(/,/g, '');" class="form-control" name="re-password"
                                               placeholder="確認用パスワードを入力してください"
                                            <?php $re_password = $_POST['re-password'] ?? '';
                                            echo "value='{$re_password}'"; ?>
                                        >
                                    </div>
                                </div>

                                <div class="login-msg">
                                    <?php
                                    if($entry_error == True){

                                        if($group_id_invalid_err){
                                            echo "<div class='msg err_msg'>入力した所属グループIDは使用できません。</div>";
                                        }
                                        if($group_id_exist_err){
                                            echo "<div class='msg err_msg'>入力した所属グループIDは存在しません。</div>";
                                        }
                                        if($user_name_invalid_err){
                                            echo "<div class='msg err_msg'>入力したユーザー名は使用できません。</div>";
                                        }
                                        if($user_name_duplication_err){
                                            echo "<div class='msg err_msg'>入力したユーザー名は使用できません。</div>";
                                        }
                                        if($password_mismatch_err){
                                            echo "<div class='msg err_msg'>パスワードが一致しません。</div>";
                                        }
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
<!-- bootstrap-datepickerのjavascriptコード -->
<script>
    $('#sample1').datepicker();
</script>
</body>
</html>



