<?php
if (session_status() === PHP_SESSION_NONE) {
    session_start();
}
require_once $_SERVER['DOCUMENT_ROOT'] . '/config/config.php';
require_once $_SERVER['DOCUMENT_ROOT'] . '/config/log_config.php';
require_once $_SERVER['DOCUMENT_ROOT'] . '/Application/Services/ApiService.php';
require_once $_SERVER['DOCUMENT_ROOT'] . '/Interfaces/Views/Partials/requireApi.php';
require_once $_SERVER['DOCUMENT_ROOT'] . '/Interfaces/Views/Partials/category/get_max_category_no.php';
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

        $count_result = apiCallCategoryCount(null, $_SESSION['user_groups_id']);
        $category_max_count = $count_result['data']['recode_count'];
        $category_cheched_count = count($selected_datas);

        if ($category_max_count == $category_cheched_count) {
            $cnt = 0;
            foreach ($selected_datas as $data) {

                $array = explode("\t", $data);
                $post_category_id = $array[0];
                $post_groups_id = $array[1];
                $post_category_no = $array[2];
                $post_category_name = $array[3];
                $post_delete_flag = $array[4];

                if ($post_category_no != 999) {
                    $cnt++;
                    apiCallCategoryUpdate($post_category_id, $cnt);
                    $suc_cnt += 1;
                }
            }

            $message = $suc_cnt . "件のカテゴリーを入替ました<br>" . $err_cnt . "件のカテゴリーをスキップしました";
            createLogs(LOG_TYPE_INFO, "カテゴリー入替 - 成功：{$suc_cnt}件, スキップ：{$err_cnt}件");

        } else {
            $message = "カテゴリーがすべて選択されていません";
            $entry_error = true;
            createLogs(LOG_TYPE_ERROR, "カテゴリー入替 - カテゴリー選択数不一致");
        }
    } else {
        $message = "カテゴリーが選択されていません";
        $entry_error = true;
        createLogs(LOG_TYPE_ERROR, "カテゴリー入替 - カテゴリー未選択");
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

            $post_category_id = $array[0];
            $post_groups_id = $array[1];
            $post_category_no = $array[2];
            $post_category_name = $array[3];
            $post_delete_flag = $array[4];

            if ($post_delete_flag == "0") {
                $err_cnt += 1;
                continue;
            }

            $category_max_no = getMaxCategoryNo($_SESSION['user_groups_id']);
            $category_new_no = $category_max_no + 1;

            apiCallCategoryUnDisable($post_category_id);
            apiCallCategoryUpdate($post_category_id, $category_new_no);
            $suc_cnt += 1;
        }

        $message = $suc_cnt . "件のカテゴリーを有効化しました<br>" . $err_cnt . "件のカテゴリーをスキップしました";
        createLogs(LOG_TYPE_INFO, "カテゴリー有効化 - 成功：{$suc_cnt}件, スキップ：{$err_cnt}件");

    } else {
        $message = "カテゴリーが選択されていません";
        $entry_error = true;
        createLogs(LOG_TYPE_ERROR, "カテゴリー有効化 - カテゴリー未選択");
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

            $post_category_id = $array[0];
            $post_groups_id = $array[1];
            $post_category_no = $array[2];
            $post_category_name = $array[3];
            $post_delete_flag = $array[4];

            if ($post_delete_flag == "1") {
                $err_cnt += 1;
                continue;
            }
            apiCallCategoryDisable($post_category_id);
            apiCallCategoryUpdate($post_category_id, 999);
            $suc_cnt += 1;
        }

        $message = $suc_cnt . "件のカテゴリーを無効化しました<br>" . $err_cnt . "件のカテゴリーをスキップしました";
        createLogs(LOG_TYPE_INFO, "カテゴリー無効化 - 成功：{$suc_cnt}件, スキップ：{$err_cnt}件");

    } else {
        $message = "カテゴリーが選択されていません";
        $entry_error = true;
        createLogs(LOG_TYPE_ERROR, "カテゴリー無効化 - カテゴリー未選択");
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

            $post_category_id = $array[0];
            $post_groups_id = $array[1];
            $post_category_no = $array[2];
            $post_category_name = $array[3];
            $post_delete_flag = $array[4];

            if ($post_delete_flag == "0") {
                $err_cnt += 1;
                continue;
            }
            apiCallCategoryDelete(null, $post_category_id);
            $suc_cnt += 1;
        }

        $message = $suc_cnt . "件のカテゴリーを削除しました<br>" . $err_cnt . "件のカテゴリーをスキップしました";
        createLogs(LOG_TYPE_INFO, "カテゴリー削除 - 成功：{$suc_cnt}件, スキップ：{$err_cnt}件");

    } else {
        $message = "カテゴリーが選択されていません";
        $entry_error = true;
        createLogs(LOG_TYPE_ERROR, "カテゴリー削除 - カテゴリー未選択");
    }
}

$category_api_refer_result = apiCallCategoryRefer(null, $_SESSION['user_groups_id']);
if ($category_api_refer_result['status'] != "error") {
    $categories_data = $category_api_refer_result['data']['category_list'];
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
                                     href='/Interfaces/Views/Pages/user/group/setting/category/user_group_category_entry.php'>カテゴリー登録</a>
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
                        onclick="return confirm('本当に実行しますか？\n以下のカテゴリーはスキップされます。\n・ 既に有効化のカテゴリー')">
                    有効化
                </button>
            </div>
            <div class="btn-item">
                <button type="submit" class="btn btn-warning" name="off_btn"
                        onclick="return confirm('本当に実行しますか？\n以下のカテゴリーはスキップされます。\n・ 既に無効化のカテゴリー')">
                    無効化
                </button>
            </div>
            <div class="btn-item">
                <button type="submit" class="btn btn-danger" name="delete_btn"
                        onclick="return confirm('本当に実行しますか？\n以下のカテゴリーはスキップされます。\n・ 有効化のカテゴリー')">
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
                    <th>カテゴリー名</th>
                    <th>ステータス</th>
                </tr>
                </thead>
                <tbody id="sortable-table">
                <?php
                if (!isset($categories_data)) {
                    $categories_data = [];
                }
                foreach ($categories_data as $category) {
                    $category_id = $category['category_id'];
                    $groups_id = $category['groups_id'];
                    $category_no_value = $category['category_no'];
                    $category_name = $category['category_name'];
                    $delete_flag_value = $category['delete_flag'];

                    if ($delete_flag_value == 0) {
                        $delete_flag = "有効";
                    } else {
                        $delete_flag = "無効";
                    }

                    if ($category_no_value == 999) {
                        $category_no = "-";
                    } else {
                        $category_no = $category_no_value;
                    }

                    echo "<tr>";
                    echo "<td onclick='event.stopPropagation(); toggleCheckbox(this)'>
        <input type='checkbox'
               name='selected_datas[]'
               value='{$category_id}\t{$groups_id}\t{$category_no_value}\t{$category_name}\t{$delete_flag_value}'
               style='pointer-events: none;'></td>";

                    echo "
                <td>$category_no</td>
                ";
                    echo "
                <td><a href='/Interfaces/Views/Pages/user/group/setting/category/user_group_category_update.php?category_id={$category_id}'>$category_name</a></td>
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



