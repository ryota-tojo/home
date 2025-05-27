<?php
if (session_status() === PHP_SESSION_NONE) {
    session_start();
}

require $_SERVER['DOCUMENT_ROOT'] . '/config/config.php';
require $_SERVER['DOCUMENT_ROOT'] . '/Application/Services/api_service.php';
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
    $_SESSION['search_permission'] = -1;
}
if (!isset($_SESSION['search_approval'])) {
    $_SESSION['search_approval'] = -1;
}
if (!isset($_SESSION['search_deleted'])) {
    $_SESSION['search_deleted'] = -1;
}
if (!isset($_SESSION['search_group_affiliation'])) {
    $_SESSION['search_group_affiliation'] = -1;
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
    $_SESSION['search_permission'] = -1;
    $_SESSION['search_approval'] = -1;
    $_SESSION['search_deleted'] = -1;
    $_SESSION['search_group_affiliation'] = -1;

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
        if (isset($_POST['group_affiliation'])) {
            if ($_POST['group_affiliation'] != "-1") {
                $_SESSION['search_group_affiliation'] = $_POST['group_affiliation'];
            }
        }
    }

    $message = "ユーザー検索条件を変更しました";

}
if (isset($_POST['reset'])) {
    $_SESSION['search_user_id'] = null;
    $_SESSION['search_user_name'] = null;
    $_SESSION['search_permission'] = -1;
    $_SESSION['search_approval'] = -1;
    $_SESSION['search_deleted'] = -1;
    $_SESSION['search_group_affiliation'] = -1;
    $_SESSION['user_search_type_flg'] = "user_id";

    $message = "ユーザー検索条件をリセットしました";
}
if (isset($_POST['user_approval'])) {
    $entry_button_click_flg = True;
    $suc_cnt = 0;
    $err_cnt = 0;
    if ($_SERVER['REQUEST_METHOD'] === 'POST' && isset($_POST['selected_users'])) {
        $selected_users = $_POST['selected_users'];

        foreach ($selected_users as $user) {
            $array = explode("\t", $user);
            $post_user_id = $array[0];
            $post_permission = $array[1];
            $post_approval = $array[2];
            $post_deleted = $array[3];
            $post_leader_flg = $array[4];

            if ($post_permission != "0") {
                $err_cnt += 1;
                continue;
            }
            if ($post_approval == "1") {
                $err_cnt += 1;
                continue;
            }
            if ($post_deleted == "1") {
                $err_cnt += 1;
                continue;
            }
            if ($post_leader_flg == "1") {
                $err_cnt += 1;
                continue;
            }
            apiCallUserUpdateInfo($post_user_id, null, null, null, 1, null);
            $suc_cnt += 1;
        }

        $message = $suc_cnt . "件のユーザーを承認しました<br>" . $err_cnt . "件のユーザーをスキップしました";

    } else {
        $message = "ユーザーが選択されていません";
        $entry_error = true;
    }
}

