<?php
if (session_status() === PHP_SESSION_NONE) {
    session_start();
}
require $_SERVER['DOCUMENT_ROOT'] . '/config/config.php';
require $_SERVER['DOCUMENT_ROOT'] . '/Application/Services/ApiService.php';
require $_SERVER['DOCUMENT_ROOT'] . '/Interfaces/Views/Partials/requireApi.php';


$screen_title = "お知らせ登録";

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

// マスター設定
$master_setting_api_refer_result = apiCallMasterSettingRefer();
$master_settings = [];
foreach ($master_setting_api_refer_result['data']['setting_list'] as $setting) {
    $master_settings[$setting['setting_key']] = $setting['setting_value'];
}
$notice_title_default = $master_settings['admin_notice_default_title'] ?? null;
$notice_content_default = $master_settings['admin_notice_default_content'] ?? null;


// ボタン押下時の処理
if (isset($_POST['entry'])) {
    $entry_button_click_flg = True;

    $notice_title = $_POST['title'] ?? '';
    $notice_content = $_POST['content'] ?? '';

    $notice_create_api_result = apiCallNoticeCreate($notice_title,$notice_content);
    $status = $notice_create_api_result['status'];
    if($status != "success"){

        $entry_error = True;
        $notice_title_default=$notice_title;
        $notice_content_default=$notice_content;
        $message = "お知らせの登録に失敗しました";
    }else{
        $notice_title = $notice_title_default;
        $notice_content = $notice_content_default;
        $message = "お知らせを登録しました";
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

<main>
    <div class="title-area">
        <h2 class="title"><?php echo $screen_title; ?></h2>
    </div>
    <div class="summary-area">
        <div class="summary">
            新規のお知らせを登録します。<br>
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

                        <div class="pc-form">
                            <!-- タイトル -->
                            <div class="form-item title-item">
                                <div class="form-item-label">
                                    <label class="item-label">タイトル</label>
                                </div>
                                <div class="input-group form-item">
                                    <input required type="text" class="form-control" name="title"
                                        <?php $init_notice_title = $notice_title ?? $notice_title_default;
                                        echo "value='{$init_notice_title}'"; ?>
                                    >
                                </div>
                            </div>
                        </div>

                        <div class="pc-form">
                            <!-- 内容 -->
                            <div class="form-item content-item">
                                <div class="form-item-label">
                                    <label class="item-label">内容</label>
                                </div>
                                <div class="input-group form-item">
                                    <textarea required class="form-control" name="content" rows="10"><?php $init_notice_content = $notice_content ?? $notice_content_default; echo "{$init_notice_content}"; ?></textarea>
                                </div>
                            </div>
                        </div>
                    </div>

                    <!-- 登録ボタン -->
                    <div class="form-item">
                        <div class="submit-area">
                            <button type="submit" class="btn btn-primary" name="entry">登録</button>
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
<!-- bootstrap-datepickerのjavascriptコード -->
<script>
    $('#sample1').datepicker();
</script>
</body>
</html>



