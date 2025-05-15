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
                XXXXXX<br>
                XXXXXX
            </div>
        </div>
        <div class="contents">
            <div class="content-row">
                <div class="left">
                </div>
                <div class="center">
                    <?php
                    $a = apiCallUserRefer();

                    foreach ($a['data']['user'] as $user){
                        $userInfo = $user['user_info'];
                        $userSettings = $user['user_setting'];
                        $groupInfo = $user['group_info'];

                        echo "ユーザー名: " . $userInfo['user_name'] . PHP_EOL;
                        echo "パスワード: " . $userInfo['password'] . PHP_EOL;

                        foreach ($userSettings as $setting) {
                            echo $setting['setting_key'] . " = " . $setting['setting_value'] . PHP_EOL;
                        }
                        echo "<br>";

                    }
                    ?>

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
