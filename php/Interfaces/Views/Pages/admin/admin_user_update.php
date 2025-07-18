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
$page = $_GET['page'] ?? null;

$url_param = [];
if ($screen !== null) {
    $url_param[] = "screen=$screen";
}
if ($user_id !== null) {
    $url_param[] = "user_id=$user_id";
}else {
    $_SESSION['access_error'] = 1;
    echo "<script>window.location.href = '/Interfaces/Views/Pages/access_error.php';</script>";
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
$create_date = "";
$update_date = "";
$approval_date = "";
$deleted_date = "";

// ボタン押下時の処理
if (isset($_POST['user-entry'])) {
    $entry_button_click_flg = True;

    $user_id = $_POST['user_id'] ?? '';
    $user_name = $_POST['user_name'] ?? '';
    $password = $_POST['password'] ?? '';
    $permission = $_POST['permission'] ?? '';
    $approval = $_POST['approval'] ?? '';
    $deleted = $_POST['deleted'] ?? '';

    $userinfo_api_update_result = apiCallUserUpdateInfo($user_id, $user_name, $password, $permission, $approval, $deleted);

    $status = $userinfo_api_update_result['status'];
    if ($status != "success") {

        $entry_error = True;
        $message = "ユーザー情報の更新に失敗しました";
    } else {
        $message = "ユーザー情報を更新しました";
    }
}
if (isset($_POST['leader-change'])) {
    echo "<script>window.location.href = '/Interfaces/Views/Pages/admin/admin_group_leader_change.php?{$url_param}';</script>";
}

if (isset($_POST['group-info-entry'])) {
    $entry_button_click_flg = True;

    $groups_id = $_POST['groups_id'] ?? '';
    $group_approval = $_POST['group_approval'] ?? '';

    $group_info_api_update_result=apiCallGroupInfoUpdate($groups_id,$user_id,null,$group_approval);

    $status = $group_info_api_update_result['status'];
    if ($status != "success") {

        $entry_error = True;
        $message = "所属グループ情報の更新に失敗しました";
        createLogs(LOG_TYPE_ERROR, "所属グループ情報の更新に失敗");
    } else {
        $message = "所属グループ情報を更新しました";
        createLogs(LOG_TYPE_INFO, "所属グループ情報を更新");
    }
}

if (isset($_POST['setting-entry'])) {
    $entry_button_click_flg = True;

    $user_id = $_POST['user_id'] ?? '';
    $setting1 = $_POST['setting1'] ?? '';
    $setting2 = $_POST['setting2'] ?? '';

    $results = [
        'Setting1' => $setting1,
        'Setting2' => $setting2,
    ];

    $all_success = true;
    $error_keys = [];

    foreach ($results as $setting_name => $value) {
        $result = apiCallUserUpdateSetting($user_id, $setting_name, $value);

        // エラーチェック
        if (isset($result['status']) && $result['status'] === 'error') {
            $all_success = false;
            $error_keys[] = $setting_name;
        }
    }
    if (!$all_success) {
        $error_keys_str = implode(', ', $error_keys);
        $message = "設定の更新に失敗しました。<br>エラーが発生した設定: " . $error_keys_str;
        $entry_error = true;
        createLogs(LOG_TYPE_ERROR, "ユーザー設定の更新に失敗");
    } else {
        $message = "設定が正常に更新されました。";
        createLogs(LOG_TYPE_INFO, "ユーザー設定を更新");
    }
}

$userinfo_api_refer_result = apiCallUserRefer($user_id);
$users_data = $userinfo_api_refer_result['data']['user'];
$user_setting = [];
foreach ($users_data as $user) {
    $user_name = $user['user_info']['user_name'];
    $password = $user['user_info']['password'];
    $permission = $user['user_info']['permission'];
    $approval = $user['user_info']['approval'];
    $deleted = $user['user_info']['delete'];
    $create_date = $user['user_info']['create_date'];
    $update_date = $user['user_info']['update_date'];
    $approval_date = $user['user_info']['approval_date'];
    $deleted_date = $user['user_info']['delete_date'];

    $groups_id = "-";
    $groups_name = "-";
    $group_leader = "-";
    $group_approval = "-";

    foreach ($user['group_info'] as $group) {
        $groups_id = $group['groups_id'];
        $group_leader = $group['leader'];
        $group_approval = $group['approval'];
    }

    foreach ($user['user_setting'] as $setting) {
        $user_setting[$setting['setting_key']] = $setting['setting_value'];
    }

    $setting1 = $user_setting['Setting1'] ?? null;
    $setting2 = $user_setting['Setting2'] ?? null;
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
            <?php
            //
            if ($group_file_flag OR $screen=="group") {
                echo "<div class='btn-item'><a class='link-btn' href='/Interfaces/Views/Pages/admin/admin_group_update.php?{$url_param}'>所属グループ更新</a></div>";
            }
            if ($group_file_flag OR $screen=="group_assign") {
                echo "<div class='btn-item'><a class='link-btn' href='/Interfaces/Views/Pages/admin/admin_group_assign.php?{$url_param}&page={$page}'>所属グループメンバー配属</a></div>";
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

                            <?php
                            $admin_disabled = "";
                            if ($permission == '2') {
                                $admin_disabled = "disabled";
                            }
                            ?>

                            <div class="settings-form">
                                <div class="settings-label-container">
                                    <div class="settings-label">
                                        ユーザーID
                                        <div class="disabled-comment">※入力不可</div>
                                    </div>
                                </div>
                                <div class="settings-input-container">
                                    <input disabled type="number" class="form-control" name=""
                                        <?php
                                        echo "value='{$user_id}'";
                                        ?>
                                    >
                                    <input hidden type="number" class="form-control" name="user_id"
                                        <?php
                                        echo "value='{$user_id}'";
                                        ?>
                                    >
                                </div>
                            </div>

                            <div class="settings-form">
                                <div class="settings-label-container">
                                    <div class="settings-label">
                                        ユーザー名
                                        <div class="required-comment">※必須</div>
                                    </div>
                                </div>
                                <div class="settings-input-container">
                                    <input required type="text" minlength="4" maxlength="32" type="text" class="form-control" name="user_name" placeholder="※ユーザー名を入力してください"
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
                                    <input required type="password" minlength="4" maxlength="64" class="form-control" name="password" placeholder="※パスワードを入力してください"
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
                                        <input <?php echo $admin_disabled; ?> class="form-check-input" type="radio"
                                                                              name="permission"
                                                                              id="permission_option1" value="0"
                                            <?php if ($permission == '0') echo 'checked'; ?>>
                                        <label class="form-check-label" for="permission_option1">
                                            一般
                                        </label>
                                    </div>
                                    <div class="form-check">
                                        <input <?php echo $admin_disabled; ?> class="form-check-input" type="radio"
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
                                        <input <?php echo $admin_disabled; ?> class="form-check-input" type="radio"
                                                                              name="approval"
                                                                              id="approval_option1" value="0"
                                            <?php if ($approval == '0') echo 'checked'; ?>>
                                        <label class="form-check-label" for="approval_option1">
                                            未承認
                                        </label>
                                    </div>
                                    <div class="form-check">
                                        <input <?php echo $admin_disabled; ?> class="form-check-input" type="radio"
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
                                        <input <?php echo $admin_disabled; ?> class="form-check-input" type="radio"
                                                                              name="deleted"
                                                                              id="deleted_option1" value="0"
                                            <?php if ($deleted == '0') echo 'checked'; ?>>
                                        <label class="form-check-label" for="deleted_option1">
                                            未削除
                                        </label>
                                    </div>
                                    <div class="form-check">
                                        <input <?php echo $admin_disabled; ?> class="form-check-input" type="radio"
                                                                              name="deleted"
                                                                              id="deleted_option2" value="1"
                                            <?php if ($deleted == '1') echo 'checked'; ?>>
                                        <label class="form-check-label" for="deleted_option2">
                                            削除
                                        </label>
                                    </div>
                                </div>
                            </div>

                            <div class="settings-form">
                                <div class="settings-label-container">
                                    <div class="settings-label">
                                        作成日
                                        <div class="disabled-comment">※入力不可</div>
                                    </div>
                                </div>
                                <div class="settings-input-container">
                                    <input disabled type="text" class="form-control" name=""
                                        <?php
                                        echo "value='{$create_date}'";
                                        ?>
                                    >
                                </div>
                            </div>

                            <div class="settings-form">
                                <div class="settings-label-container">
                                    <div class="settings-label">
                                        最終更新日
                                        <div class="disabled-comment">※入力不可</div>
                                    </div>
                                </div>
                                <div class="settings-input-container">
                                    <input disabled type="text" class="form-control" name=""
                                        <?php
                                        echo "value='{$update_date}'";
                                        ?>
                                    >
                                </div>
                            </div>

                            <div class="settings-form">
                                <div class="settings-label-container">
                                    <div class="settings-label">
                                        承認日
                                        <div class="disabled-comment">※入力不可</div>
                                    </div>
                                </div>
                                <div class="settings-input-container">
                                    <input disabled type="text" class="form-control" name=""
                                        <?php
                                        echo "value='{$approval_date}'";
                                        ?>
                                    >
                                </div>
                            </div>

                            <div class="settings-form">
                                <div class="settings-label-container">
                                    <div class="settings-label">
                                        削除日
                                        <div class="disabled-comment">※入力不可</div>
                                    </div>
                                </div>
                                <div class="settings-input-container">
                                    <input disabled type="text" class="form-control" name=""
                                        <?php
                                        echo "value='{$deleted_date}'";
                                        ?>
                                    >
                                </div>
                            </div>

                        </div>

                        <!-- 登録ボタン -->
                        <div class="btn-area">
                            <div class="btn-center-area">
                                <div class="btn-item">
                                    <button type="submit" class="btn btn-primary" name="user-entry">
                                        ユーザー情報更新
                                    </button>
                                </div>
                            </div>
                        </div>
                    </form>

                    <form action="" method="post">
                        <div class="settings-section">
                            <h4 class="settings-title">ユーザー設定</h4>
                            <hr>

                            <div hidden class="settings-form">
                                <div class="settings-label-container">
                                    <div class="settings-label">
                                        ユーザーID
                                    </div>
                                </div>
                                <div class="settings-input-container">
                                    <input type="number" class="form-control" name="user_id"
                                        <?php
                                        echo "value='{$user_id}'";
                                        ?>
                                    >
                                </div>
                            </div>

                            <div class="settings-form">
                                <div class="settings-label-container">
                                    <div class="settings-label">
                                        設定１
                                        <div class="optional-comment">※任意</div>
                                    </div>
                                </div>
                                <div class="settings-input-container">
                                    <input type="text" class="form-control" name="setting1"
                                        <?php
                                        echo "value='{$setting1}'";
                                        ?>
                                    >
                                </div>
                            </div>
                            <div class="settings-form">
                                <div class="settings-label-container">
                                    <div class="settings-label">
                                        設定２
                                        <div class="optional-comment">※任意</div>
                                    </div>
                                </div>
                                <div class="settings-input-container">
                                    <input type="text" class="form-control" name="setting2"
                                        <?php
                                        echo "value='{$setting2}'";
                                        ?>
                                    >
                                </div>
                            </div>

                            <!-- 登録ボタン -->
                            <div class="btn-area">
                                <div class="btn-center-area">
                                    <div class="btn-item">
                                        <button type="submit" class="btn btn-primary" name="setting-entry">
                                            ユーザー設定更新
                                        </button>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </form>

                    <form action="" method="post">
                        <div class="settings-section">
                            <h4 class="settings-title">所属グループ情報</h4>
                            <hr>

                            <div class="settings-form">
                                <div class="settings-label-container">
                                    <div class="settings-label">
                                        所属グループ名
                                    </div>
                                </div>
                                <div class="settings-input-container">
                                    <input hidden type="text" class="form-control" name="groups_id"
                                        <?php
                                        echo "value='{$groups_id}'";
                                        ?>
                                    >
                                    <?php
                                    if ($groups_id == "-") {
                                        echo "未所属";
                                    } else {
                                        echo "<a href='/Interfaces/Views/Pages/admin/admin_group_update.php?screen={$screen}&user_id={$user_id}&groups_id={$groups_id}'>$groups_id</a>";
                                    }
                                    ?>
                                </div>
                            </div>

                            <?php if ($groups_id != "-") { ?>
                                <div class="settings-form">
                                    <div class="settings-label-container">
                                        <div class="settings-label">
                                            リーダーフラグ
                                            <div class="disabled-comment">※入力不可</div>
                                        </div>
                                    </div>
                                    <div class="settings-input-container">
                                        <div class="form-check">
                                            <input disabled class="form-check-input" type="radio"
                                                   name="group_leader"
                                                   id="group_leader_option1" value="0"
                                                <?php if ($group_leader != '1') echo 'checked'; ?>>
                                            <label class="form-check-label" for="group_leader_option1">
                                                メンバー
                                            </label>
                                        </div>
                                        <div class="form-check">
                                            <input disabled class="form-check-input" type="radio"
                                                   name="group_leader"
                                                   id="group_leader_option2" value="1"
                                                <?php if ($group_leader == '1') echo 'checked'; ?>>
                                            <label class="form-check-label" for="group_leader_option2">
                                                リーダー
                                            </label>
                                        </div>
                                    </div>
                                </div>

                                <div class="settings-form">
                                    <div class="settings-label-container">
                                        <div class="settings-label">
                                            所属グループ承認フラグ
                                            <div class="required-comment">※必須</div>
                                        </div>
                                    </div>
                                    <div class="settings-input-container">
                                        <div class="form-check">
                                            <input <?php if ($group_leader== '1') echo 'disabled'; ?> class="form-check-input" type="radio"
                                                                                                      name="group_approval"
                                                                                                      id="group_approval_option1" value="0"
                                                <?php if ($group_approval != '1') echo 'checked'; ?>>
                                            <label class="form-check-label" for="group_approval_option1">
                                                未承認
                                            </label>
                                        </div>
                                        <div class="form-check">
                                            <input <?php if ($group_leader== '1') echo 'disabled'; ?> class="form-check-input" type="radio"
                                                                                                      name="group_approval"
                                                                                                      id="group_approval_option2" value="1"
                                                <?php if ($group_approval == '1') echo 'checked'; ?>>
                                            <label class="form-check-label" for="group_approval_option2">
                                                承認
                                            </label>
                                        </div>
                                    </div>
                                </div>

                                <!-- リーダー変更ボタン -->
                                <div <?php if($group_leader!=1){ echo "hidden"; } ?> class="btn-area">
                                    <div class="btn-center-area">
                                        <div class="btn-item">
                                            <button type="submit" class="btn btn-primary" name="leader-change">
                                                リーダーを変更する
                                            </button>
                                        </div>
                                    </div>
                                </div>

                                <!-- 登録ボタン -->
                                <div <?php if($group_leader==1){ echo "hidden"; } ?> class="btn-area">
                                    <div class="btn-center-area">
                                        <div class="btn-item">
                                            <button type="submit" class="btn btn-primary" name="group-info-entry">
                                                所属グループ情報更新
                                            </button>
                                        </div>
                                    </div>
                                </div>

                            <?php }?>

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



