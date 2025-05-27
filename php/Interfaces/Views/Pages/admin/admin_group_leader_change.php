<?php
if (session_status() === PHP_SESSION_NONE) {
    session_start();
}
require $_SERVER['DOCUMENT_ROOT'] . '/config/config.php';
require $_SERVER['DOCUMENT_ROOT'] . '/Application/Services/api_service.php';
require $_SERVER['DOCUMENT_ROOT'] . '/Interfaces/Views/Partials/requireApi.php';


$screen_title = "所属グループリーダー変更";

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
    <link rel="stylesheet" href="/Interfaces/Assets/CSS/search_form.css">
    <link rel="stylesheet" href="/Interfaces/Assets/CSS/setting_form.css">
    <link rel="stylesheet" href="/Interfaces/Assets/CSS/message.css">
    <link rel="stylesheet" href="/Interfaces/Assets/CSS/table_form.css">
    <link rel="stylesheet" href="/Interfaces/Assets/CSS/button_form.css">
    <link rel="stylesheet" href="/Interfaces/Assets/CSS/modal.css">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha3/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        .item-label {
            display: flex;
        }

        .user-id-label {

        }

        .user-name-label {
            margin-left: 30px;
        }

    </style>
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
if (isset($_POST['update'])) {
    $entry_button_click_flg = True;

    $leader_id = $_POST['leader-id'];
    $leader_name = $_POST['leader-name'];

    $new_leader_id = $_POST['new-leader-id'];
    $new_leader_name = $_POST['new-leader-name'];

    $group_info_api_leader_change_result = apiCallGroupInfoLeaderChange($groups_id, $leader_id, $new_leader_id);
    $status = $group_info_api_leader_change_result['status'];

    if ($status == 'error') {
        $message = $group_info_api_leader_change_result['data']['message'];
        $entry_error = True;
    } else {
        $message = "所属グループのリーダーを変更しました";
    }

}

$group_api_refer_result = apiCallGroupRefer($groups_id);
$group_list_data = $group_api_refer_result['data']['group'];
$group_name = "";
foreach ($group_list_data as $group) {
    $group_name = $group['group_list']['group_name'];
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
    $group_leader = "-";
    $group_approval = "-";

    foreach ($user['group_info'] as $group) {
        $groups_id = $group['groups_id'];
        $group_leader = $group['leader'];
        $group_approval = $group['approval'];
    }
}

$leader_id = "";
$leader_name = "";
$group_api_refer_result = apiCallGroupInfoAndUserInfoRefer($groups_id, null, 1, null, null);
$group_data = $group_api_refer_result['data']['group_info'];
foreach ($group_data as $group) {
    $leader_id = $group['user_id'];
    $leader_name = $group['user_name'];
}

// ユーザー情報を取得する際に、limit と offset を使用する
$group_api_count_result = apiCallGroupInfoCount($groups_id, null, 0);
if ($group_api_count_result['status'] != "error") {
    $group_api_refer_result = apiCallGroupInfoAndUserInfoRefer($groups_id, null, 0, $offset, $limit);
    $group_data = $group_api_refer_result['data']['group_info'];
}

// ユーザーの総数を取得
$total_users = $group_api_count_result['data']['recode_count'];

// 総ページ数を計算
$total_pages = ceil($total_users / $limit);

?>

