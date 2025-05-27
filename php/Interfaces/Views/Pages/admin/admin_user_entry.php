<?php
if (session_status() === PHP_SESSION_NONE) {
    session_start();
}
require $_SERVER['DOCUMENT_ROOT'] . '/config/config.php';
require $_SERVER['DOCUMENT_ROOT'] . '/Application/Services/api_service.php';
require $_SERVER['DOCUMENT_ROOT'] . '/Interfaces/Views/Partials/requireApi.php';
require_once $_SERVER['DOCUMENT_ROOT'] . '/Interfaces/Views/Partials/user/user_create.php';

$screen_title = "ユーザー登録";

// 管理者判定
$admin_flag = 0;
if ($_SESSION['user_permission'] == 2) {
    $admin_flag = 1;
}
if ($admin_flag == 0) {
    $_SESSION['access_error'] = 1;
    echo "<script>window.location.href = '/Interfaces/Views/Pages/access_error.php';</script>";
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
    <link rel="stylesheet" href="/Interfaces/Assets/CSS/setting_form.css">
    <link rel="stylesheet" href="/Interfaces/Assets/CSS/message.css">
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

$filename = basename(__FILE__);
$user_file_flag   = str_contains($filename, 'user');
$group_file_flag  = str_contains($filename, 'group');
$update_file_flag = str_contains($filename, 'update');
$screen = $_GET['screen'] ?? null;
$user_id = $_GET['user_id'] ?? null;
$groups_id = $_GET['groups_id'] ?? null;

$url_param = [];
if ($screen !== null) {
    $url_param[] = "screen=$screen";
}
if ($user_id !== null) {
    $url_param[] = "user_id=$user_id";
}
if ($groups_id !== null) {
    $url_param[] = "groups_id=$groups_id";
}
$url_param = implode('&', $url_param);

// 変数初期化
$entry_button_click_flg = False;
$message = "";
$entry_error = False;

$user_name = "";
$password = "";
$permission = "";
$approval = "";
$deleted = "";

// ボタン押下時の処理
if (isset($_POST['entry'])) {
    $entry_button_click_flg = True;

    $user_name = $_POST['user_name'] ?? '';
    $password = $_POST['password'] ?? '';
    $permission = $_POST['permission'] ?? '';
    $approval = $_POST['approval'] ?? '';
    $deleted = $_POST['deleted'] ?? '';

    $result = userEntry($user_name, $password, $permission, $approval, $deleted);
    $data = json_decode($result, true);
    $status = $data['status'];

    if ($status != "success") {
        $entry_error = True;
        $error_message = $data['message'];
        $message = "$error_message";
    } else {
        $message = "ユーザー情報を登録しました";
    }
}

?>

<main>
    <div class="title-area">
        <h2 class="title"><?php echo $screen_title; ?></h2>
    </div>
    <div class="summary-area">
        <div class="summary">
            ユーザーを登録します
        </div>
    </div>

    <div class="btn-area">
        <div class="btn-center-area">
            <?php
            //
            if ($user_file_flag or $screen == "user") {
                echo "<div class='btn-item'><a class='link-btn' href='/Interfaces/Views/Pages/admin/admin_user_control.php?$url_param'>ユーザー管理</a></div>";
            }
            ?>
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
                            <h4 class="settings-title">ユーザー情報</h4>
                            <hr>

                            <div class="settings-form">
                                <div class="settings-label-container">
                                    <div class="settings-label">
                                        ユーザー名
                                        <div class="required-comment">※必須</div>
                                    </div>
                                </div>
                                <div class="settings-input-container">
                                    <input required type="text" minlength="4" maxlength="32" class="form-control" name="user_name" placeholder="※ユーザー名を入力してください"
                                        <?php
                                        echo "value='{$user_name}'";
                                        ?>
                                    >
                                </div>
                            </div>

                            <div class="settings-form">
                                <div class="settings-label-container">
                                    <div class="settings-label">
                                        パスワード
                                        <div class="required-comment">※必須</div>
                                    </div>
                                </div>
                                <div class="settings-input-container">
                                    <input required type="password"  minlength="8" maxlength="64" class="form-control" name="password" placeholder="※パスワードを入力してください"
                                        <?php
                                        echo "value='{$password}'";
                                        ?>
                                    >
                                </div>
                            </div>

                            <div class="settings-form">
                                <div class="settings-label-container">
                                    <div class="settings-label">
                                        権限
                                        <div class="required-comment">※必須</div>
                                    </div>
                                </div>
                                <div class="settings-input-container">
                                    <div class="form-check">
                                        <input checked class="form-check-input" type="radio"
                                                                              name="permission"
                                                                              id="permission_option1" value="0"
                                            <?php if ($permission == '0') echo 'checked'; ?>>
                                        <label class="form-check-label" for="permission_option1">
                                            一般
                                        </label>
                                    </div>
                                    <div class="form-check">
                                        <input class="form-check-input" type="radio"
                                                                              name="permission"
                                                                              id="permission_option2" value="1"
                                            <?php if ($permission == '1') echo 'checked'; ?>>
                                        <label class="form-check-label" for="permission_option2">
                                            一般管理者
                                        </label>
                                    </div>
                                    <div class="form-check">
                                        <input disabled class="form-check-input" type="radio" name="permission"
                                               id="permission_option3" value="1"
                                            <?php if ($permission == '2') echo 'checked'; ?>>
                                        <label class="form-check-label" for="permission_option3">
                                            管理者
                                        </label>
                                    </div>
                                </div>
                            </div>

                            <div class="settings-form">
                                <div class="settings-label-container">
                                    <div class="settings-label">
                                        承認
                                        <div class="required-comment">※必須</div>
                                    </div>
                                </div>
                                <div class="settings-input-container">
                                    <div class="form-check">
                                        <input class="form-check-input" type="radio"
                                                                              name="approval"
                                                                              id="approval_option1" value="0"
                                            <?php if ($approval == '0') echo 'checked'; ?>>
                                        <label class="form-check-label" for="approval_option1">
                                            未承認
                                        </label>
                                    </div>
                                    <div class="form-check">
                                        <input checked class="form-check-input" type="radio"
                                                                              name="approval"
                                                                              id="approval_option2" value="1"
                                            <?php if ($approval == '1') echo 'checked'; ?>>
                                        <label class="form-check-label" for="approval_option2">
                                            承認
                                        </label>
                                    </div>
                                </div>
                            </div>

                            <div class="settings-form">
                                <div class="settings-label-container">
                                    <div class="settings-label">
                                        削除
                                        <div class="required-comment">※必須</div>
                                    </div>
                                </div>
                                <div class="settings-input-container">
                                    <div class="form-check">
                                        <input checked class="form-check-input" type="radio"
                                                                              name="deleted"
                                                                              id="deleted_option1" value="0"
                                            <?php if ($deleted == '0') echo 'checked'; ?>>
                                        <label class="form-check-label" for="deleted_option1">
                                            未削除
                                        </label>
                                    </div>
                                    <div class="form-check">
                                        <input class="form-check-input" type="radio"
                                                                              name="deleted"
                                                                              id="deleted_option2" value="1"
                                            <?php if ($deleted == '1') echo 'checked'; ?>>
                                        <label class="form-check-label" for="deleted_option2">
                                            削除
                                        </label>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <!-- 登録ボタン -->
                        <div class="btn-area">
                            <div class="btn-center-area">
                                <div class="btn-item">
                                    <button type="submit" class="btn btn-primary" name="entry">
                                        ユーザー登録
                                    </button>
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

<?php
require $_SERVER['DOCUMENT_ROOT'] . '/Interfaces/Assets/JS/basic_js.php';
?>

</body>
</html>



