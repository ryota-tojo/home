<?php
if (session_status() === PHP_SESSION_NONE) {
    session_start();
}
require_once $_SERVER['DOCUMENT_ROOT'] . '/config/config.php';
require_once $_SERVER['DOCUMENT_ROOT'] . '/config/log_config.php';
require_once $_SERVER['DOCUMENT_ROOT'] . '/Application/Services/ApiService.php';
require_once $_SERVER['DOCUMENT_ROOT'] . '/Interfaces/Views/Partials/requireApi.php';
require_once $_SERVER['DOCUMENT_ROOT'] . '/Interfaces/Views/Partials/group/group_create.php';
require_once $_SERVER['DOCUMENT_ROOT'] . '/Interfaces/Views/Partials/systems/logs/create_logs.php';
require_once $_SERVER['DOCUMENT_ROOT'] . '/Interfaces/Views/Partials/systems/screen/get_screen.php';

$screen_items = getScreen( basename(__FILE__));
$screen_title = $screen_items['name'];
$screen_remarks = $screen_items['remarks'];

// 管理者判定
$admin_flag = 0;

// 所属グループ判定
if ($_SESSION['user_groups_id'] != null and $_SESSION['user_group_approval_flg'] == 1) {
    echo "<script>window.location.href = '/Interfaces/Views/Pages/home.php';</script>";
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
$entry_error = False;

// ボタン押下時の処理

$post_groups_id = '';
$post_group_name = '';
$post_group_password = '';

if (isset($_POST['entry'])) {
    $entry_button_click_flg = True;

    $post_groups_id = $_POST['groups-id'] ?? '';
    $post_group_name = $_POST['group-name'] ?? '';
    $post_group_password = $_POST['group-password'] ?? '';
    $post_group_re_password = $_POST['group-re-password'] ?? '';
    $new_leader_id = $_SESSION['user_id'];

    if ($post_group_password != $post_group_re_password) {
        $entry_error = True;
        $message = "確認用パスワードが一致しません";
    } else {
        $result = groupCreate($new_leader_id, $post_groups_id, $post_group_name, $post_group_password);
        $data = json_decode($result, true);
        $status = $data['status'];

        if ($status != "success") {
            $entry_error = True;
            $message = $data['message'];

        } else {
            $message = $data['message'];

            // ユーザー情報最新化
            require_once $_SERVER['DOCUMENT_ROOT'] . '/Interfaces/Views/Partials/user/get_user.php';
            getUser($_SESSION['user_name']);
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

    <?php
    if ($entry_button_click_flg == True) {
        if ($entry_error == True) {
            echo "<div class='message-fields error-message'>$message</div>";
        } else {
            echo "<div class='message-fields success-message'>$message</div>";
        }
    }
    ?>

    <?php
    $hidden = "";
    $content = "";
    if ($_SESSION['user_group_approval_flg'] === "0") {
        $hidden = " hidden disabled ";
        $content = "
                            <div class='summary-area'>
                                <div class='summary suc_msg'>
                                    所属グループへ加入申請中です。<br>承認されるまでお待ちください。
                                </div>
                            </div>
                        ";
    }
    if ($_SESSION['user_group_approval_flg'] === "1") {
        $hidden = " hidden disabled ";
        $content = "
                            <div class='summary-area'>
                                <div class='summary err_msg'>
                                    所属グループを作成済みです。<br>5秒後、自動でログアウトします。
                                </div>
                            </div>
                            <script>
                                setTimeout(function() {
                                    window.location.href = '/Interfaces/Views/Pages/logout.php';
                                }, 5000); 
                            </script>
                        ";
    }
    ?>
    <div class="contents">
        <div class="content-row">
            <div class="left">
            </div>
            <div class="center">
                <div class="settings">
                    <?php echo $content; ?>
                    <form action="" method="post" <?php echo $hidden; ?>>

                        <div class="settings-section">
                            <h4 class="settings-title">所属グループ情報</h4>
                            <hr>

                            <div class="settings-form">
                                <div class="settings-label-container">
                                    <div class="settings-label">
                                        所属グループID
                                        <div class="required-comment">※必須</div>
                                    </div>
                                </div>
                                <div class="settings-input-container">
                                    <input required type="text" minlength="4" maxlength="32" class="form-control"
                                           name="groups-id" placeholder="※所属グループIDを入力してください"
                                        <?php
                                        if ($entry_error == True) {
                                            echo "value='$post_groups_id'";
                                        }
                                        ?>
                                    >
                                </div>
                            </div>
                            <div class="settings-form">
                                <div class="settings-label-container">
                                    <div class="settings-label">
                                        所属グループ名
                                        <div class="required-comment">※必須</div>
                                    </div>
                                </div>
                                <div class="settings-input-container">
                                    <input required type="text" minlength="4" maxlength="32" class="form-control"
                                           name="group-name" placeholder="※所属グループ名を入力してください"
                                        <?php
                                        if ($entry_error == True) {
                                            echo "value='$post_group_name'";
                                        }
                                        ?>
                                    >
                                </div>
                            </div>
                            <div class="settings-form">
                                <div class="settings-label-container">
                                    <div class="settings-label">
                                        所属グループパスワード
                                        <div class="required-comment">※必須</div>
                                    </div>
                                </div>
                                <div class="settings-input-container">
                                    <input required type="password" minlength="4" maxlength="64" class="form-control"
                                           name="group-password" placeholder="※所属グループパスワードを入力してください"
                                        <?php
                                        if ($entry_error == True) {
                                            echo "value=''";
                                        }
                                        ?>
                                    >
                                </div>
                            </div>
                            <div class="settings-form">
                                <div class="settings-label-container">
                                    <div class="settings-label">
                                        所属グループパスワード（確認用）
                                        <div class="required-comment">※必須</div>
                                    </div>
                                </div>
                                <div class="settings-input-container">
                                    <input required type="password" minlength="4" maxlength="64" class="form-control"
                                           name="group-re-password"
                                           placeholder="※所属グループパスワードを入力してください"
                                        <?php
                                        if ($entry_error == True) {
                                            echo "value=''";
                                        }
                                        ?>
                                    >
                                </div>
                            </div>
                        </div>

                        <!-- 登録ボタン -->
                        <div class="btn-area">
                            <div class="btn-center-area">
                                <div class="btn-item">
                                    <button type="submit" class="btn btn-primary" name="entry">
                                        所属グループ登録
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
    function onRowClick(userId, userName) {
        // フォームの hidden フィールドに値をセット
        document.querySelector("input[name='new-leader-id']").value = userId;
        document.querySelector("input[name='new-leader-name']").value = userName;

        // 表示ラベルに値を反映
        document.getElementById("new-leader-id-label").value = userId;
        document.getElementById("new-leader-name-label").value = userName;
    }
</script>
<script>
    function toggleAll(source) {
        const checkboxes = document.querySelectorAll('input[name="selected_users[]"]');
        checkboxes.forEach(cb => cb.checked = source.checked);
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



