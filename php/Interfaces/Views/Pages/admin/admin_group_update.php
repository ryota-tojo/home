<?php
if (session_status() === PHP_SESSION_NONE) {
    session_start();
}
require $_SERVER['DOCUMENT_ROOT'] . '/config/config.php';
require $_SERVER['DOCUMENT_ROOT'] . '/Application/Services/ApiService.php';
require $_SERVER['DOCUMENT_ROOT'] . '/Interfaces/Views/Partials/requireApi.php';


$screen_title = "所属グループ更新";

// 管理者判定
$admin_flag = 0;
if ($_SESSION['user_permission'] == 2) {
    $admin_flag = 1;
}
if ($admin_flag == 0) {
    $_SESSION['access_error'] = 1;
    echo "<script>window.location.href = '/Interfaces/Views/Pages/access_error.php';</script>";
}

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
}else{
    $_SESSION['access_error'] = 1;
    echo "<script>window.location.href = '/Interfaces/Views/Pages/access_error.php';</script>";

}
$url_param = implode('&', $url_param);

// 変数初期化
$entry_button_click_flg = False;
$message = "";
$entry_error = False;

// ボタン押下時の処理
if (isset($_POST['group_entry'])) {
    $entry_button_click_flg = True;

    $groups_id = $_POST['groups_id'] ?? '';
    $group_name = $_POST['group_name'] ?? '';
    $group_password = $_POST['group_password'] ?? '';

    $group_api_update_result = apiCallGroupUpdateList($groups_id, $group_name, $group_password);
    $status = $group_api_update_result['status'];

    $status = "success";
    if ($status != "success") {

        $entry_error = True;
        $message = "所属グループの更新に失敗しました";
    } else {
        $message = "所属グループを更新しました";
    }
}

if (isset($_POST['setting_entry'])) {
    $entry_button_click_flg = True;

    $groups_id = $_POST['groups_id'] ?? '';
    $group_setting_display_year = $_POST['display_year'] ?? '';
    $group_setting_graph_type = $_POST['graph_type'] ?? '';
    $group_setting_notification_send_flg = $_POST['notification_send_flg'] ?? '';
    $group_setting_notification_url = $_POST['notification_url'] ?? '';
    $group_setting_notification_token = $_POST['notification_token'] ?? '';

    $results = [
        'display_year' => $group_setting_display_year,
        'graph_type' => $group_setting_graph_type,
        'notification_send_flg' => $group_setting_notification_send_flg,
        'notification_url' => $group_setting_notification_url,
        'notification_token' => $group_setting_notification_token,
    ];

    $all_success = true;
    $error_keys = [];

    foreach ($results as $setting_name => $value) {
        $result = apiCallGroupUpdateSetting($groups_id, $setting_name, $value);

        // エラーチェック
        if (isset($result['status']) && $result['status'] === 'error') {
            $all_success = false;
            $error_keys[] = $setting_name;
        }
    }

    if (!$all_success) {
        $error_keys_str = implode(', ', $error_keys);
        $message = "所属グループ設定の更新に失敗しました。<br>エラーが発生した設定: " . $error_keys_str;
        $entry_error = true;
    } else {
        $message = "所属グループ設定が正常に更新されました。";
    }
}

$group_refer_api_result = apiCallGroupRefer($groups_id);
if ($group_refer_api_result['status'] == "error") {
    $_SESSION['access_error'] = 1;
    echo "<script>window.location.href = '/Interfaces/Views/Pages/access_error.php';</script>";
}
$group_info_refer_api_result = apiCallGroupInfoRefer($groups_id);
if ($group_info_refer_api_result['status'] == "error") {
    $_SESSION['access_error'] = 1;
    echo "<script>window.location.href = '/Interfaces/Views/Pages/access_error.php';</script>";
}

$group_name = "";
$group_password = "";
$group_setting_display_year = "";
$group_setting_graph_type = "";
$group_setting_notification_send_flg = "";
$group_setting_notification_url = "";
$group_setting_notification_token = "";

