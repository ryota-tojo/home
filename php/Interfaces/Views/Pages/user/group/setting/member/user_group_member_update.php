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

if (!isset($_GET['member_id'])) {
    echo "<script>window.location.href = '/Interfaces/Views/Pages/access_error.php';</script>";
}
$member_id = $_GET['member_id'];


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

    $post_member_id = $_POST['member-id'];
    $post_new_member_name = $_POST['new-member-name'];

    $result = apiCallMemberUpdate($post_member_id,null,$post_new_member_name);

    if($result['status']!='error'){
        $message = "メンバー情報を変更しました";
        createLogs(LOG_TYPE_INFO, "メンバー変更 - メンバー変更");
    } else {
        $message = "メンバー情報の変更に失敗しました";
        $entry_error = true;
        createLogs(LOG_TYPE_ERROR, "メンバー変更 - メンバー変更失敗");
    }
}

$member_name="";
$member_api_refer_result = apiCallMemberRefer($member_id, $_SESSION['user_groups_id']);
if ($member_api_refer_result['status'] != "error") {
    $categories_data = $member_api_refer_result['data']['member_list'];
}
foreach ($categories_data as $member_data){
    $member_name = $member_data['member_name'];
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
                            <!-- メンバーID -->
                            <div class="form-item">
                                <div class="form-item-label">
                                    <label class="item-label">メンバーID</label>
                                </div>
                                <div class="input-group form-item"">
                                    <input type="text" style="display: none" minlength="1" maxlength="64" oninput="this.value = this.value.replace(/,/g, '');" class="form-control" name="member-id"
                                           placeholder="メンバー名を入力してください"
                                        <?php echo "value='{$member_id}'"; ?>
                                    >
                                    <input type="text" disabled minlength="1" maxlength="64" oninput="this.value = this.value.replace(/,/g, '');" class="form-control" name=""
                                           placeholder="メンバー名を入力してください"
                                        <?php echo "value='{$member_id}'"; ?>
                                    >
                                </div>
                            </div>

                            <!-- メンバー名 -->
                            <div class="form-item">
                                <div class="form-item-label">
                                    <label class="item-label">メンバー名</label>
                                </div>
                                <div class="input-group form-item">
                                    <input disabled type="text" required minlength="1" maxlength="64" oninput="this.value = this.value.replace(/,/g, '');" class="form-control" name="member-name"
                                           placeholder="メンバー名を入力してください"
                                        <?php echo "value='{$member_name}'"; ?>
                                    >
                                </div>
                            </div>

                            <!-- 変更後メンバー名 -->
                            <div class="form-item">
                                <div class="form-item-label">
                                    <label class="item-label">変更後メンバー名</label>
                                </div>
                                <div class="input-group form-item">
                                    <input type="text" required minlength="1" maxlength="64" oninput="this.value = this.value.replace(/,/g, '');" class="form-control" name="new-member-name"
                                           placeholder="変更後のメンバー名を入力してください"
                                        <?php

                                        if($entry_error == True){
                                            if(isset($_POST['new-member-name'])){
                                                echo "value={$_POST['new-member-name']}";
                                            }
                                        } ?>
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



