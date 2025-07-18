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

    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script src="https://code.jquery.com/ui/1.13.2/jquery-ui.min.js"></script>
    <link rel="stylesheet" href="https://code.jquery.com/ui/1.13.2/themes/base/jquery-ui.css">
    <style>
        .sortable-placeholder {
            background-color: #f0f0f0;
            border: 2px dashed #aaa;
            height: 40px;
        }
    </style>
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

// ボタン押下時の処理
if (isset($_POST['change_btn'])) {
    $entry_button_click_flg = True;
    $suc_cnt = 0;
    $err_cnt = 0;
    if ($_SERVER['REQUEST_METHOD'] === 'POST' && isset($_POST['selected_datas'])) {
        $selected_datas = $_POST['selected_datas'];

        $count_result = apiCallMemberCount(null, $_SESSION['user_groups_id']);
        $member_max_count = $count_result['data']['recode_count'];
        $member_checked_count = count($selected_datas);

        if ($member_max_count == $member_checked_count) {
            $cnt = 0;
            foreach ($selected_datas as $data) {

                $array = explode("\t", $data);
                $post_member_id = $array[0];
                $post_groups_id = $array[1];
                $post_member_no = $array[2];
                $post_member_name = $array[3];
                $post_delete_flag = $array[4];

                if ($post_member_no != 999) {
                    $cnt++;
                    apiCallMemberUpdate($post_member_id, $cnt);
                    $suc_cnt += 1;
                }
            }

            $message = $suc_cnt . "件のメンバーを入替ました<br>" . $err_cnt . "件のメンバーをスキップしました";
            createLogs(LOG_TYPE_INFO, "メンバー入替 - 成功：{$suc_cnt}件, スキップ：{$err_cnt}件");

        } else {
            $message = "メンバーがすべて選択されていません";
            $entry_error = true;
            createLogs(LOG_TYPE_ERROR, "メンバー入替 - メンバー選択数不一致");
        }
    } else {
        $message = "メンバーが選択されていません";
        $entry_error = true;
        createLogs(LOG_TYPE_ERROR, "メンバー入替 - メンバー未選択");
    }
}

if (isset($_POST['on_btn'])) {
    $entry_button_click_flg = True;
    $suc_cnt = 0;
    $err_cnt = 0;
    if ($_SERVER['REQUEST_METHOD'] === 'POST' && isset($_POST['selected_datas'])) {
        $selected_datas = $_POST['selected_datas'];

        foreach ($selected_datas as $data) {
            $array = explode("\t", $data);

            $post_member_id = $array[0];
            $post_groups_id = $array[1];
            $post_member_no = $array[2];
            $post_member_name = $array[3];
            $post_delete_flag = $array[4];

            if ($post_delete_flag == "0") {
                $err_cnt += 1;
                continue;
            }

            $member_max_no = getMaxMemberNo($_SESSION['user_groups_id']);
            $member_new_no = $member_max_no + 1;

            apiCallMemberUnDisable($post_member_id);
            apiCallMemberUpdate($post_member_id, $member_new_no);
            $suc_cnt += 1;
        }

        $message = $suc_cnt . "件のメンバーを有効化しました<br>" . $err_cnt . "件のメンバーをスキップしました";
        createLogs(LOG_TYPE_INFO, "メンバー有効化 - 成功：{$suc_cnt}件, スキップ：{$err_cnt}件");

    } else {
        $message = "メンバーが選択されていません";
        $entry_error = true;
        createLogs(LOG_TYPE_ERROR, "メンバー有効化 - メンバー未選択");
    }
}

if (isset($_POST['off_btn'])) {
    $entry_button_click_flg = True;
    $suc_cnt = 0;
    $err_cnt = 0;
    if ($_SERVER['REQUEST_METHOD'] === 'POST' && isset($_POST['selected_datas'])) {
        $selected_datas = $_POST['selected_datas'];

        foreach ($selected_datas as $data) {
            $array = explode("\t", $data);

            $post_member_id = $array[0];
            $post_groups_id = $array[1];
            $post_member_no = $array[2];
            $post_member_name = $array[3];
            $post_delete_flag = $array[4];

            if ($post_delete_flag == "1") {
                $err_cnt += 1;
                continue;
            }
            apiCallMemberDisable($post_member_id);
            apiCallMemberUpdate($post_member_id, 999);
            $suc_cnt += 1;
        }

        $message = $suc_cnt . "件のメンバーを無効化しました<br>" . $err_cnt . "件のメンバーをスキップしました";
        createLogs(LOG_TYPE_INFO, "メンバー無効化 - 成功：{$suc_cnt}件, スキップ：{$err_cnt}件");

    } else {
        $message = "メンバーが選択されていません";
        $entry_error = true;
        createLogs(LOG_TYPE_ERROR, "メンバー無効化 - メンバー未選択");
    }
}

