<?php
if (session_status() === PHP_SESSION_NONE) {
    session_start();
}
require $_SERVER['DOCUMENT_ROOT'] . '/config/config.php';
require $_SERVER['DOCUMENT_ROOT'] . '/Application/Services/ApiService.php';
require $_SERVER['DOCUMENT_ROOT'] . '/Interfaces/Views/Partials/requireApi.php';


$screen_title = "ユーザー管理";

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
}
$url_param = implode('&', $url_param);

// 変数初期化
$entry_button_click_flg = False;
$message = "";
$entry_error = False;
if (!isset($_SESSION['user_search_type_flg'])) {
    $_SESSION['user_search_type_flg'] = "user_id";
}
if (!isset($_SESSION['user_count'])) {
    $_SESSION['user_count'] = 0;
}
if (!isset($_SESSION['search_user_id'])) {
    $_SESSION['search_user_id'] = 0;
}
if (!isset($_SESSION['search_user_name'])) {
    $_SESSION['search_user_name'] = null;
}
if (!isset($_SESSION['search_permission'])) {
    $_SESSION['search_permission'] = null;
}
if (!isset($_SESSION['search_approval'])) {
    $_SESSION['search_approval'] = null;
}
if (!isset($_SESSION['search_deleted'])) {
    $_SESSION['search_deleted'] = null;
}

// マスター設定
$master_setting_api_refer_result = apiCallMasterSettingRefer();
$master_settings = [];
foreach ($master_setting_api_refer_result['data']['setting_list'] as $setting) {
    $master_settings[$setting['setting_key']] = $setting['setting_value'];
}
$master_setting_admin_userdata_view = $master_settings['admin_userdata_view'] ?? null;

// 1ページに表示する件数
$limit = $master_setting_admin_userdata_view;

// 現在のページ番号
$page = isset($_GET['page']) ? (int)$_GET['page'] : 1;
$offset = ($page - 1) * $limit;

// ボタン押下時の処理
if (isset($_POST['entry'])) {
    $entry_button_click_flg = True;

    $_SESSION['user_search_type_flg'] = $_POST['search_type'];

    $_SESSION['search_user_id'] = null;
    $_SESSION['search_user_name'] = null;
    $_SESSION['search_permission'] = null;
    $_SESSION['search_approval'] = null;
    $_SESSION['search_deleted'] = null;

    if ($_SESSION['user_search_type_flg'] == "user_id") {
        if (isset($_POST['user_id'])) {
            if ($_POST['user_id'] != "") {
                $_SESSION['search_user_id'] = $_POST['user_id'];
            }
        }
    }
    if ($_SESSION['user_search_type_flg'] == "wildcard") {
        if (isset($_POST['user_name'])) {
            if ($_POST['user_name'] != "") {
                $_SESSION['search_user_name'] = $_POST['user_name'];
            }
        }
        if (isset($_POST['permission'])) {
            if ($_POST['permission'] != "-1") {
                $_SESSION['search_permission'] = $_POST['permission'];
            }
        }
        if (isset($_POST['approval'])) {
            if ($_POST['approval'] != "-1") {
                $_SESSION['search_approval'] = $_POST['approval'];
            }
        }
        if (isset($_POST['deleted'])) {
            if ($_POST['deleted'] != "-1") {
                $_SESSION['search_deleted'] = $_POST['deleted'];
            }
        }
    }

    $message = "ユーザー検索条件を変更しました";

}
if (isset($_POST['reset'])) {
    $_SESSION['search_user_id'] = null;
    $_SESSION['search_user_name'] = null;
    $_SESSION['search_permission'] = null;
    $_SESSION['search_approval'] = null;
    $_SESSION['search_deleted'] = null;
    $_SESSION['user_search_type_flg'] = "user_id";
    header("Location: " . $_SERVER['PHP_SELF']); // フォーム再送信防止
    exit;
}

// ユーザー情報を取得する際に、limit と offset を使用する
$userinfo_api_count_result = apiCallUserCount($_SESSION['search_user_id'], $_SESSION['search_user_name'], $_SESSION['search_permission'], $_SESSION['search_approval'], $_SESSION['search_deleted']);
if($userinfo_api_count_result['status'] != "error"){
    $userinfo_api_refer_result = apiCallUserRefer($_SESSION['search_user_id'], $_SESSION['search_user_name'], $_SESSION['search_permission'], $_SESSION['search_approval'], $_SESSION['search_deleted'], $offset, $limit);
    $users_data = $userinfo_api_refer_result['data']['user'];
}

