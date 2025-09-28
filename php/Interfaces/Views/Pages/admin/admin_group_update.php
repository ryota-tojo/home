<?php
if (session_status() === PHP_SESSION_NONE) {
    session_start();
}
require_once $_SERVER['DOCUMENT_ROOT'] . '/Config/config.php';
require_once $_SERVER['DOCUMENT_ROOT'] . '/Config/log_config.php';
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

$filename = basename(__FILE__);
$user_file_flag = str_contains($filename, 'user');
$group_file_flag = str_contains($filename, 'group');
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
} else {
    $_SESSION['access_error'] = 1;
    echo "<script>window.location.href = '/Interfaces/Views/Pages/access_error.php';</script>";

}
$url_param = implode('&', $url_param);

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
$master_setting_admin_groupinfodata_view = $master_settings['admin_groupinfodata_view'] ?? null;

// 1ページに表示する件数
$limit = $master_setting_admin_groupinfodata_view;

// 現在のページ番号
$page = isset($_GET['page']) ? (int)$_GET['page'] : 1;
$offset = ($page - 1) * $limit;

// ボタン押下時の処理
if (isset($_POST['group_entry'])) {
    $entry_button_click_flg = True;

    $groups_id = $_POST['groups_id'] ?? '';
    $group_name = $_POST['group_name'] ?? '';
    $group_password = $_POST['group_password'] ?? '';

    $group_api_update_result = apiCallGroupUpdateList($groups_id, $group_name, $group_password);
    $status = $group_api_update_result['status'];

    if ($status != "success") {

        $entry_error = True;
        $message = UI_ITEM_GROUP . "の更新に失敗しました";
        createLogs(LOG_TYPE_ERROR, UI_ITEM_GROUP . "の更新に失敗");
    } else {
        $message = UI_ITEM_GROUP . "を更新しました";
        createLogs(LOG_TYPE_INFO, UI_ITEM_GROUP . "を更新");
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
        $message = UI_ITEM_GROUP . "設定の更新に失敗しました。<br>エラーが発生した設定: " . $error_keys_str;
        $entry_error = true;
        createLogs(LOG_TYPE_ERROR, UI_ITEM_GROUP . "設定の更新に失敗");
    } else {
        $message = UI_ITEM_GROUP . "設定が正常に更新されました。";
        createLogs(LOG_TYPE_INFO, UI_ITEM_GROUP . "設定を更新");
    }
}

if (isset($_POST['user_assign'])) {
    echo "<script>window.location.href = 'admin_group_assign.php?$url_param';</script>";
}

if (isset($_POST['user_approval'])) {
    $entry_button_click_flg = True;
    $suc_cnt = 0;
    $err_cnt = 0;
    if ($_SERVER['REQUEST_METHOD'] === 'POST' && isset($_POST['selected_users'])) {
        $selected_users = $_POST['selected_users'];

        foreach ($selected_users as $user_id) {

            $result = apiCallGroupInfoRefer($groups_id, $user_id);
            $user_leader_flg = "0";
            $user_group_approval = "0";
            if ($result['status'] == "success") {
                foreach ($result['data']['group_info'] as $group_info) {
                    $user_leader_flg = $group_info['leader'];
                    $user_group_approval = $group_info['approval'];
                }
            }
            if ($user_leader_flg == "1") {
                $err_cnt += 1;
                continue;
            }
            if ($user_group_approval == "1") {
                $err_cnt += 1;
                continue;
            }
            apiCallGroupInfoUpdate($groups_id, $user_id, null, 1);
            $suc_cnt += 1;
        }

        $message = $suc_cnt . "件の" . UI_ITEM_USER . "を承認しました<br>" . $err_cnt . "件の" . UI_ITEM_USER . "をスキップしました";
        createLogs(LOG_TYPE_INFO, UI_ITEM_USER . "承認 - 成功：{$suc_cnt}件, スキップ：{$err_cnt}件");

    } else {
        $message = UI_ITEM_USER . "が選択されていません";
        $entry_error = true;
        createLogs(LOG_TYPE_ERROR, UI_ITEM_USER . "承認 - " . UI_ITEM_USER . "未選択");
    }
}

