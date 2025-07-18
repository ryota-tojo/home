<?php
if (session_status() === PHP_SESSION_NONE) {
    session_start();
}
require_once $_SERVER['DOCUMENT_ROOT'] . '/config/config.php';
require_once $_SERVER['DOCUMENT_ROOT'] . '/config/log_config.php';
require_once $_SERVER['DOCUMENT_ROOT'] . '/Application/Services/ApiService.php';
require_once $_SERVER['DOCUMENT_ROOT'] . '/Interfaces/Views/Partials/requireApi.php';
require_once $_SERVER['DOCUMENT_ROOT'] . '/Interfaces/Views/Partials/member/get_max_member_no.php';
require_once $_SERVER['DOCUMENT_ROOT'] . '/Interfaces/Views/Partials/systems/logs/create_logs.php';
require_once $_SERVER['DOCUMENT_ROOT'] . '/Interfaces/Views/Partials/systems/screen/get_screen.php';

$screen_items = getScreen(basename(__FILE__));
$screen_title = $screen_items['name'];
$screen_remarks = $screen_items['remarks'];

// 管理者判定
$admin_flag = 0;

// 所属グループ判定
if ($_SESSION['user_groups_id'] == null or $_SESSION['user_group_approval_flg'] == 0) {
    echo "<script>window.location.href = '/Interfaces/Views/Partials/home.php';</script>";
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
    <link rel="stylesheet" href="/Interfaces/Assets/CSS/search_form.css">
    <link rel="stylesheet" href="/Interfaces/Assets/CSS/message.css">
    <link rel="stylesheet" href="/Interfaces/Assets/CSS/table_form.css">
    <link rel="stylesheet" href="/Interfaces/Assets/CSS/button_form.css">
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
// なし

if (isset($_POST['entry'])) {
    $entry_button_click_flg = True;

    $member_name = $_POST['member-name'];

    $member_max_no = getMaxMemberNo($_SESSION['user_groups_id']);
    $member_new_no = $member_max_no + 1;

    $result = apiCallMemberCreate($_SESSION['user_groups_id'],$member_new_no,$member_name);

    if($result['status']!='error'){
        $message = "メンバーを登録しました";
        createLogs(LOG_TYPE_INFO, "メンバー登録 - メンバー登録");
    } else {
        $message = "メンバーの登録に失敗しました";
        $entry_error = true;
        createLogs(LOG_TYPE_ERROR, "メンバー登録 - メンバー登録失敗");
    }
}

$member_api_refer_result = apiCallMemberRefer(null, $_SESSION['user_groups_id']);
if ($member_api_refer_result['status'] != "error") {
    $categories_data = $member_api_refer_result['data']['member_list'];
}

?>

<main>
    <div class="title-area">
        <h2 class="title"><?php echo $screen_title; ?></h2>
    </div>
    <div class="summary-area">
        <div class="summary">
            <?php echo $screen_remarks; ?>
        </div>
    </div>

    <div class="btn-area">
        <div class="btn-center-area">
            <div class='btn-item'><a class='link-btn'
                                     href='/Interfaces/Views/Pages/user/group/setting/member/user_group_member_list.php'>メンバー一覧</a>
            </div>
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
                <form action="" method="post">
                    <div class="form-area">


                        <h6 class="form-title">入力フォーム</h6>
                        <hr>

                        <div class="pc-form">
                            <!-- メンバー名 -->
                            <div class="form-item">
                                <div class="form-item-label">
                                    <label class="item-label">メンバー名</label>
                                </div>
                                <div class="input-group form-item">
                                    <input type="text" required minlength="1" maxlength="64" oninput="this.value = this.value.replace(/,/g, '');" class="form-control" name="member-name"
                                           placeholder="メンバー名を入力してください"
                                        <?php $member_name = $_POST['member-name'] ?? '';
                                        if($entry_error == True){echo "value='{$member_name}'";}
                                         ?>
                                    >
                                </div>
                            </div>
                        </div>
                    </div>

                    <!-- 登録ボタン -->
                    <div class="btn-area">
                        <div class="btn-center-area">
                            <div class="btn-item">
                                <button type="submit" class="btn btn-primary" name="entry">登録</button>
                            </div>
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



