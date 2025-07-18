<?php

require $_SERVER['DOCUMENT_ROOT'] . '/config/config.php';
require $_SERVER['DOCUMENT_ROOT'] . '/Infrastructure/Persistence/Database.php';
require $_SERVER['DOCUMENT_ROOT'] . '/Domain/Models/GroupListRepository.php';
require $_SERVER['DOCUMENT_ROOT'] . '/Domain/Models/GroupSettingRepository.php';
require $_SERVER['DOCUMENT_ROOT'] . '/Domain/Services/GroupRepositoryCollection.php';
require $_SERVER['DOCUMENT_ROOT'] . '/Application/Services/GroupManagementService.php';
require $_SERVER['DOCUMENT_ROOT'] . '/Application/Services/ApiService.php';

use Application\Services\GroupManagementService;
use Domain\Models\GroupListRepository;
use Domain\Models\GroupSettingRepository;
use Domain\Services\GroupRepositoryCollection;
use Infrastructure\Persistence\Database;
use Application\Services\ApiService;

session_start();

//画面名
$screen_title = "所属グループ新規登録";

//DB接続
$db_config = new Database(DB_HOST, DB_PORT, DB_USER, DB_PASSWORD, DB_NAME);
$db_config->connect();

// 変数初期化
$entry = False;
$entry_error = False;
$group_id_invalid_err = False;
$group_id_duplication_err = False;

// ボタン押下時の処理
if (isset($_POST['back-btn'])) {
    echo "<script>window.location.href = 'login.php';</script>";
}
if (isset($_POST['entry-btn'])) {

    $groups_name = $_POST['groups-name'] ?? '';
    $groups_id = $_POST['groups-id'] ?? '';

    $groupListRepository = new GroupListRepository($db_config, $groups_id, $groups_name);
    $groupSettingRepository = new GroupSettingRepository($db_config, $groups_id);
    $groupRepositoryCollection = new GroupRepositoryCollection($groupListRepository, $groupSettingRepository);
    $groupManagementService = new GroupManagementService($groupRepositoryCollection);
    $entry_group_result = $groupManagementService->entryGroup($groups_id);

    if ($entry_group_result == "unavailable group id") {
        $group_id_invalid_err = True;
    } else if ($entry_group_result == "group id is duplication") {
        $group_id_duplication_err = True;
    } else {
        $entry = True;
    }
    if ($group_id_invalid_err or $group_id_duplication_err) {
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
                                        所属グループ表示名
                                    </div>
                                    <div class="login-form-input">
                                        <input type="text" required minlength="4" maxlength="32"
                                               oninput="this.value = this.value.replace(/,/g, '');" class="form-control"
                                               name="groups-name"
                                               placeholder="所属グループ表示名を入力してください"
                                            <?php $groups_name = $_POST['groups-name'] ?? '';
                                            echo "value='{$groups_name}'"; ?>
                                        >
                                    </div>
                                </div>

                                <div class="form-area">
                                    <div class="login-form-label">
                                        所属グループID
                                        <div class="sub-label">
                                            <div class="sub-label-text">
                                                半角英数字 + _ (アンダースコア)
                                            </div>
                                        </div>
                                    </div>
                                    <div class="login-form-input">
                                        <input type="text" required minlength="8" maxlength="64"
                                               pattern="^[a-zA-Z0-9_]+$" class="form-control" name="groups-id"
                                               placeholder="所属グループIDを入力してください">
                                    </div>
                                </div>

                                <div class="login-msg">
                                    <?php
                                    if ($entry_error == True) {
                                        if ($group_id_invalid_err == True) {
                                            echo "<div class='msg err_msg'>入力した所属グループIDは使用できません。</div>";
                                        }
                                        if ($group_id_duplication_err == True) {
                                            echo "<div class='msg err_msg'>入力した所属グループIDは既に使用されています。</div>";
                                        }
                                    }
                                    if ($entry == True) {
                                        echo "<div class='msg suc_msg'>所属グループを登録しました。</div>";
                                    }
                                    ?>
                                </div>

                                <div class="form-area">
                                    <div class="login-form-btn">
                                        <button type="submit" class="btn btn-primary date-btn-item" name="entry-btn">
                                            新規登録
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