if (isset($_POST['user_un_approval'])) {
    $entry_button_click_flg = True;
    $suc_cnt = 0;
    $err_cnt = 0;
    if ($_SERVER['REQUEST_METHOD'] === 'POST' && isset($_POST['selected_users'])) {
        $selected_users = $_POST['selected_users'];

        foreach ($selected_users as $user_id) {

            $result = apiCallGroupInfoRefer($groups_id, $user_id);
            $user_leader_flg = "0";
            $user_group_approval = "0";
            if ($result['status'] == "success") {
                foreach ($result['data']['group_info'] as $group_info) {
                    $user_leader_flg = $group_info['leader'];
                    $user_group_approval = $group_info['approval'];
                }
            }
            if ($user_leader_flg == "1") {
                $err_cnt += 1;
                continue;
            }
            if ($user_group_approval == "0") {
                $err_cnt += 1;
                continue;
            }
            apiCallGroupInfoUpdate($groups_id, $user_id, null, 0);
            $suc_cnt += 1;
        }

        $message = $suc_cnt . "件の" . UI_ITEM_USER . "を否認しました<br>" . $err_cnt . "件の" . UI_ITEM_USER . "をスキップしました";
        createLogs(LOG_TYPE_INFO, UI_ITEM_USER . "否認 - 成功：{$suc_cnt}件, スキップ：{$err_cnt}件");

    } else {
        $message = UI_ITEM_USER . "が選択されていません";
        $entry_error = true;
        createLogs(LOG_TYPE_ERROR, UI_ITEM_USER . "否認 - " . UI_ITEM_USER . "未選択");
    }
}

if (isset($_POST['user_deleted'])) {
    $entry_button_click_flg = True;
    $suc_cnt = 0;
    $err_cnt = 0;
    if ($_SERVER['REQUEST_METHOD'] === 'POST' && isset($_POST['selected_users'])) {
        $selected_users = $_POST['selected_users'];

        foreach ($selected_users as $user_id) {

            $result = apiCallGroupInfoRefer($groups_id, $user_id);
            $user_leader_flg = "0";
            $user_group_approval = "0";
            if ($result['status'] == "success") {
                foreach ($result['data']['group_info'] as $group_info) {
                    $user_leader_flg = $group_info['leader'];
                    $user_group_approval = $group_info['approval'];
                }
            }
            if ($user_leader_flg == "1") {
                $err_cnt += 1;
                continue;
            }
            if ($user_group_approval == "1") {
                $err_cnt += 1;
                continue;
            }
            apiCallGroupInfoDelete($groups_id, $user_id);
            $suc_cnt += 1;
        }

        $message = $suc_cnt . "件の" . UI_ITEM_USER . "を除籍しました<br>" . $err_cnt . "件の" . UI_ITEM_USER . "をスキップしました";
        createLogs(LOG_TYPE_INFO, UI_ITEM_USER . "除籍 - 成功：{$suc_cnt}件, スキップ：{$err_cnt}件");

    } else {
        $message = UI_ITEM_USER . "が選択されていません";
        $entry_error = true;
        createLogs(LOG_TYPE_ERROR, UI_ITEM_USER . "除籍 - " . UI_ITEM_USER . "未選択");
    }
}


$group_refer_api_result = apiCallGroupRefer($groups_id);
if ($group_refer_api_result['status'] == "error") {
    $_SESSION['access_error'] = 1;
    echo "<script>window.location.href = '/Interfaces/Views/Pages/access_error.php';</script>";
}
$group_info_count_api_result = apiCallGroupInfoCount($groups_id);
if ($group_info_count_api_result['status'] != "error") {
    $group_info_refer_api_result = apiCallGroupInfoRefer($groups_id, null, null, $offset, $limit);
    if ($group_info_refer_api_result['status'] == "error") {
        $_SESSION['access_error'] = 1;
        echo "<script>window.location.href = '/Interfaces/Views/Pages/access_error.php';</script>";
    }
}