// ユーザーの総数を取得
$total_users = $userinfo_api_count_result['data']['recode_count'];

// 総ページ数を計算
$total_pages = ceil($total_users / $limit);

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
            ユーザー情報を管理します
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
                <form action="" method="post">
                    <div class="form-area">
                        <h6 class="form-title">検索フォーム</h6>
                        <hr>

                        <!-- 設定切替 -->
                        <div class="form-item payment-item">
                            <div class="form-item-label">
                                <label class="item-label">検索方法</label>
                            </div>
                            <div class="input-group form-item">
                                <div class="form-check">
                                    <input class="form-check-input" type="radio" name="search_type" id="searchUserId"
                                           value="user_id"
                                        <?= ($_SESSION['user_search_type_flg'] === "user_id" || !isset($_SESSION['user_search_type_flg'])) ? 'checked' : '' ?>>
                                    <label class="form-check-label" for="searchUserId">ユーザーIDで検索</label>
                                </div>
                                <div class="form-check">
                                    <input class="form-check-input" type="radio" name="search_type" id="searchUserName"
                                           value="wildcard"
                                        <?= ($_SESSION['user_search_type_flg'] === "wildcard") ? 'checked' : '' ?>>
                                    <label class="form-check-label" for="searchUserName">曖昧検索</label>
                                </div>
                            </div>
                        </div>

                        <!-- ユーザーID -->
                        <div class="form-item remarks-item" id="userIdField">
                            <div class="form-item-label">
                                <label class="item-label">ユーザーID</label>
                            </div>
                            <div class="input-group form-item">
                                <input type="number" class="form-control" name="user_id"
                                       value="<?php if($_SESSION['search_user_id']==null){echo "";}else{ echo $_SESSION['search_user_id']; }?>"
                                >
                            </div>
                        </div>

                        <!-- ユーザー名 -->
                        <div class="form-item remarks-item" id="userNameField">
                            <div class="form-item-label">
                                <label class="item-label">ユーザー名</label>
                            </div>
                            <div class="input-group form-item">
                                <input type="text" class="form-control" name="user_name"
                                       value="<?= $_POST['user_name'] ?? ($_SESSION['search_user_name'] ?? '') ?>">
                            </div>
                        </div>

                        <!-- 権限 -->
                        <div class="form-item payment-item" id="permissionField">
                            <div class="form-item-label">
                                <label class="item-label">権限</label>
                            </div>
                            <div class="input-group form-item">
                                <select class="form-select" name="permission">
                                    <option value="-1" <?= ($_SESSION['search_permission'] == -1 || !isset($_SESSION['search_permission'])) ? 'selected' : '' ?>>すべて</option>
                                    <option value="0" <?= ($_SESSION['search_permission'] == 0) ? 'selected' : '' ?>>一般</option>
                                    <option value="1" <?= ($_SESSION['search_permission'] == 1) ? 'selected' : '' ?>>一般管理者</option>
                                    <option value="2" <?= ($_SESSION['search_permission'] == 2) ? 'selected' : '' ?>>管理者</option>
                                </select>
                            </div>
                        </div>

                        <!-- 承認 -->
                        <div class="form-item payment-item" id="approvalField">
                            <div class="form-item-label">
                                <label class="item-label">承認</label>
                            </div>
                            <div class="input-group form-item">
                                <select class="form-select" name="approval">
                                    <option value="-1" <?= ($_SESSION['search_approval'] == -1 || !isset($_SESSION['search_approval'])) ? 'selected' : '' ?>>すべて</option>
                                    <option value="0" <?= ($_SESSION['search_approval'] == 0) ? 'selected' : '' ?>>未承認</option>
                                    <option value="1" <?= ($_SESSION['search_approval'] == 1) ? 'selected' : '' ?>>承認</option>
                                </select>
                            </div>
                        </div>

                        <!-- 削除 -->
                        <div class="form-item payment-item" id="deletedField">
                            <div class="form-item-label">
                                <label class="item-label">削除</label>
                            </div>
                            <div class="input-group form-item">
                                <select class="form-select" name="deleted">
                                    <option value="-1" <?= ($_SESSION['search_deleted'] == -1 || !isset($_SESSION['search_deleted'])) ? 'selected' : '' ?>>すべて</option>
                                    <option value="0" <?= ($_SESSION['search_deleted'] == 0) ? 'selected' : '' ?>>未削除</option>
                                    <option value="1" <?= ($_SESSION['search_deleted'] == 1) ? 'selected' : '' ?>>削除済</option>
                                </select>
                            </div>
                        </div>


                        <!-- 登録ボタン-->
                        <div class="form-item">
                            <div class="submit-area">
                                <button type="submit" class="btn btn-primary entry-btn" name="entry">検索</button>
                                <button type="submit" class="btn btn-warning reset-btn" name="reset">リセット</button>
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

