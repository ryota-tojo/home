<?php
if (session_status() === PHP_SESSION_NONE) {
    session_start();
}
require $_SERVER['DOCUMENT_ROOT'] . '/config/config.php';
require $_SERVER['DOCUMENT_ROOT'] . '/Application/Services/ApiService.php';
require $_SERVER['DOCUMENT_ROOT'] . '/Interfaces/Views/Partials/requireApi.php';


$screen_title = "システム設定";

// 管理者判定
$admin_flag = 0;
if ($_SESSION['user_permission'] == 2) {
    $admin_flag = 1;
}
if($admin_flag == 0){
    $_SESSION['access_error'] = 1;
    echo "<script>window.location.href = 'access_error.php';</script>";
}

// 変数初期化
$entry_button_click_flg = False;
$message = "";
$entry_error = False;

// ボタン押下時の処理
if (isset($_POST['entry'])) {
    $entry_button_click_flg = True;

    $status = "success";



    if ($status != "success") {

        $entry_error = True;
        $message = "設定の更新に失敗しました";
    } else {
        $message = "設定を更新しました";
    }
}

// マスター設定
$master_setting_api_result = apiCallMasterSettingRefer();
$master_settings = [];
foreach ($master_setting_api_result['data']['setting_list'] as $setting) {
    $master_settings[$setting['setting_key']] = $setting['setting_value'];
}

$master_setting_login_failure_limit = $master_settings['login_failure_limit'] ?? null;
$master_setting_lording_layout = $master_settings['lording_layout'] ?? null;
$master_setting_admin_userdata_view = $master_settings['admin_userdata_view'] ?? null;
$master_setting_admin_notice_view = $master_settings['admin_notice_view'] ?? null;
$master_setting_admin_notice_default_title = $master_settings['admin_notice_default_title'] ?? null;
$master_setting_admin_notice_default_content = $master_settings['admin_notice_default_content'] ?? null;
$master_setting_user_input_history_view = $master_settings['user_input_history_view'] ?? null;
$master_setting_user_management_view = $master_settings['user_management_view'] ?? null;
$master_setting_user_analysis_graph_size_pc_width = $master_settings['user_analysis_graph_size_pc_width'] ?? null;
$master_setting_user_analysis_graph_size_pc_height = $master_settings['user_analysis_graph_size_pc_height'] ?? null;
$master_setting_user_analysis_graph_size_sp_width = $master_settings['user_analysis_graph_size_sp_width'] ?? null;
$master_setting_user_analysis_graph_size_sp_height = $master_settings['user_analysis_graph_size_sp_height'] ?? null;
$master_setting_user_analysis_graph_size_tb_width = $master_settings['user_analysis_graph_size_tb_width'] ?? null;
$master_setting_user_analysis_graph_size_tb_height = $master_settings['user_analysis_graph_size_tb_height'] ?? null;
$master_setting_user_communication_input_history_view = $master_settings['user_communication_input_history_view'] ?? null;
$master_setting_user_communication_list_view = $master_settings['user_communication_list_view'] ?? null;
$master_setting_user_communication_list_view_conditions = $master_settings['user_communication_list_view_conditions'] ?? null;

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
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha3/dist/css/bootstrap.min.css" rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha3/dist/js/bootstrap.bundle.min.js"></script>
</head>

<body class="<?php if($admin_flag == 1){echo 'admin-body';}else{echo 'body';}?>">
<header>
    <?php require $_SERVER['DOCUMENT_ROOT'] . '/Interfaces/Views/Partials/nav.php'; ?>
</header>


