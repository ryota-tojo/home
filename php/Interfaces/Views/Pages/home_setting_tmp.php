<?php
if (session_status() === PHP_SESSION_NONE) {
    session_start();
}
require $_SERVER['DOCUMENT_ROOT'] . '/config/config.php';
require $_SERVER['DOCUMENT_ROOT'] . '/Application/Services/ApiService.php';
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

// 変数初期化
$entry_button_click_flg = False;
$message = "";
$entry_error = False;
$master_setting_001 = "";
$master_setting_002 = "";
$master_setting_003 = "";

// ボタン押下時の処理
if (isset($_POST['entry'])) {
    $entry_button_click_flg = True;


    $status = "success";
    if ($status != "success") {

        $entry_error = True;
        $message = "設定の更新に失敗しました";
    } else {
        $message = "設定を更新しました";
    }
}

// マスター設定
if ($entry_error == False) {
    $master_setting_api_result = apiCallMasterSettingRefer();
    foreach ($master_setting_api_result['data']['setting_list'] as $setting) {
        if ($setting['setting_key'] == 'XXXXXXXXXX') {
            $master_setting_001 = $setting['setting_value'];
        }
        if ($setting['setting_key'] == 'XXXXXXXXXX') {
            $master_setting_002 = $setting['setting_value'];
        }
        if ($setting['setting_key'] == 'XXXXXXXXXX') {
            $master_setting_003 = $setting['setting_value'];
        }
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
    <link rel="stylesheet" href="/Interfaces/Assets/CSS/home.css">
    <link rel="stylesheet" href="/Interfaces/Assets/CSS/setting_form.css">
    <link rel="stylesheet" href="/Interfaces/Assets/CSS/message.css">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha3/dist/css/bootstrap.min.css" rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha3/dist/js/bootstrap.bundle.min.js"></script>
</head>

<body class="<?php if($admin_flag == 1){echo 'admin-body';}else{echo 'body';}?>">
<header>
    <?php require $_SERVER['DOCUMENT_ROOT'] . '/Interfaces/Views/Partials/nav.php'; ?>
</header>


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

                        <div class="settings-section">
                            <h4 class="settings-title">XXX設定</h4>
                            <hr>

                            <div class="settings-form">
                                <div class="settings-label-container">
                                    <div class="settings-label">
                                        setting1
                                    </div>
                                </div>
                                <div class="settings-input-container">
                                    <div class="settings-input">
                                        <input type="text" class="form-control" name="setting1"
                                            <?php $master_setting_001 = $_POST['setting1'] ?? '';
                                            if($entry_error == True){ echo "value='{$master_setting_001}'";} ?>
                                        >
                                    </div>
                                </div>
                            </div>
                            <div class="settings-form">
                                <div class="settings-label-container">
                                    <div class="settings-label">
                                        setting2
                                    </div>
                                </div>
                                <div class="settings-input-container">
                                    <div class="settings-input">
                                        <input type="text" class="form-control" name="setting2"
                                            <?php $master_setting_002 = $_POST['setting2'] ?? '';
                                            if($entry_error == True){ echo "value='{$master_setting_002}'";} ?>
                                        >
                                    </div>
                                </div>
                            </div>
                            <div class="settings-form">
                                <div class="settings-label-container">
                                    <div class="settings-label">
                                        setting3
                                    </div>
                                </div>
                                <div class="settings-input-container">
                                    <input type="text" class="form-control" name="setting3"
                                        <?php $master_setting_003 = $_POST['setting3'] ?? '';
                                        if($entry_error == True){ echo "value='{$master_setting_003}'";} ?>
                                    >
                                </div>
                            </div>
                        </div>


                        <!-- 登録ボタン -->
                        <div class="settings-section">
                            <div class="settings-form">
                                <div class="settings-btn-container">
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
<!-- bootstrap-datepickerのjavascriptコード -->
<script>
    $('#sample1').datepicker();
</script>
</body>
</html>