<!-- ページネーション -->
<div class="pagination-container">
    <nav>
        <ul class="pagination justify-content-center">
            <?php if ($page > 1): ?>
                <li class="page-item">
                    <a class="page-link" href="?page=1">最初</a>
                </li>
                <li class="page-item">
                    <a class="page-link" href="?page=<?php echo $page - 1; ?>">前</a>
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
                    <a class="page-link" href="?page=<?php echo $i; ?>"><?php echo $i; ?></a>
                </li>
            <?php endfor; ?>

            <?php if ($page < $total_pages): ?>
                <li class="page-item">
                    <a class="page-link" href="?page=<?php echo $page + 1; ?>">次</a>
                </li>
                <li class="page-item">
                    <a class="page-link" href="?page=<?php echo $total_pages; ?>">最後</a>
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
                <th>所属グループ</th>
                <th>リーダーフラグ</th>
            </tr>
            </thead>
            <tbody>
            <?php

            if(!isset($users_data)){
                $users_data=[];
            }
            foreach ($users_data as $user) {
                $user_id = $user['user_info']['user_id'];
                $user_name = $user['user_info']['user_name'];
                if($user['user_info']['permission'] == 0){
                    $permission = "一般";
                }else if($user['user_info']['permission'] == 1){
                    $permission = "一般管理者";
                }else if($user['user_info']['permission'] == 2){
                    $permission = "管理者";
                }

                if($user['user_info']['approval'] == 0){
                    $approval = "未承認";
                }else if($user['user_info']['approval'] == 1){
                    $approval = "承認";
                }

                if($user['user_info']['delete'] == 0){
                    $deleted = "-";
                }else if($user['user_info']['delete'] == 1){
                    $deleted = "削除";
                }

                $groups_name = "-";
                $group_leader = "-";

                foreach ($user['group_info'] as $group) {
                    $groups_name = $group['groups_id'];
                    $group_leader = $group['leader'];
                }

                echo "<tr style='cursor: pointer;' onclick=\"window.location='/Interfaces/Views/Pages/admin/admin_user_update.php?screen=user&user_id={$user_id}'\">";
                echo "<td>$user_id</td>";
                echo "<td>$user_name</td>";
                echo "<td>$permission</td>";
                echo "<td>$approval</td>";
                echo "<td>$deleted</td>";
                echo "<td>$groups_name</td>";
                echo "<td>$group_leader</td>";
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
                    <a class="page-link" href="?page=1">最初</a>
                </li>
                <li class="page-item">
                    <a class="page-link" href="?page=<?php echo $page - 1; ?>">前</a>
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
                    <a class="page-link" href="?page=<?php echo $i; ?>"><?php echo $i; ?></a>
                </li>
            <?php endfor; ?>

            <?php if ($page < $total_pages): ?>
                <li class="page-item">
                    <a class="page-link" href="?page=<?php echo $page + 1; ?>">次</a>
                </li>
                <li class="page-item">
                    <a class="page-link" href="?page=<?php echo $total_pages; ?>">最後</a>
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

<script src="/Interfaces/Assets/JS/setToday.js"></script>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>

<script>
    document.addEventListener('DOMContentLoaded', function () {
        const searchTypeRadios = document.querySelectorAll('input[name="search_type"]');
        const userIdField = document.getElementById('userIdField');
        const userNameField = document.getElementById('userNameField');
        const permissionField = document.getElementById('permissionField');
        const approvalField = document.getElementById('approvalField');
        const deletedField = document.getElementById('deletedField');

        // 初期表示
        toggleFields();

        function toggleFields() {
            const selectedValue = document.querySelector('input[name="search_type"]:checked').value;

            if (selectedValue === 'user_id') {
                userIdField.style.display = 'block';
                userNameField.style.display = 'none';
                permissionField.style.display = 'none';
                approvalField.style.display = 'none';
                deletedField.style.display = 'none';
            } else if (selectedValue === 'wildcard') {
                userIdField.style.display = 'none';
                userNameField.style.display = 'block';
                permissionField.style.display = 'block';
                approvalField.style.display = 'block';
                deletedField.style.display = 'block';
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


<!-- bootstrap-datepickerのjavascriptコード -->
<script>
    $('#sample1').datepicker();
</script>
</body>
</html>



