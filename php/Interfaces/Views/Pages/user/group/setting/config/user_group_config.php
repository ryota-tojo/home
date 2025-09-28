<?php
if (session_status() === PHP_SESSION_NONE) {
    session_start();
}
require_once $_SERVER['DOCUMENT_ROOT'] . '/Config/config.php';
require_once $_SERVER['DOCUMENT_ROOT'] . '/Config/log_config.php';
require_once $_SERVER['DOCUMENT_ROOT'] . '/Application/Services/ApiService.php';
require_once $_SERVER['DOCUMENT_ROOT'] . '/Interfaces/Views/Partials/requireApi.php';
require_once $_SERVER['DOCUMENT_ROOT'] . '/Interfaces/Views/Partials/user/create_user.php';
require_once $_SERVER['DOCUMENT_ROOT'] . '/Interfaces/Views/Partials/group/import_group.php';
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

// 変数初期化
$entry_button_click_flg = False;
$message = "";
$read_flag = False;
$entry_error = False;
$user_name = "";

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

$group_data = getGroup($_SESSION['user_groups_id']);
$group=json_decode($group_data,true);
if($group['status'] == 'error'){
    $_SESSION['access_error'] = 1;
    echo "<script>window.location.href = '/Interfaces/Views/Pages/access_error.php';</script>";
}
$group_name = $group['data']['group_info']['group_name'];
$group_password = $group['data']['group_info']['group_password'];
$group_setting_display_year = $group['data']['group_setting']['display_year'];
$group_setting_graph_type = $group['data']['group_setting']['graph_type'];
$group_setting_notification_send_flg = $group['data']['group_setting']['notification_send_flg'];
$group_setting_notification_url = $group['data']['group_setting']['notification_url'];
$group_setting_notification_token = $group['data']['group_setting']['notification_token'];

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
                            <h4 class="settings-title"><?php echo UI_ITEM_GROUP; ?>情報</h4>
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
                                        echo "value='{$_SESSION['user_groups_id']}'";
                                        ?>
                                    >
                                    <input hidden type="text" class="form-control" name="groups_id"
                                        <?php
                                        echo "value='{$_SESSION['user_groups_id']}'";
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
                                        <?php echo UI_ITEM_GROUP; ?>情報更新
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
                                        echo "value='{$_SESSION['user_groups_id']}'";
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

<footer>
    <?php require $_SERVER['DOCUMENT_ROOT'] . '/Interfaces/Views/Layouts/footer.php'; ?>
</footer>

<script>
    function toggleAllFromTh(th) {
        const checkbox = th.querySelector('#select-all');
        if (checkbox) {
            checkbox.checked = !checkbox.checked;
            toggleAll(checkbox);  // 全体に反映
        }
    }

    function toggleAll(source) {
        const checkboxes = document.querySelectorAll('input[name="selected_users[]"]');
        checkboxes.forEach(cb => {
            cb.checked = source.checked;
        });
    }
</script>
<script>
    function toggleCheckbox(td) {
        const checkbox = td.querySelector('input[type="checkbox"]');
        if (checkbox) {
            checkbox.checked = !checkbox.checked;
        }
    }
</script>
<script>
    document.addEventListener('DOMContentLoaded', function () {
        const searchTypeRadios = document.querySelectorAll('input[name="search_type"]');
        const userIdField = document.getElementById('userIdField');
        const userNameField = document.getElementById('userNameField');
        const approvalField = document.getElementById('approvalField');
        const deletedField = document.getElementById('deletedField');
        const groupApproval = document.getElementById('groupApproval');
        const groupMemberType = document.getElementById('groupMemberType');

        groupMemberType
        // 初期表示
        toggleFields();

        function toggleFields() {
            const selectedValue = document.querySelector('input[name="search_type"]:checked').value;

            if (selectedValue === 'user_id') {
                userIdField.style.display = 'block';
                userNameField.style.display = 'none';
                approvalField.style.display = 'none';
                deletedField.style.display = 'none';
                groupApproval.style.display = 'none';
                groupMemberType.style.display = 'none';
            } else if (selectedValue === 'wildcard') {
                userIdField.style.display = 'none';
                userNameField.style.display = 'block';
                approvalField.style.display = 'block';
                deletedField.style.display = 'block';
                groupApproval.style.display = 'block';
                groupMemberType.style.display = 'block';
            }
        }

        // ラジオボタンが変わった時に表示切り替え
        searchTypeRadios.forEach(function (radio) {
            radio.addEventListener('change', function () {
                toggleFields();
            });
        });
    });
</script>

<?php
require $_SERVER['DOCUMENT_ROOT'] . '/Interfaces/Assets/JS/basic_js.php';
?>

</body>
</html>