<main>
    <div class="title-area">
        <h2 class="title"><?php echo $screen_title; ?></h2>
    </div>
    <div class="summary-area">
        <div class="summary">
            所属グループのリーダーを変更します
        </div>
    </div>

    <div class="btn-area">
        <div class="btn-center-area">
            <?php
            //
            if ($user_id != null) {
                echo "<div class='btn-item'><a class='link-btn' href='/Interfaces/Views/Pages/admin/admin_user_update.php?$url_param'>ユーザー更新</a></div>";
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
                <form id="leader-form" action="" method="post">
                    <div class="form-area">
                        <h4 class="">所属グループ：<?php echo $group_name; ?></h4><br>

                        <h6 class="form-title"><?php echo "リーダー情報"; ?></h6>
                        <hr>

                        <div class="form-item remarks-item" id="groupIdField">
                            <div class="form-item-label">
                                <label class="item-label">
                                    <input hidden type="text" class="form-control" name="leader-id"
                                        <?php
                                        echo "value='{$leader_id}'";
                                        ?>
                                    >
                                    <input hidden type="text" class="form-control" name="leader-name"
                                        <?php
                                        echo "value='{$leader_name}'";
                                        ?>
                                    >
                                    ユーザーID：
                                    <div class="user-id-label"><?= $leader_id ?></div>
                                    <div class="user-name-label"><?= $leader_name ?></div>
                                </label>
                            </div>
                        </div>
                        <br>

                        <div style="display: flex;">
                            <h6 class="form-title" style="margin-top: 20px"><?php echo "新リーダー情報"; ?></h6>
                            <div class='disabled-comment' style="margin-top: 25px;">※ユーザーをクリックしてください
                            </div>
                        </div>
                        <hr>

                        <div class="form-item remarks-item" id="groupIdField">
                            <div class="form-item-label">
                                <label class="item-label">
                                    <input hidden type="text" class="form-control" name="new-leader-id"
                                        <?php
                                        echo "value=''";
                                        ?>
                                    >
                                    <input hidden type="text" class="form-control" name="new-leader-name"
                                        <?php
                                        echo "value=''";
                                        ?>
                                    >
                                    ユーザーID：
                                    <div class="user-id-label" id="new-leader-id-label"></div>
                                    <div class="user-name-label" id="new-leader-name-label"></div>
                                </label>
                            </div>
                        </div><br>

                        <!-- 登録ボタン-->
                        <div class="btn-area">
                            <div class="btn-center-area">
                                <div class="btn-item">
                                    <button type="button" class="btn btn-primary entry-btn" name="update"
                                            onclick="confirmLeaderChange()">変更
                                    </button>
                                </div>
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

<div class="table-main">
    <div class="table-area">
        <table class="table table-light table-striped table-bordered table-hover">
            <thead class="table-dark">
            <tr>
                <th>#</th>
                <th>ユーザー名</th>
                <th>権限</th>
                <th>承認</th>
                <th>削除</th>
                <th>リーダー</th>
                <th>グループ承認</th>
            </tr>
            </thead>
            <tbody>
            <?php
            if (!isset($group_data)) {
                $group_data = [];
            }
            foreach ($group_data as $group) {

                $user_id = $group['user_id'];
                $user_name = $group['user_name'];
                $user_permission = $group['user_permission'];
                $user_approval = $group['user_approval'];
                $user_deleted = $group['user_deleted'];
                $group_leader = $group['group_leader'];
                $group_approval = $group['group_approval'];

                if ($user_approval == 0 or $user_deleted == 1 or $group_approval == 0) {
                    $class = "class='lock-rows'";
                    $onclick = ""; // クリックなし
                } else {
                    $class = "class='clickable-row'";
                    $onclick = "onclick=\"onRowClick('$user_id', '$user_name')\"";
                }
                echo "<tr $class $onclick>";
                echo "<td>$user_id</td>";
                echo "<td>$user_name</td>";
                echo "<td>$user_permission</td>";
                echo "<td>$user_approval</td>";
                echo "<td>$user_deleted</td>";
                echo "<td>$group_leader</td>";
                echo "<td>$group_approval</td>";
                echo "</tr>";

            }
            ?>

            </tbody>
        </table>
    </div>
</div>

<!-- ページネーション -->
<div class="pagination-container">
    <nav>
        <ul class="pagination justify-content-center">
            <?php if ($page > 1): ?>
                <li class="page-item">
                    <a class="page-link" href="?<?php echo $url_param . "&" ?>page=1&modal=1">最初</a>
                </li>
                <li class="page-item">
                    <a class="page-link"
                       href="?<?php echo $url_param . "&" ?>page=<?php echo $page - 1; ?>&modal=1">前</a>
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
                    <a class="page-link"
                       href="?<?php echo $url_param . "&" ?>page=<?php echo "$i"; ?>&modal=1"><?php echo $i; ?></a>
                </li>
            <?php endfor; ?>

            <?php if ($page < $total_pages): ?>
                <li class="page-item">
                    <a class="page-link"
                       href="?<?php echo $url_param . "&" ?>page=<?php echo $page + 1; ?>&modal=1">次</a>
                </li>
                <li class="page-item">
                    <a class="page-link"
                       href="?<?php echo $url_param . "&" ?>page=<?php echo $total_pages; ?>&modal=1">最後</a>
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
    function onRowClick(userId, userName) {
        // フォームの hidden フィールドに値をセット
        document.querySelector("input[name='new-leader-id']").value = userId;
        document.querySelector("input[name='new-leader-name']").value = userName;

        // 表示ラベルに値を反映
        document.getElementById("new-leader-id-label").textContent = userId;
        document.getElementById("new-leader-name-label").textContent = userName;
    }
</script>
<script>
    function confirmLeaderChange() {
        if (confirm("このユーザーをリーダーに設定しますか？")) {
            // フォームに entry を追加（なければ）
            let form = document.getElementById("leader-form");
            let hiddenEntry = document.createElement("input");
            hiddenEntry.type = "hidden";
            hiddenEntry.name = "update";
            hiddenEntry.value = "1";
            form.appendChild(hiddenEntry);

            form.submit();
        }
    }
</script>

<?php
require $_SERVER['DOCUMENT_ROOT'] . '/Interfaces/Assets/JS/basic_js.php';
?>

</body>
</html>



