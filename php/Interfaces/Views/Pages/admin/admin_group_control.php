<?php
if (session_status() === PHP_SESSION_NONE) {
    session_start();
}
require $_SERVER['DOCUMENT_ROOT'] . '/config/config.php';
require $_SERVER['DOCUMENT_ROOT'] . '/Application/Services/ApiService.php';
require $_SERVER['DOCUMENT_ROOT'] . '/Interfaces/Views/Partials/requireApi.php';


$screen_title = "所属グループ管理";

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
if (!isset($_SESSION['group_search_type_flg'])) {
    $_SESSION['group_search_type_flg'] = "groups_id";
}
if (!isset($_SESSION['group_count'])) {
    $_SESSION['group_count'] = 0;
}
if (!isset($_SESSION['groups_id'])) {
    $_SESSION['groups_id'] = null;
}
if (!isset($_SESSION['group_name'])) {
    $_SESSION['group_name'] = null;
}


// マスター設定
$master_setting_api_refer_result = apiCallMasterSettingRefer();
$master_settings = [];
foreach ($master_setting_api_refer_result['data']['setting_list'] as $setting) {
    $master_settings[$setting['setting_key']] = $setting['setting_value'];
}
$master_setting_admin_groupdata_view = $master_settings['admin_groupdata_view'] ?? null;

// 1ページに表示する件数
$limit = $master_setting_admin_groupdata_view;

// 現在のページ番号
$page = isset($_GET['page']) ? (int)$_GET['page'] : 1;
$offset = ($page - 1) * $limit;

// ボタン押下時の処理
if (isset($_POST['entry'])) {
    $entry_button_click_flg = True;

    $_SESSION['group_search_type_flg'] = $_POST['search_type'];

    $_SESSION['groups_id'] = null;
    $_SESSION['group_name'] = null;

    if ($_SESSION['group_search_type_flg'] == "groups_id") {
        if (isset($_POST['groups_id'])) {
            if ($_POST['groups_id'] != "") {
                $_SESSION['groups_id'] = $_POST['groups_id'];
            }
        }
    }
    if ($_SESSION['group_search_type_flg'] == "wildcard") {
        if (isset($_POST['group_name'])) {
            if ($_POST['group_name'] != "") {
                $_SESSION['group_name'] = $_POST['group_name'];
            }
        }
    }

    $message = "ユーザー検索条件を変更しました";

}

// ユーザー情報を取得する際に、limit と offset を使用する
$group_api_count_result = apiCallGroupCount($_SESSION['groups_id']);
if($group_api_count_result['status'] != "error"){
    $group_api_refer_result = apiCallGroupRefer($_SESSION['groups_id'], $offset, $limit);
    $group_data = $group_api_refer_result['data']['group'];
}

// ユーザーの総数を取得
$total_users = $group_api_count_result['data']['recode_count'];

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
            所属グループの一覧を管理します
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
                                    <input class="form-check-input" type="radio" name="search_type" id="searchGroupId"
                                           value="groups_id"
                                        <?= ($_SESSION['group_search_type_flg'] === "groups_id" || !isset($_SESSION['group_search_type_flg'])) ? 'checked' : '' ?>>
                                    <label class="form-check-label" for="searchGroupId">所属グループIDで検索</label>
                                </div>
                                <div style='display:none' class="form-check">
                                    <input class="form-check-input" type="radio" name="search_type" id="searchGroupName"
                                           value="wildcard"
                                        <?= ($_SESSION['group_search_type_flg'] === "wildcard") ? 'checked' : '' ?>>
                                    <label class="form-check-label" for="searchGroupName">曖昧検索</label>
                                </div>
                            </div>
                        </div>

                        <!-- グループID -->
                        <div class="form-item remarks-item" id="groupIdField">
                            <div class="form-item-label">
                                <label class="item-label">所属グループID</label>
                            </div>
                            <div class="input-group form-item">
                                <input type="text" class="form-control" name="groups_id"
                                       value="<?= $_POST['groups_id'] ?? ($_SESSION['groups_id'] ?? '') ?>">
                            </div>
                        </div>

                        <!-- グループ名 -->
                        <div class="form-item remarks-item" id="groupNameField">
                            <div class="form-item-label">
                                <label class="item-label">所属グループ名</label>
                            </div>
                            <div class="input-group form-item">
                                <input type="text" class="form-control" name="group_name"
                                       value="<?= $_POST['group_name'] ?? ($_SESSION['group_name'] ?? '') ?>">
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
                <th>所属グループID</th>
                <th>所属グループ名</th>
            </tr>
            </thead>
            <tbody>
            <?php

            if(!isset($group_data)){
                $group_data=[];
            }
            foreach ($group_data as $group) {
                $group_id = $group['group_list']['id'];
                $groups_id = $group['group_list']['groups_id'];
                $group_name = $group['group_list']['group_name'];

                echo "<tr style='cursor: pointer;' onclick=\"window.location='/Interfaces/Views/Pages/admin/admin_group_update.php?screen=group&user_id={$user_id}&groups_id={$groups_id}'\">";
                echo "<td>$group_id</td>";
                echo "<td>$groups_id</td>";
                echo "<td>$group_name</td>";
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
        const groupIdField = document.getElementById('groupIdField');
        const groupNameField = document.getElementById('groupNameField');

        // 初期表示
        toggleFields();

        function toggleFields() {
            const selectedValue = document.querySelector('input[name="search_type"]:checked').value;

            if (selectedValue === 'groups_id') {
                groupIdField.style.display = 'block';
                groupNameField.style.display = 'none';
            } else if (selectedValue === 'wildcard') {
                groupIdField.style.display = 'none';
                groupNameField.style.display = 'block';
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