if (isset($_POST['user_un_approval'])) {
    $entry_button_click_flg = True;
    $suc_cnt = 0;
    $err_cnt = 0;
    if ($_SERVER['REQUEST_METHOD'] === 'POST' && isset($_POST['selected_users'])) {
        $selected_users = $_POST['selected_users'];

        foreach ($selected_users as $user) {
            $array = explode("\t", $user);
            $post_user_id = $array[0];
            $post_permission = $array[1];
            $post_approval = $array[2];
            $post_deleted = $array[3];
            $post_leader_flg = $array[4];

            if ($post_permission != "0") {
                $err_cnt += 1;
                continue;
            }
            if ($post_approval == "0") {
                $err_cnt += 1;
                continue;
            }
            if ($post_deleted == "1") {
                $err_cnt += 1;
                continue;
            }
            if ($post_leader_flg == "1") {
                $err_cnt += 1;
                continue;
            }
            apiCallUserUpdateInfo($post_user_id, null, null, null, 0, null);
            $suc_cnt += 1;
        }

        $message = $suc_cnt . "件のユーザーを否認しました<br>" . $err_cnt . "件のユーザーをスキップしました";

    } else {
        $message = "ユーザーが選択されていません";
        $entry_error = true;
    }
}
if (isset($_POST['user_deleted'])) {
    $entry_button_click_flg = True;
    $suc_cnt = 0;
    $err_cnt = 0;
    if ($_SERVER['REQUEST_METHOD'] === 'POST' && isset($_POST['selected_users'])) {
        $selected_users = $_POST['selected_users'];

        foreach ($selected_users as $user) {
            $array = explode("\t", $user);
            $post_user_id = $array[0];
            $post_permission = $array[1];
            $post_approval = $array[2];
            $post_deleted = $array[3];
            $post_leader_flg = $array[4];

            if ($post_permission != "0") {
                $err_cnt += 1;
                continue;
            }
            if ($post_approval == "1") {
                $err_cnt += 1;
                continue;
            }
            if ($post_deleted == "1") {
                $err_cnt += 1;
                continue;
            }
            if ($post_leader_flg == "1") {
                $err_cnt += 1;
                continue;
            }
            apiCallUserUpdateInfo($post_user_id, null, null, null, null, 1);
            $suc_cnt += 1;
        }

        $message = $suc_cnt . "件のユーザーを削除しました<br>" . $err_cnt . "件のユーザーをスキップしました";

    } else {
        $message = "ユーザーが選択されていません";
        $entry_error = true;
    }
}
if (isset($_POST['user_un_deleted'])) {
    $entry_button_click_flg = True;
    $suc_cnt = 0;
    $err_cnt = 0;
    if ($_SERVER['REQUEST_METHOD'] === 'POST' && isset($_POST['selected_users'])) {
        $selected_users = $_POST['selected_users'];

        foreach ($selected_users as $user) {
            $array = explode("\t", $user);
            $post_user_id = $array[0];
            $post_permission = $array[1];
            $post_approval = $array[2];
            $post_deleted = $array[3];
            $post_leader_flg = $array[4];

            if ($post_permission != "0") {
                $err_cnt += 1;
                continue;
            }
            if ($post_deleted == "0") {
                $err_cnt += 1;
                continue;
            }
            if ($post_leader_flg == "1") {
                $err_cnt += 1;
                continue;
            }
            apiCallUserUpdateInfo($post_user_id, null, null, null, null, 0);
            $suc_cnt += 1;
        }

        $message = $suc_cnt . "件のユーザーを削除解除しました<br>" . $err_cnt . "件のユーザーをスキップしました";

    } else {
        $message = "ユーザーが選択されていません";
        $entry_error = true;
    }
}

// ユーザー情報を取得する際に、limit と offset を使用する

$permission_param = ($_SESSION['search_permission'] == -1) ? null : $_SESSION['search_permission'];
$approval_param = ($_SESSION['search_approval'] == -1) ? null : $_SESSION['search_approval'];
$deleted_param = ($_SESSION['search_deleted'] == -1) ? null : $_SESSION['search_deleted'];
$group_affiliation_param = ($_SESSION['search_group_affiliation'] == -1) ? null : $_SESSION['search_group_affiliation'];

$userinfo_api_count_result = apiCallUserCount($_SESSION['search_user_id'], $_SESSION['search_user_name'], $permission_param, $approval_param, $deleted_param, $group_affiliation_param);
if ($userinfo_api_count_result['status'] != "error") {
    $userinfo_api_refer_result = apiCallUserRefer($_SESSION['search_user_id'], $_SESSION['search_user_name'], $permission_param, $approval_param, $deleted_param, $group_affiliation_param, $offset, $limit);
    if ($userinfo_api_refer_result['status'] != "error") {
        $users_data = $userinfo_api_refer_result['data']['user'];
    }
}

// ユーザーの総数を取得
$total_users = $userinfo_api_count_result['data']['recode_count'] ?? 0;

// 総ページ数を計算
$total_pages = ceil($total_users / $limit);

?>

