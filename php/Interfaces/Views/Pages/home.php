<?php
if (session_status() === PHP_SESSION_NONE) {
    session_start();
}
require_once $_SERVER['DOCUMENT_ROOT'] . '/config/config.php';
require_once $_SERVER['DOCUMENT_ROOT'] . '/config/log_config.php';
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
    <link rel="stylesheet" href="/Interfaces/Assets/CSS/notice_form.css">
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

<?php
ob_flush();
flush();

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
$admin_notice_view = $master_settings['admin_notice_view'] ?? null;
if (!isset($_SESSION['notice_view'])) {
    $_SESSION['notice_view'] = $admin_notice_view;
}

// お知らせ最大数
$notice_api_count_result = apiCallNoticeCount();
$notice_count = $notice_api_count_result['data']['recode_count'];

// ボタン押下時の処理
if (isset($_POST['update'])) {
    echo "<script>window.location.href = '/Interfaces/Views/Pages/admin/admin_notice_update.php?id={$_POST['update']}';</script>";
}
if (isset($_POST['next'])) {
    if ($_SESSION['notice_view'] < $notice_count) {
        $_SESSION['notice_view'] = $_SESSION['notice_view'] + $admin_notice_view;
    }
}
if (isset($_POST['reset'])) {
    $_SESSION['notice_view'] = $admin_notice_view;
}

$notice_api_refer_result = apiCallNoticeRefer(null, 0, $_SESSION['notice_view']);


?>

<main>
    <div class="title-area">
        <h2 class="title"><?php echo $screen_title; ?></h2>
    </div>
    <div class="summary-area">
        <div class="summary">
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
                <?php foreach ($notice_api_refer_result['data']['notice_list'] as $index => $notice):
                    $cnt = $index + 1;
                    $isHidden = $cnt > $_SESSION['notice_view'];
                    ?>
                    <form
                            action=""
                            method="post"
                            id="notice-form-<?php echo $cnt; ?>"
                        <?php if ($cnt > $_SESSION['notice_view']) echo 'style="display:none;"'; ?>
                    >
                        <div class="form-area">
                            <div class='notice-header'>
                                <div class='notice-no'>
                                    <h6 class="form-title">
                                        No. <?php echo htmlspecialchars($notice['notice_id']); ?></h6>
                                </div>
                                <div class='notice-date'>
                                    <div>最終更新日時：<?php echo htmlspecialchars($notice['update_datetime']); ?></div>
                                </div>
                            </div>
                            <div class='notice-title'>
                                <h3><?php echo nl2br(htmlspecialchars($notice['title'])); ?></h3>
                            </div>
                            <hr>
                            <div class='notice-content'>
                                <?php echo nl2br(htmlspecialchars($notice['content'])); ?>
                            </div>
                            <?php if ($admin_flag == 1) { ?>
                                <div class='notice-update'>
                                    <button type='submit' class='btn btn-primary update-btn' name='update'
                                            value="<?php echo nl2br(htmlspecialchars($notice['notice_id'])); ?>">更新
                                    </button>
                                </div>
                            <?php } ?>
                        </div>
                    </form>
                <?php endforeach; ?>

                <form action="" method="post">
                    <!-- 次のX件ボタン -->
                    <div class="form-item">
                        <div class="submit-area">
                            <?php
                            $next_view = 0;
                            if (($notice_count - $_SESSION['notice_view']) >= $admin_notice_view) {
                                $next_view = $admin_notice_view;
                            } else {
                                $next_view = $notice_count - $_SESSION['notice_view'];
                            }

                            if ($next_view < 0) {
                            } else {
                                echo "<button type='submit' class='btn btn-primary next-btn' name='next' id='next'>次の{$next_view}件を表示する</button>";
                            }
                            if($_SESSION['notice_view']>$admin_notice_view){
                                echo "<button type='submit' class='btn btn-warning reset-btn' name='reset' id='reset'>表示をリセット</button>";
                            }
                            ?>
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