if (isset($_POST['delete_btn'])) {
    $entry_button_click_flg = True;
    $suc_cnt = 0;
    $err_cnt = 0;
    if ($_SERVER['REQUEST_METHOD'] === 'POST' && isset($_POST['selected_datas'])) {
        $selected_datas = $_POST['selected_datas'];

        foreach ($selected_datas as $data) {
            $array = explode("\t", $data);

            $post_member_id = $array[0];
            $post_groups_id = $array[1];
            $post_member_no = $array[2];
            $post_member_name = $array[3];
            $post_delete_flag = $array[4];

            if ($post_delete_flag == "0") {
                $err_cnt += 1;
                continue;
            }
            apiCallMemberDelete(null, $post_member_id);
            $suc_cnt += 1;
        }

        $message = $suc_cnt . "件のメンバーを削除しました<br>" . $err_cnt . "件のメンバーをスキップしました";
        createLogs(LOG_TYPE_INFO, "メンバー削除 - 成功：{$suc_cnt}件, スキップ：{$err_cnt}件");

    } else {
        $message = "メンバーが選択されていません";
        $entry_error = true;
        createLogs(LOG_TYPE_ERROR, "メンバー削除 - メンバー未選択");
    }
}

$member_api_refer_result = apiCallMemberRefer(null, $_SESSION['user_groups_id']);
if ($member_api_refer_result['status'] != "error") {
    $members_data = $member_api_refer_result['data']['member_list'];
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
                                     href='/Interfaces/Views/Pages/user/group/setting/member/user_group_member_entry.php'>メンバー登録</a>
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
                <div class="settings">


                </div>
            </div>
            <div class="right">
            </div>
        </div>
    </div>
</main>


<form method="post" action="">

    <div class="btn-area">
        <div class="btn-left-area">

        </div>
        <div class="btn-right-area">
            <div class="btn-item">
                <button type="submit" class="btn btn-primary" name="change_btn"
                        onclick="return checkAllAndConfirm();">
                    入替
                </button>
            </div>
            <div class="btn-item">
                <button type="submit" class="btn btn-primary" name="on_btn"
                        onclick="return confirm('本当に実行しますか？\n以下のメンバーはスキップされます。\n・ 既に有効化のメンバー')">
                    有効化
                </button>
            </div>
            <div class="btn-item">
                <button type="submit" class="btn btn-warning" name="off_btn"
                        onclick="return confirm('本当に実行しますか？\n以下のメンバーはスキップされます。\n・ 既に無効化のメンバー')">
                    無効化
                </button>
            </div>
            <div class="btn-item">
                <button type="submit" class="btn btn-danger" name="delete_btn"
                        onclick="return confirm('本当に実行しますか？\n以下のメンバーはスキップされます。\n・ 有効化のメンバー')">
                    削除
                </button>
            </div>
        </div>
    </div>

    <div class="table-main">
        ※行をドラッグすると順番を入れ替えられます
    </div>
    <div class="table-main">
        <div class="table-area">
            <table class="table table-light table-striped table-bordered table-hover">
                <thead class="table-dark">
                <tr>
                    <th><input type="checkbox" id="select-all" onclick="toggleAll(this)"></th>
                    <th>#</th>
                    <th>メンバー名</th>
                    <th>ステータス</th>
                </tr>
                </thead>
                <tbody id="sortable-table">
                <?php
                if (!isset($members_data)) {
                    $members_data = [];
                }
                foreach ($members_data as $category) {
                    $member_id = $category['member_id'];
                    $groups_id = $category['groups_id'];
                    $member_no_value = $category['member_no'];
                    $member_name = $category['member_name'];
                    $delete_flag_value = $category['delete_flag'];

                    if ($delete_flag_value == 0) {
                        $delete_flag = "有効";
                    } else {
                        $delete_flag = "無効";
                    }

                    if ($member_no_value == 999) {
                        $member_no = "-";
                    } else {
                        $member_no = $member_no_value;
                    }

                    echo "<tr>";
                    echo "<td onclick='event.stopPropagation(); toggleCheckbox(this)'>
        <input type='checkbox'
               name='selected_datas[]'
               value='{$member_id}\t{$groups_id}\t{$member_no_value}\t{$member_name}\t{$delete_flag_value}'
               style='pointer-events: none;'></td>";

                    echo "
                <td>$member_no</td>
                ";
                    echo "
                <td><a href='/Interfaces/Views/Pages/user/group/setting/member/user_group_member_update.php?member_id={$member_id}'>$member_name</a></td>
                ";
                    echo "
                <td>$delete_flag</td>
                ";
                    echo "</tr>";
                }
                ?>
                </tbody>
            </table>
        </div>
    </div>
</form>

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
        const checkboxes = document.querySelectorAll('input[name="selected_datas[]"]');
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
    $("#sortable-table").sortable({
        helper: function (e, tr) {
            const $originals = tr.children();
            const $helper = tr.clone();
            $helper.children().each(function (index) {
                $(this).width($originals.eq(index).width());
            });
            return $helper;
        },
        cursor: "move",
        placeholder: "sortable-placeholder",
        items: "tr",
        update: function (event, ui) {
            // 並び順変更後の確認
            console.log("New order:");
            $("#sortable-table tr").each(function (index) {
                const categoryId = $(this).find("input[type=checkbox]").val().split('\t')[0];
                console.log(index + 1 + ": " + categoryId);
            });
        }
    }).disableSelection();
</script>
<script>
    function checkAllAndConfirm() {
        const checkboxes = document.querySelectorAll('input[name="selected_datas[]"]');
        checkboxes.forEach(cb => cb.checked = true);
        const ok = confirm('順番を入れ替えますか？\n※入替を行うと番号が連番になるように整形されます');
        if (!ok) {
            return false;
        }
        return true;
    }
</script>

<?php
require $_SERVER['DOCUMENT_ROOT'] . '/Interfaces/Assets/JS/basic_js.php';
?>

</body>
</html>