<main>
    <div class="title-area">
        <h2 class="title"><?php echo $screen_title; ?></h2>
    </div>
    <div class="summary-area">
        <div class="summary">
            ユーザー情報を管理します
        </div>
    </div>

    <div class="btn-area">
        <div class="btn-center-area">
            <?php
            //
            if ($user_file_flag or $screen == "user") {
                echo "<div class='btn-item'><a class='link-btn' href='/Interfaces/Views/Pages/admin/admin_user_entry.php?$url_param'>ユーザー登録</a></div>";
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
                <div class="btn-area">
                    <div class="btn-center-area">
                        <div class="btn-item">
                            <button type="button" class="btn btn-secondary" data-bs-toggle="modal"
                                    data-bs-target="#staticBackdrop">
                                🔍 検索フォームを表示/非表示
                            </button>
                        </div>
                    </div>
                </div>

                <!-- Modal -->
                <div class="modal fade" id="staticBackdrop" data-bs-backdrop="static" data-bs-keyboard="false"
                     tabindex="-1" aria-labelledby="staticBackdropLabel" aria-hidden="true">
                    <div class="modal-dialog modal-dialog-centered">
                        <div class="modal-content">
                            <div class="modal-header">
                                <h1 class="modal-title fs-5" id="staticBackdropLabel">検索フォーム</h1>
                                <button type="button" class="btn-close" data-bs-dismiss="modal"
                                        aria-label="Close"></button>
                            </div>
                            <div class="modal-body">

                                <form action="" method="post">

                                    <!-- 設定切替 -->
                                    <div class="form-item payment-item">
                                        <div class="form-item-label">
                                            <label class="item-label">検索方法</label>
                                        </div>
                                        <div class="input-group form-item">
                                            <div class="form-check">
                                                <input class="form-check-input" type="radio" name="search_type"
                                                       id="searchUserId"
                                                       value="user_id"
                                                    <?= ($_SESSION['user_search_type_flg'] === "user_id" || !isset($_SESSION['user_search_type_flg'])) ? 'checked' : '' ?>>
                                                <label class="form-check-label"
                                                       for="searchUserId">ユーザーIDで検索</label>
                                            </div>
                                            <div class="form-check">
                                                <input class="form-check-input" type="radio" name="search_type"
                                                       id="searchUserName"
                                                       value="wildcard"
                                                    <?= ($_SESSION['user_search_type_flg'] === "wildcard") ? 'checked' : '' ?>>
                                                <label class="form-check-label"
                                                       for="searchUserName">ユーザー情報で検索</label>
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
                                                   value="<?php if ($_SESSION['search_user_id'] == null) {
                                                       echo "";
                                                   } else {
                                                       echo $_SESSION['search_user_id'];
                                                   } ?>"
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
                                                <option value="-1" <?= ($_SESSION['search_permission'] == -1 || !isset($_SESSION['search_permission'])) ? 'selected' : '' ?>>
                                                    すべて
                                                </option>
                                                <option value="0" <?= ($_SESSION['search_permission'] == 0) ? 'selected' : '' ?>>
                                                    一般
                                                </option>
                                                <option value="1" <?= ($_SESSION['search_permission'] == 1) ? 'selected' : '' ?>>
                                                    一般管理者
                                                </option>
                                                <option value="2" <?= ($_SESSION['search_permission'] == 2) ? 'selected' : '' ?>>
                                                    管理者
                                                </option>
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
                                                <option value="-1" <?= ($_SESSION['search_approval'] == -1 || !isset($_SESSION['search_approval'])) ? 'selected' : '' ?>>
                                                    すべて
                                                </option>
                                                <option value="0" <?= ($_SESSION['search_approval'] == 0) ? 'selected' : '' ?>>
                                                    未承認
                                                </option>
                                                <option value="1" <?= ($_SESSION['search_approval'] == 1) ? 'selected' : '' ?>>
                                                    承認
                                                </option>
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
                                                <option value="-1" <?= ($_SESSION['search_deleted'] == -1 || !isset($_SESSION['search_deleted'])) ? 'selected' : '' ?>>
                                                    すべて
                                                </option>
                                                <option value="0" <?= ($_SESSION['search_deleted'] == 0) ? 'selected' : '' ?>>
                                                    未削除
                                                </option>
                                                <option value="1" <?= ($_SESSION['search_deleted'] == 1) ? 'selected' : '' ?>>
                                                    削除済
                                                </option>
                                            </select>
                                        </div>
                                        <!-- 所属グループ -->
                                        <div class="form-item payment-item" id="groupAffiliationField">
                                            <div class="form-item-label">
                                                <label class="item-label">所属グループ</label>
                                            </div>
                                            <div class="input-group form-item">
                                                <select class="form-select" name="group_affiliation">
                                                    <option value="-1" <?= ($_SESSION['search_group_affiliation'] == -1 || !isset($_SESSION['search_group_affiliation'])) ? 'selected' : '' ?>>
                                                        すべて
                                                    </option>
                                                    <option value="0" <?= ($_SESSION['search_group_affiliation'] == 0) ? 'selected' : '' ?>>
                                                        未所属
                                                    </option>
                                                    <option value="1" <?= ($_SESSION['search_group_affiliation'] == 1) ? 'selected' : '' ?>>
                                                        所属済
                                                    </option>
                                                </select>
                                            </div>
                                        </div>
                                    </div>

                                    <!-- 登録ボタン-->
                                    <div class="btn-area">
                                        <div class="modal-btn-left-area">

                                        </div>
                                        <div class="modal-btn-right-area">
                                            <div class="btn-item">
                                                <button type="submit" class="btn btn-primary" name="entry">
                                                    検索
                                                </button>
                                            </div>
                                            <div class="btn-item">
                                                <button type="submit" class="btn btn-warning" name="reset">
                                                    リセット
                                                </button>
                                            </div>
                                        </div>
                                    </div>
                                </form>
                            </div>
                            <div class="modal-footer">
                                <div class="btn-area">
                                    <div class="modal-btn-center-area">
                                        <div class="btn-item">
                                            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">
                                                閉じる
                                            </button>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
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

        </div>
        <div class="btn-right-area">
            <div class="btn-item">
                <button type="submit" class="btn btn-primary" name="user_approval"
                        onclick="return confirm('本当に実行しますか？\n以下のユーザーはスキップされます。\n・ 所属グループのリーダーユーザー\n・ 権限「0」以外のユーザー\n・ 既に承認済みのユーザー\n・ 削除済みのユーザー')">
                    選択ユーザーを承認
                </button>
            </div>
            <div class="btn-item">
                <button type="submit" class="btn btn-warning" name="user_un_approval"
                        onclick="return confirm('本当に実行しますか？\n以下のユーザーはスキップされます。\n・ 所属グループのリーダーユーザー\n・ 権限「0」以外のユーザー\n・ 既に未承認のユーザー\n・ 削除済みのユーザー')">
                    選択ユーザーを否認
                </button>
            </div>
            <div class="btn-item">
                <button type="submit" class="btn btn-danger" name="user_deleted"
                        onclick="return confirm('本当に実行しますか？\n以下のユーザーはスキップされます。\n・ 所属グループのリーダーユーザー\n・ 権限「0」以外のユーザー\n・ 承認済みのユーザー\n・ 削除済みのユーザー')">
                    選択ユーザーを削除
                </button>
            </div>
            <div class="btn-item">
                <button type="submit" class="btn btn-secondary" name="user_un_deleted"
                        onclick="return confirm('本当に実行しますか？\n以下のユーザーはスキップされます。\n・ 所属グループのリーダーユーザー\n・ 権限「0」以外のユーザー\n・ 未削除のユーザー')">
                    選択ユーザーを削除解除
                </button>
            </div>
        </div>
    </div>

    <div class="table-main">
        <div class="table-area">
            <table class="table table-light table-striped table-bordered table-hover">
                <thead class="table-dark">
                <tr>
                    <th onclick="toggleAllFromTh(this)">
                        <input type="checkbox" id="select-all"
                               onclick="event.stopPropagation(); toggleAll(this);"/>
                    </th>
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

                if (!isset($users_data)) {
                    $users_data = [];
                }
                foreach ($users_data

                         as $user) {
                    $user_id = $user['user_info']['user_id'];
                    $user_name = $user['user_info']['user_name'];
                    $permission_value = $user['user_info']['permission'];
                    $approval_value = $user['user_info']['approval'];
                    $deleted_value = $user['user_info']['delete'];

                    if ($permission_value == 0) {
                        $permission = "一般";
                    } else if ($permission_value == 1) {
                        $permission = "一般管理者";
                    } else if ($permission_value == 2) {
                        $permission = "管理者";
                    }

                    if ($approval_value == 0) {
                        $approval = "未承認";
                    } else if ($approval_value == 1) {
                        $approval = "承認";
                    }

                    if ($deleted_value == 0) {
                        $deleted = "-";
                    } else if ($deleted_value == 1) {
                        $deleted = "削除";
                    }

                    $groups_name = "-";
                    $group_leader = "-";

                    foreach ($user['group_info'] as $group) {
                        $groups_name = $group['groups_id'];
                        $group_leader = $group['leader'];
                    }

                    echo "<tr style='cursor: pointer;' onclick=\"window.location='/Interfaces/Views/Pages/admin/admin_user_update.php?screen=user&user_id={$user_id}'\">";
                    echo "<td onclick='event.stopPropagation(); toggleCheckbox(this)'>
        <input type='checkbox'
               name='selected_users[]'
               value='{$user_id}\t{$permission_value}\t{$approval_value}\t{$deleted_value}\t{$group_leader}'
               style='pointer-events: none;'></td>";

                    echo "
                <td>$user_id</td>
                ";
                    echo "
                <td>$user_name</td>
                ";
                    echo "
                <td>$permission</td>
                ";
                    echo "
                <td>$approval</td>
                ";
                    echo "
                <td>$deleted</td>
                ";
                    echo "
                <td>$groups_name</td>
                ";
                    echo "
                <td>$group_leader</td>
                ";
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
        const permissionField = document.getElementById('permissionField');
        const approvalField = document.getElementById('approvalField');
        const deletedField = document.getElementById('deletedField');
        const groupAffiliationField = document.getElementById('groupAffiliationField');

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
                groupAffiliationField.style.display = 'none';
            } else if (selectedValue === 'wildcard') {
                userIdField.style.display = 'none';
                userNameField.style.display = 'block';
                permissionField.style.display = 'block';
                approvalField.style.display = 'block';
                deletedField.style.display = 'block';
                groupAffiliationField.style.display = 'block';
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