if(isset($group_info_refer_api_result)){
    // 所属グループ情報の総数を取得
    $total_groupinfos = $group_info_count_api_result['data']['recode_count'];
    $group_info_data = $group_info_refer_api_result['data']['group_info'];
    // 総ページ数を計算
    $total_pages = ceil($total_groupinfos / $limit);
}else{
    $total_pages=1;
    $group_info_data=[];
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
                            <h4 class="settings-title"><?php echo UI_ITEM_GROUP_INFO; ?></h4>
                            <hr>

                            <div class="settings-form">
                                <div class="settings-label-container">
                                    <div class="settings-label">
                                        <?php echo UI_ITEM_GROUPS_ID; ?>
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
                                        <?php echo UI_ITEM_GROUP_NAME; ?>
                                    </div>
                                </div>
                                <div class="settings-input-container">
                                    <input required type="text" minlength="4" maxlength="32" class="form-control"
                                           name="group_name"
                                        <?php
                                        echo "value='{$group_name}'";
                                        ?>
                                    >
                                </div>
                            </div>
                            <div class="settings-form">
                                <div class="settings-label-container">
                                    <div class="settings-label">
                                        <?php echo UI_ITEM_GROUP_PASSWORD; ?>
                                    </div>
                                </div>
                                <div class="settings-input-container">
                                    <input required type="password" minlength="4" maxlength="64" class="form-control"
                                           name="group_password"
                                        <?php
                                        echo "value='{$group_password}'";
                                        ?>
                                    >
                                </div>
                            </div>
                        </div>

                        <!-- 登録ボタン -->
                        <div class="btn-area">
                            <div class="btn-center-area">
                                <div class="btn-item">
                                    <button type="submit" class="btn btn-primary" name="group_entry">
                                        <?php echo UI_ITEM_GROUP_INFO; ?>更新
                                    </button>
                                </div>
                            </div>
                        </div>

                    </form>

                    <form action="" method="post">
                        <div class="settings-section">
                            <h4 class="settings-title"><?php echo UI_ITEM_GROUP; ?>設定</h4>
                            <hr>

                            <div style='display: none' class="settings-form">
                                <div class="settings-label-container">
                                    <div class="settings-label">
                                        <?php echo UI_ITEM_GROUPS_ID; ?>
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
                        <div class="btn-area">
                            <div class="btn-center-area">
                                <div class="btn-item">
                                    <button type="submit" class="btn btn-primary" name="setting_entry">
                                        <?php echo UI_ITEM_GROUP; ?>設定更新
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

<hr>

<form method="post" action="">

    <div class="btn-area">
        <div class="btn-left-area">
            <div class="btn-item">
                <button type="submit" class="btn btn-primary" name="user_assign">
                    <?php echo UI_ITEM_USER; ?>を配属する
                </button>
            </div>
        </div>
        <div class="btn-right-area">
            <div class="btn-item">
                <button type="submit" class="btn btn-primary" name="user_approval"
                        onclick="return confirm('本当に実行しますか？\n以下の<?php echo UI_ITEM_USER; ?>はスキップされます。\n・ リーダー<?php echo UI_ITEM_USER; ?>\n・ 既に承認済みの<?php echo UI_ITEM_USER; ?>')">
                    選択<?php echo UI_ITEM_USER; ?>を承認
                </button>
            </div>
            <div class="btn-item">
                <button type="submit" class="btn btn-warning" name="user_un_approval"
                        onclick="return confirm('本当に実行しますか？\n以下の<?php echo UI_ITEM_USER; ?>はスキップされます。\n・ リーダー<?php echo UI_ITEM_USER; ?>\n・ 既に未承認の<?php echo UI_ITEM_USER; ?>')">
                    選択<?php echo UI_ITEM_USER; ?>を否認
                </button>
            </div>
            <div class="btn-item">
                <button type="submit" class="btn btn-danger" name="user_deleted"
                        onclick="return confirm('本当に実行しますか？\n以下の<?php echo UI_ITEM_USER; ?>はスキップされます。\n・ リーダー<?php echo UI_ITEM_USER; ?>\n・ 承認済みの<?php echo UI_ITEM_USER; ?>')">
                    選択<?php echo UI_ITEM_USER; ?>を除籍
                </button>
            </div>
        </div>
    </div>

    <div class="table-main">
        <div class="table-area">
            <table class="table table-light table-striped table-bordered table-hover">
                <thead class="table-dark">
                <tr>
                    <th><input type="checkbox" id="select-all" onclick="toggleAll(this)"></th>
                    <th>#</th>
                    <th><?php echo UI_ITEM_USER_NAME; ?></th>
                    <th><?php echo UI_ITEM_USER_PERMISSION; ?></th>
                    <th><?php echo UI_ITEM_USER_APPROVAL; ?></th>
                    <th><?php echo UI_ITEM_USER_DELETED; ?></th>
                    <th><?php echo UI_ITEM_GROUP_INFO_LEADER; ?></th>
                    <th><?php echo UI_ITEM_GROUP_INFO_APPROVAL; ?></th>
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

                        if ($user['user_info']['permission'] == "0") {
                            $permission = "一般";
                        } else if ($user['user_info']['permission'] == "1") {
                            $permission = "一般管理者";
                        } else {
                            $permission = "管理者";
                        }

                        $approval = $user['user_info']['approval'] == "0" ? "未承認" : "承認";
                        $deleted = $user['user_info']['delete'] == "0" ? "-" : "削除";
                    }

                    $leader = $group_info['leader'] == "0" ? "一般" : "リーダー";
                    $group_approval = $group_info['approval'] == "0" ? "未承認" : "承認";

                    echo "<tr style='cursor: pointer;' onclick=\"window.location='/Interfaces/Views/Pages/admin/admin_user_update.php?screen={$screen}&groups_id={$groups_id}&user_id={$user_id}'\">";
                    echo "<td><input type='checkbox' name='selected_users[]' value='$user_id' onclick='event.stopPropagation();'></td>";
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
</form>