<main>
    <div class="title-area">
        <h2 class="title"><?php echo $screen_title; ?></h2>
    </div>
    <div class="summary-area">
        <div class="summary">
            システム全体の設定を管理する
        </div>
    </div>

    <?php
    if($entry_button_click_flg == True){
        if($entry_error == True){
            echo "<div class='message-fields error-message'>$message</div>";
        }else{
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
                            <h4 class="settings-title">システム設定</h4>
                            <hr>

                            <!-- ログイン失敗許容回数 -->
                            <div class="settings-form">
                                <div class="settings-label-container">
                                    <div class="settings-label">
                                        ログイン失敗許容回数
                                    </div>
                                </div>
                                <div class="settings-input-container">
                                    <div class="settings-input">
                                        <input required type="number" min="1" max="99" class="form-control" name="login_failure_limit"
                                            <?php echo "value='$master_setting_login_failure_limit'"; ?>
                                        >
                                    </div>
                                </div>
                            </div>

                            <!-- ロード画面レイアウトパターン -->
                            <div class="settings-form">
                                <div class="settings-label-container">
                                    <div class="settings-label">
                                        ロード画面レイアウトパターン
                                    </div>
                                </div>
                                <div class="settings-input-container">
                                    <div class="settings-input">
                                        <input required type="number" min="0" max="1" class="form-control" name="lording_layout"
                                            <?php echo "value='$master_setting_lording_layout'"; ?>
                                        >
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div class="settings-section">
                            <h4 class="settings-title">管理者画面設定</h4>
                            <hr>

                            <h5 class="settings-subtitle">ユーザー管理画面設定</h5>

                            <!-- ユーザーデータ表示数 -->
                            <div class="settings-form">
                                <div class="settings-label-container">
                                    <div class="settings-label">
                                        ユーザーデータ表示数 / 1ページ
                                    </div>
                                </div>
                                <div class="settings-input-container">
                                    <div class="settings-input">
                                        <input required type="number" min="10" max="25" class="form-control" name="admin_userdata_view"
                                            <?php echo "value='$master_setting_admin_userdata_view'"; ?>
                                        >
                                    </div>
                                </div>
                            </div>

                            <h5 class="settings-subtitle">お知らせ管理画面設定</h5>

                            <!-- お知らせデフォルト表示数 -->
                            <div class="settings-form">
                                <div class="settings-label-container">
                                    <div class="settings-label">
                                        お知らせデフォルト表示数
                                    </div>
                                </div>
                                <div class="settings-input-container">
                                    <div class="settings-input">
                                        <input required type="number" min="1" max="10" class="form-control" name="admin_notice_view"
                                            <?php echo "value='$master_setting_admin_notice_view'"; ?>
                                        >
                                    </div>
                                </div>
                            </div>

                            <!-- お知らせタイトルデフォルト値 -->
                            <div class="settings-form">
                                <div class="settings-label-container">
                                    <div class="settings-label">
                                        お知らせタイトルデフォルト値
                                    </div>
                                </div>
                                <div class="settings-input-container">
                                    <div class="settings-input-large">
                                        <input required type="text" class="form-control" name="admin_notice_default_title"
                                            <?php echo "value='$master_setting_admin_notice_default_title'"; ?>
                                        >
                                    </div>
                                </div>
                            </div>

                            <!-- お知らせ内容デフォルト値 -->
                            <div class="settings-form">
                                <div class="settings-label-container">
                                    <div class="settings-label">
                                        お知らせ内容デフォルト値
                                    </div>
                                </div>
                                <div class="settings-input-container">
                                    <div class="settings-input-large">
                                        <textarea required class="form-control" name="admin_notice_default_content"><?php echo $master_setting_admin_notice_default_content; ?></textarea>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div class="settings-section">
                            <h4 class="settings-title">ユーザー画面設定</h4>
                            <hr>

                            <h5 class="settings-subtitle">購入データ入力画面設定</h5>

                            <!-- 購入データ入力 - 入力履歴表示数 -->
                            <div class="settings-form">
                                <div class="settings-label-container">
                                    <div class="settings-label">
                                        購入データ入力 - 入力履歴表示数
                                    </div>
                                </div>
                                <div class="settings-input-container">
                                    <div class="settings-input">
                                        <input required type="number" min="10" max="25" class="form-control" name="user_input_history_view"
                                            <?php echo "value='$master_setting_user_input_history_view'"; ?>
                                        >
                                    </div>
                                </div>
                            </div>

                            <h5 class="settings-subtitle">購入データ管理画面設定</h5>

                            <!-- 購入データ管理 - データ表示数 / 1ページ -->
                            <div class="settings-form">
                                <div class="settings-label-container">
                                    <div class="settings-label">
                                        購入データ管理 - データ表示数 / 1ページ
                                    </div>
                                </div>
                                <div class="settings-input-container">
                                    <div class="settings-input">
                                        <input required type="number" min="10" max="25" class="form-control" name="user_management_view"
                                            <?php echo "value='$master_setting_user_management_view'"; ?>
                                        >
                                    </div>
                                </div>
                            </div>

                            <h5 class="settings-subtitle">購入データ分析画面設定</h5>

                            <!-- 購入データ分析 - PC表示: グラフ幅 -->
                            <div class="settings-form">
                                <div class="settings-label-container">
                                    <div class="settings-label">
                                        購入データ分析 - PC表示: グラフ幅
                                    </div>
                                </div>
                                <div class="settings-input-container">
                                    <div class="settings-input">
                                        <input required type="number" min="200" max="2500" class="form-control" name="user_analysis_graph_size_pc_width"
                                            <?php echo "value='$master_setting_user_analysis_graph_size_pc_width'"; ?>
                                        >
                                    </div>
                                </div>
                            </div>

                            <!-- 購入データ分析 - PC表示: グラフ高さ -->
                            <div class="settings-form">
                                <div class="settings-label-container">
                                    <div class="settings-label">
                                        購入データ分析 - PC表示: グラフ高さ
                                    </div>
                                </div>
                                <div class="settings-input-container">
                                    <div class="settings-input">
                                        <input required type="number" min="200" max="2500" class="form-control" name="user_analysis_graph_size_pc_height"
                                            <?php echo "value='$master_setting_user_analysis_graph_size_pc_height'"; ?>
                                        >
                                    </div>
                                </div>
                            </div>

                            <!-- 購入データ分析 - スマホ表示: グラフ幅 -->
                            <div class="settings-form">
                                <div class="settings-label-container">
                                    <div class="settings-label">
                                        購入データ分析 - スマホ表示: グラフ幅
                                    </div>
                                </div>
                                <div class="settings-input-container">
                                    <div class="settings-input">
                                        <input required type="number" min="200" max="2500" class="form-control" name="user_analysis_graph_size_sp_width"
                                            <?php echo "value='$master_setting_user_analysis_graph_size_sp_width'"; ?>
                                        >
                                    </div>
                                </div>
                            </div>

                            <!-- 購入データ分析 - スマホ表示: グラフ高さ -->
                            <div class="settings-form">
                                <div class="settings-label-container">
                                    <div class="settings-label">
                                        購入データ分析 - スマホ表示: グラフ高さ
                                    </div>
                                </div>
                                <div class="settings-input-container">
                                    <div class="settings-input">
                                        <input required type="number" min="200" max="2500" class="form-control" name="user_analysis_graph_size_sp_height"
                                            <?php echo "value='$master_setting_user_analysis_graph_size_sp_height'"; ?>
                                        >
                                    </div>
                                </div>
                            </div>

                            <!-- 購入データ分析 - タブレット表示: グラフ幅 -->
                            <div class="settings-form">
                                <div class="settings-label-container">
                                    <div class="settings-label">
                                        購入データ分析 - タブレット表示: グラフ幅
                                    </div>
                                </div>
                                <div class="settings-input-container">
                                    <div class="settings-input">
                                        <input required type="number" min="200" max="2500" class="form-control" name="user_analysis_graph_size_tb_width"
                                            <?php echo "value='$master_setting_user_analysis_graph_size_tb_width'"; ?>
                                        >
                                    </div>
                                </div>
                            </div>

                            <!-- 購入データ分析 - タブレット表示: グラフ高さ -->
                            <div class="settings-form">
                                <div class="settings-label-container">
                                    <div class="settings-label">
                                        購入データ分析 - タブレット表示: グラフ高さ
                                    </div>
                                </div>
                                <div class="settings-input-container">
                                    <div class="settings-input">
                                        <input required type="number" min="200" max="2500" class="form-control" name="user_analysis_graph_size_tb_height"
                                            <?php echo "value='$master_setting_user_analysis_graph_size_tb_height'"; ?>
                                        >
                                    </div>
                                </div>
                            </div>

                            <h5 class="settings-subtitle">お付き合い帳入力画面設定</h5>

                            <!-- お付き合い帳入力 - 入力履歴表示数 -->
                            <div class="settings-form">
                                <div class="settings-label-container">
                                    <div class="settings-label">
                                        お付き合い帳入力 - 入力履歴表示数
                                    </div>
                                </div>
                                <div class="settings-input-container">
                                    <div class="settings-input">
                                        <input required type="number" min="10" max="25" class="form-control" name="user_communication_input_histry_view"
                                            <?php echo "value='$master_setting_user_communication_input_history_view'"; ?>
                                        >
                                    </div>
                                </div>
                            </div>

                            <!-- お付き合い帳一覧 - データ表示数 / 1ページ -->
                            <div class="settings-form">
                                <div class="settings-label-container">
                                    <div class="settings-label">
                                        お付き合い帳一覧 - データ表示数 / 1ページ
                                    </div>
                                </div>
                                <div class="settings-input-container">
                                    <div class="settings-input">
                                        <input required type="number" min="10" max="25" class="form-control" name="user_communication_list_view"
                                            <?php echo "value='$master_setting_user_communication_list_view'"; ?>
                                        >
                                    </div>
                                </div>
                            </div>

                            <!-- お付き合い帳一覧 - データ検索条件デフォルト設定 -->
                            <div class="settings-form">
                                <div class="settings-label-container">
                                    <div class="settings-label">
                                        お付き合い帳一覧 - データ検索条件デフォルト設定
                                    </div>
                                </div>
                                <div class="settings-input-container">
                                    <div class="settings-input">
                                        <input required type="number" min="0" max="1" class="form-control" name="user_communication_list_view_conditions"
                                            <?php echo "value='$master_setting_user_communication_list_view_conditions'"; ?>
                                        >
                                    </div>
                                </div>
                            </div>

                            <!-- 更新ボタン -->
                            <div class="settings-section">
                                <div class="settings-form">
                                    <div class="settings-btn-container">
                                        <div class="settings-submit">
                                            <button type="submit" class="btn btn-primary" name="entry">更新</button>
                                        </div>
                                    </div>
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
<!-- bootstrap-datepickerのjavascriptコード -->
<script>
    $('#sample1').datepicker();
</script>
</body>
</html>



