<?php
if (session_status() === PHP_SESSION_NONE) {
    session_start();
}
require_once $_SERVER['DOCUMENT_ROOT'] . '/config/config.php';
require_once $_SERVER['DOCUMENT_ROOT'] . '/config/log_config.php';
require_once $_SERVER['DOCUMENT_ROOT'] . '/Application/Services/ApiService.php';
require_once $_SERVER['DOCUMENT_ROOT'] . '/Interfaces/Views/Partials/requireApi.php';
require_once $_SERVER['DOCUMENT_ROOT'] . '/Interfaces/Views/Partials/user/user_create.php';
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
// なし

// ボタン押下時の処理
if (isset($_POST['read_id'])) {
    $entry_button_click_flg = True;
    $read_flag = True;

    $user_id = $_POST['user_id'];

    $result = apiCallUserRefer($user_id, null, null, null, null, $_SESSION['user_groups_id']);
    if ($result['status'] == "error") {
        $message = 'ユーザー情報の取得に失敗しました。';
        $entry_error = True;
        createLogs(LOG_TYPE_ERROR, "ユーザー情報の取得に失敗しました。");
    } else {
        foreach ($result['data']['user'] as $data) {
            $message = 'ユーザー情報を取得しました。';
            $user_name = $data['user_info']['user_name'];
            createLogs(LOG_TYPE_ERROR, "ユーザー情報取得");
        }
    }
}


if (isset($_POST['entry'])) {
    $entry_button_click_flg = True;

    $user_id = $_POST['user_id'];

    $result = apiCallUserRefer($user_id, null, null, null, null, $_SESSION['user_groups_id']);
    if ($result['status'] == "error") {
        $message = 'ユーザー情報の取得に失敗しました。';
        $entry_error = True;
        createLogs(LOG_TYPE_ERROR, "ユーザー情報の取得に失敗しました。");
    } else {
        foreach ($result['data']['user'] as $data) {
            $user_name = $data['user_info']['user_name'];
        }

        $group_info_api_leader_change_result = apiCallGroupInfoLeaderChange($_SESSION['user_groups_id'], $_SESSION['user_id'], $user_id);
        $status = $group_info_api_leader_change_result['status'];

        if ($status == 'error') {
            $message = $group_info_api_leader_change_result['data']['message'];
            $entry_error = True;
            createLogs(LOG_TYPE_ERROR, "所属グループのリーダー変更に失敗");
        } else {
            $message = "所属グループのリーダーを変更しました";
            createLogs(LOG_TYPE_INFO, "所属グループのリーダーを変更");
            createLogs(LOG_TYPE_INFO, "画面遷移 -> ログアウト");
            echo "<script>window.location.href = '../../../../logout.php';</script>";
        }
    }
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
            <div class='btn-item'><a class='link-btn' href='/Interfaces/Views/Pages/user/group/setting/group_user/user_group_user_list.php'>ユーザー一覧</a></div>
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

                        <div class="pc-form">
                            <!-- ユーザー名 -->
                            <div class="form-item">
                                <div class="form-item-label">
                                    <label class="item-label">ユーザーID</label>
                                </div>
                                <div class="input-group form-item">
                                    <div class="date-form">
                                        <input required type="number" min=1 class="form-control" name="user_id"
                                            <?php
                                            $input_user_id = $user_id ?? '';
                                            if($entry_error == True or $read_flag == True){echo "value='{$input_user_id}'";} ?>
                                        >
                                    </div>
                                    <div class="date-btn-form">
                                        <button type="submit" class="btn btn-primary date-btn-item" name="read_id">
                                            読込
                                        </button>
                                    </div>
                                </div>

                                <hr>

                                <!-- ユーザー情報 -->
                                <div class="form-item remarks-item">
                                    <div class="form-item-label">
                                        <label class="item-label">ユーザー名</label>
                                    </div>
                                    <div class="input-group form-item">
                                        <input disabled type="text" class="form-control" name="user_name"
                                            <?php $input_user_name = $user_name ?? '';
                                            if($entry_error == True or $read_flag == True){echo "value='{$input_user_name}'";} ?>
                                        >
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <!-- 登録ボタン -->
                    <div class="btn-area">
                        <div class="btn-center-area">
                            <div class="btn-item">
                                <button type="submit" class="btn btn-primary" name="entry"
                                        onclick="return confirm('本当に実行しますか？\n※リーダー変更に成功後、現在のアカウントはログアウトします。')">
                                    変更
                                </button>
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