<!-- ページネーション -->
<div class="pagination-container">
    <nav>
        <ul class="pagination justify-content-center">
            <?php if ($page > 1): ?>
                <li class="page-item">
                    <a class="page-link" href="?<?php echo $url_param; ?>&page=1">最初</a>

                </li>
                <li class="page-item">
                    <a class="page-link" href="?<?php echo $url_param; ?>&page=<?php echo $page - 1; ?>">前</a>
                </li>
            <?php else: ?>
                <li class="page-item disabled">
                    <a class="page-link" href="#">最初</a>
                </li>
                <li class="page-item disabled">
                    <a class="page-link" href="#">前</a>
                </li>
            <?php endif; ?>

            <!-- ページ番号 -->
            <?php
            // 表示するページの範囲を設定
            $start = max(1, $page - 2); // 最初のページは1
            $end = min($total_pages, $page + 2); // 最後のページは$total_pages

            for ($i = $start; $i <= $end; $i++): ?>
                <li class="page-item <?php echo ($i === $page) ? 'active' : ''; ?>">
                    <a class="page-link" href="?<?php echo $url_param; ?>&page=<?php echo $i; ?>"><?php echo $i; ?></a>
                </li>
            <?php endfor; ?>

            <?php if ($page < $total_pages): ?>
                <li class="page-item">
                    <a class="page-link" href="?<?php echo $url_param; ?>&page=<?php echo $page + 1; ?>">次</a>
                </li>
                <li class="page-item">
                    <a class="page-link" href="?<?php echo $url_param; ?>&page=<?php echo $total_pages; ?>">最後</a>
                </li>
            <?php else: ?>
                <li class="page-item disabled">
                    <a class="page-link" href="#">次</a>
                </li>
                <li class="page-item disabled">
                    <a class="page-link" href="#">最後</a>
                </li>
            <?php endif; ?>
        </ul>
    </nav>
</div>


<footer>
    <?php require $_SERVER['DOCUMENT_ROOT'] . '/Interfaces/Views/Layouts/footer.php'; ?>
</footer>

<script>
    function toggleAll(source) {
        const checkboxes = document.querySelectorAll('input[name="selected_users[]"]');
        checkboxes.forEach(cb => cb.checked = source.checked);
    }
</script>

<?php
require $_SERVER['DOCUMENT_ROOT'] . '/Interfaces/Assets/JS/basic_js.php';
?>

</body>
</html>