foreach ($group_refer_api_result['data']['group'] as $group) {

    $group_name = $group['group_list']['group_name'];
    $group_password = $group['group_list']['group_password'];

    $group_settings = [];
    foreach ($group['group_setting'] as $setting) {
        $group_settings[$setting['setting_key']] = $setting['setting_value'];
    }

    $group_setting_display_year = $group_settings['display_year'] ?? null;
    $group_setting_graph_type = $group_settings['graph_type'] ?? null;
    $group_setting_notification_send_flg = $group_settings['notification_send_flg'] ?? null;
    $group_setting_notification_url = $group_settings['notification_url'] ?? null;
    $group_setting_notification_token = $group_settings['notification_token'] ?? null;

}

$group_info_data = $group_info_refer_api_result['data']['group_info'];

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
    <link rel="stylesheet" href="/Interfaces/Assets/CSS/table_form.css">
    <link rel="stylesheet" href="/Interfaces/Assets/CSS/link.css">
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
            所属グループの各種設定を更新します
        </div>
    </div>

    <div class="link-area">
        <div class="link">
            <?php
            //
            if ($user_file_flag OR $screen=="user") {
                echo "<a href='/Interfaces/Views/Pages/admin/admin_user_control.php?$url_param'>ユーザー管理</a>";
                echo "<a href='/Interfaces/Views/Pages/admin/admin_user_entry.php?$url_param'>ユーザー登録</a>";
            }
            if ($user_id != null) {
                echo "<a href='/Interfaces/Views/Pages/admin/admin_user_update.php?$url_param'>ユーザー更新</a>";
            }

            if ($group_file_flag OR $screen=="group") {
                echo "<a href='/Interfaces/Views/Pages/admin/admin_group_control.php?$url_param'>所属グループ管理</a>";
                echo "<a href='/Interfaces/Views/Pages/admin/admin_group_entry.php?$url_param'>所属グループ登録</a>";
            }
            if ($groups_id != null) {
                echo "<a href='/Interfaces/Views/Pages/admin/admin_group_update.php?$url_param'>所属グループ更新</a>";
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
                            <h4 class="settings-title">基本設定</h4>
                            <hr>

                            <div class="settings-form">
                                <div class="settings-label-container">
                                    <div class="settings-label">
                                        所属グループID
                                    </div>
                                </div>
                                <div class="settings-input-container">
                                    <input disabled type="text" class="form-control" name=""
                                        <?php
                                        echo "value='{$groups_id}'";
                                        ?>
                                    >
                                    <input hidden type="text" class="form-control" name="groups_id"
                                        <?php
                                        echo "value='{$groups_id}'";
                                        ?>
                                    >
                                </div>
                            </div>
                            <div class="settings-form">
                                <div class="settings-label-container">
                                    <div class="settings-label">
                                        所属グループ名
                                    </div>
                                </div>
                                <div class="settings-input-container">
                                    <input required type="text" minlength="4" maxlength="32" class="form-control" name="group_name"
                                        <?php
                                        echo "value='{$group_name}'";
                                        ?>
                                    >
                                </div>
                            </div>
                            <div class="settings-form">
                                <div class="settings-label-container">
                                    <div class="settings-label">
                                        所属グループパスワード
                                    </div>
                                </div>
                                <div class="settings-input-container">
                                    <input required type="password" minlength="4" maxlength="64" class="form-control" name="group_password"
                                        <?php
                                        echo "value='{$group_password}'";
                                        ?>
                                    >
                                </div>
                            </div>
                        </div>

                        <!-- 登録ボタン -->
                        <div class="settings-section">
                            <div class="settings-form">
                                <div class="settings-btn-container">
                                    <div class="settings-submit">
                                        <button type="submit" class="btn btn-primary" name="group_entry">
                                            所属グループ更新
                                        </button>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </form>

                    <form action="" method="post">
                        <div class="settings-section">
                            <h4 class="settings-title">所属グループ設定</h4>
                            <hr>

                            <div style='display: none' class="settings-form">
                                <div class="settings-label-container">
                                    <div class="settings-label">
                                        所属グループID
                                    </div>
                                </div>
                                <div class="settings-input-container">
                                    <input hidden type="text" class="form-control" name="groups_id"
                                        <?php
                                        echo "value='{$groups_id}'";
                                        ?>
                                    >
                                </div>
                            </div>

                            <div class="settings-form">
                                <div class="settings-label-container">
                                    <div class="settings-label">
                                        表示年
                                    </div>
                                </div>
                                <div class="settings-input-container">
                                    <div class="settings-input">
                                        <input type="number" class="form-control" name="display_year"
                                            <?php
                                            echo "value='{$group_setting_display_year}'";
                                            ?>
                                        >
                                    </div>
                                </div>
                            </div>
                            <div class="settings-form">
                                <div class="settings-label-container">
                                    <div class="settings-label">
                                        グラフタイプ
                                    </div>
                                </div>
                                <div class="settings-input-container">
                                    <div class="settings-input">
                                        <input type="number" class="form-control" name="graph_type"
                                            <?php
                                            echo "value='{$group_setting_graph_type}'";
                                            ?>
                                        >
                                    </div>
                                </div>
                            </div>
                            <div class="settings-form">
                                <div class="settings-label-container">
                                    <div class="settings-label">
                                        通知送信フラグ
                                    </div>
                                </div>
                                <div class="settings-input-container">
                                    <div class="settings-input">
                                        <input type="number" class="form-control" name="notification_send_flg"
                                            <?php
                                            echo "value='{$group_setting_notification_send_flg}'";
                                            ?>
                                        >
                                    </div>
                                </div>
                            </div>
                            <div class="settings-form">
                                <div class="settings-label-container">
                                    <div class="settings-label">
                                        通知URL
                                    </div>
                                </div>
                                <div class="settings-input-container">
                                    <input type="text" class="form-control" name="notification_url"
                                        <?php
                                        echo "value='{$group_setting_notification_url}'";
                                        ?>
                                    >
                                </div>
                            </div>
                            <div class="settings-form">
                                <div class="settings-label-container">
                                    <div class="settings-label">
                                        通知トークン
                                    </div>
                                </div>
                                <div class="settings-input-container">
                                    <input type="text" class="form-control" name="notification_token"
                                        <?php
                                        echo "value='{$group_setting_notification_token}'";
                                        ?>
                                    >
                                </div>
                            </div>
                        </div>

                        <!-- 登録ボタン -->
                        <div class="settings-section">
                            <div class="settings-form">
                                <div class="settings-btn-container">
                                    <div class="settings-submit">
                                        <button type="submit" class="btn btn-primary" name="setting_entry">
                                            所属グループ設定更新
                                        </button>
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

<div class="table-main">
    <div class="table-area">
        <table class="table table-light table-striped table-bordered table-hover">
            <thead class="table-dark">
            <tr>
                <th>#</th>
                <th>氏名</th>
                <th>権限</th>
                <th>承認</th>
                <th>削除</th>
                <th>リーダー</th>
                <th>所属グループ承認</th>
            </tr>
            </thead>
            <tbody>
            <?php

            foreach ($group_info_data as $group_info) {
                $user_id = htmlspecialchars($group_info['user_id'], ENT_QUOTES, 'UTF-8');

                $userinfo_api_refer_result = apiCallUserRefer($user_id);
                $users_data = $userinfo_api_refer_result['data']['user'];

                $user_name = "";
                $permission = "";
                $approval = "";
                $deleted = "";

                foreach ($users_data as $user) {
                    $user_name = $user['user_info']['user_name'];
                    $permission = $user['user_info']['permission'];
                    $approval = $user['user_info']['approval'];
                    $deleted = $user['user_info']['delete'];
                }


                $leader = $group_info['leader'] == "0" ? "-" : "リーダー";
                $group_approval = $group_info['approval'] == "0" ? "未承認" : "承認";

                echo "<tr style='cursor: pointer;' onclick=\"window.location='/Interfaces/Views/Pages/admin/admin_user_update.php?screen={$screen}&groups_id={$groups_id}&user_id={$user_id}'\">";
                echo "<td>$user_id</td>";
                echo "<td>$user_name</td>";
                echo "<td>$permission</td>";
                echo "<td>$approval</td>";
                echo "<td>$deleted</td>";
                echo "<td>$leader</td>";
                echo "<td>$group_approval</td>";
                echo "</tr>";

            }
            ?>
            </tbody>
        </table>
    </div>
</div>

<footer>
    <?php require $_SERVER['DOCUMENT_ROOT'] . '/Interfaces/Views/Layouts/footer.php'; ?>
</footer>
<!-- bootstrap-datepickerのjavascriptコード -->
<script>
    $('#sample1').datepicker();
</script>
</body>
</html>



