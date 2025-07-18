<?php
if (session_status() === PHP_SESSION_NONE) {
    session_start();
}

// ユーザー情報最新化
require_once $_SERVER['DOCUMENT_ROOT'] . '/Interfaces/Views/Partials/user/get_user.php';
getUser($_SESSION['user_name']);

// 管理者判定
$admin_flag = 0;
if ($_SESSION['user_permission'] == 2) {
    $admin_flag = 1;
}

// マスター設定
$master_setting_maintenance = 0;
$master_setting_lording_layout = 0;
$master_setting_output_logs = 0;
$master_setting_refer_api_result = apiCallMasterSettingRefer();
foreach ($master_setting_refer_api_result['data']['setting_list'] as $setting) {
    if ($setting['setting_key'] == 'maintenance') {
        $master_setting_maintenance = $setting['setting_value'];
    }
    if ($setting['setting_key'] == 'lording_layout') {
        $master_setting_lording_layout = $setting['setting_value'];
    }
    if ($setting['setting_key'] == 'output_logs') {
        $master_setting_output_logs = $setting['setting_value'];
    }
}
?>

<!-- 共通 -->
<link rel="stylesheet" href="/Interfaces/Assets/CSS/nav.css">

<!-- 管理者メニュー -->
<?php if ($admin_flag == 1) { ?>
    <nav class="navbar navbar-expand-lg bg-body-tertiary main-font <?php if ($master_setting_maintenance == "1") {
        echo "maintenance-nav";
    } ?> ">
        <div class="container-fluid">
            <?php if (!isset($_SESSION['access_error'])){ ?>
            <a class="navbar-brand title-font" href="<?php echo '/Interfaces/Views/Pages/home.php'; ?>">管理者</a>
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse"
                    data-bs-target="#navbarSupportedContent" aria-controls="navbarSupportedContent"
                    aria-expanded="false" aria-label="Toggle navigation">
                <span class="navbar-toggler-icon"></span>
            </button>

            <div class="collapse navbar-collapse" id="navbarSupportedContent">
                <ul class="navbar-nav me-auto mb-2 mb-lg-0">
<!--                    <li class="nav-item dropdown">-->
<!--                        <a class="nav-link dropdown-toggle menu-font" href="#" role="button" data-bs-toggle="dropdown"-->
<!--                           aria-expanded="false">-->
<!--                            テンプレート-->
<!--                        </a>-->
<!--                        <ul class="dropdown-menu drop-font">-->
<!--                            <li><a class="dropdown-item" href="/Interfaces/Views/Pages/test.php">テスト用ページ</a></li>-->
<!--                            <li><a class="dropdown-item"-->
<!--                                   href="/Interfaces/Views/Pages/home_setting_tmp.php">設定フォーム</a></li>-->
<!--                            <li><a class="dropdown-item"-->
<!--                                   href="/Interfaces/Views/Pages/home_input_tmp.php">入力フォーム</a></li>-->
<!--                        </ul>-->
<!--                    </li>-->
                    <li class="nav-item dropdown">
                        <a class="nav-link dropdown-toggle menu-font" href="#" role="button" data-bs-toggle="dropdown"
                           aria-expanded="false">
                            マスタ設定
                        </a>
                        <ul class="dropdown-menu drop-font">
                            <li><a class="dropdown-item"
                                   href="/Interfaces/Views/Pages/admin/admin_master_setting_system.php">システム設定</a>
                            </li>
                            <li><a class="dropdown-item"
                                   href="/Interfaces/Views/Pages/admin/admin_master_setting_send.php">通知設定</a></li>
                        </ul>
                    </li>
                    <li class="nav-item dropdown">
                        <a class="nav-link dropdown-toggle menu-font" href="#" role="button" data-bs-toggle="dropdown"
                           aria-expanded="false">
                            データ管理
                        </a>
                        <ul class="dropdown-menu drop-font">
                            <li><a class="dropdown-item" href="/Interfaces/Views/Pages/admin/admin_user_control.php">ユーザー管理</a>
                            </li>
                            <li><a class="dropdown-item" href="/Interfaces/Views/Pages/admin/admin_group_control.php">所属グループ管理</a>
                            </li>
                            <li><a class="dropdown-item"
                                   href="/Interfaces/Views/Pages/admin/admin_user_setting_all_apply.php">ユーザー設定一括反映</a>
                            </li>
                            <li><a class="dropdown-item"
                                   href="/Interfaces/Views/Pages/admin/admin_group_setting_all_apply.php">所属グループ設定一括反映</a>
                            </li>
                        </ul>
                    </li>
                    <li class="nav-item dropdown">
                        <a class="nav-link dropdown-toggle menu-font" href="#" role="button" data-bs-toggle="dropdown"
                           aria-expanded="false">
                            運用管理
                        </a>
                        <ul class="dropdown-menu drop-font">
                            <li><a class="dropdown-item" href="/Interfaces/Views/Pages/admin/admin_notice_entry.php">お知らせ登録</a>
                            </li>
                            <li><a class="dropdown-item" href="/Interfaces/Views/Pages/admin/admin_maintenance.php">メンテナンス</a>
                            </li>
                            <li><a class="dropdown-item"
                                   href="/Interfaces/Views/Pages/session_list.php">セッション管理</a></li>
                        </ul>
                    </li>
                    <li class="nav-item dropdown">
                        <a class="nav-link dropdown-toggle menu-font" href="#" role="button" data-bs-toggle="dropdown"
                           aria-expanded="false">
                            データベース管理
                        </a>
                        <ul class="dropdown-menu drop-font">
                            <li><a class="dropdown-item" href="#">バックアップ</a></li>
                            <li><a class="dropdown-item" href="#">初期化</a></li>
                        </ul>
                    </li>
                </ul>
                <ul class="navbar-nav ms-auto mb-2 mb-lg-0">
                    <li class="nav-item">
                        <a class="nav-link menu-font" href="/Interfaces/Views/Pages/logout.php">
                            ログアウト
                        </a>
                    </li>
                </ul>
                <?php } else { ?>
                    <a class="navbar-brand title-font" href="<?php echo '#'; ?>">管理者</a>
                    <button class="navbar-toggler" type="button" data-bs-toggle="collapse"
                            data-bs-target="#navbarSupportedContent" aria-controls="navbarSupportedContent"
                            aria-expanded="false" aria-label="Toggle navigation">
                        <span class="navbar-toggler-icon"></span>
                    </button>
                    <ul class="navbar-nav ms-auto mb-2 mb-lg-0">
                        <li class="nav-item">
                            <a class="nav-link menu-font" href="/Interfaces/Views/Pages/logout.php">
                                ログアウト
                            </a>
                        </li>
                    </ul>

                <?php } ?>
            </div>
        </div>
    </nav>

    <!-- ユーザーメニュー -->
<?php } else { ?>
    <nav class="navbar navbar-expand-lg bg-body-tertiary <?php if ($master_setting_maintenance == "1") {
        echo "maintenance-nav";
    } ?> ">
        <div class="container-fluid">

            <?php if (!isset($_SESSION['access_error'])){ ?>
            <a class="navbar-brand title-font" href="<?php echo '/Interfaces/Views/Pages/home.php'; ?>">ホーム</a>
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse"
                    data-bs-target="#navbarSupportedContent" aria-controls="navbarSupportedContent"
                    aria-expanded="false" aria-label="Toggle navigation">
                <span class="navbar-toggler-icon"></span>
            </button>

            <div class="collapse navbar-collapse" id="navbarSupportedContent">

                <?php if ($_SESSION['user_groups_id'] == "" or $_SESSION['user_group_approval_flg'] == 0) { ?>

                    <!-- グループ未所属 -->
                    <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                        <li class="nav-item dropdown">
                            <a class="nav-link dropdown-toggle menu-font" href="#" role="button"
                               data-bs-toggle="dropdown" aria-expanded="false">
                                所属グループ
                            </a>
                            <ul class="dropdown-menu drop-font">
                                <li><a class="dropdown-item"
                                       href="/Interfaces/Views/Pages/user/un_group/user_group_entry.php">新規登録</a>
                                </li>
                                <li><a class="dropdown-item"
                                       href="/Interfaces/Views/Pages/user/un_group/user_group_application.php">加入申請</a>
                                </li>
                            </ul>
                        </li>
                    </ul>

                <?php } else { ?>

                    <!-- グループ所属 -->
                    <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                        <?php if ($_SESSION['user_group_leader'] == 1) { ?>
                            <li class="nav-item dropdown">
                                <a class="nav-link dropdown-toggle menu-font" href="#" role="button"
                                   data-bs-toggle="dropdown" aria-expanded="false">
                                    グループ管理
                                </a>
                                <ul class="dropdown-menu drop-font">
                                    <li><a class="dropdown-item" href="/Interfaces/Views/Pages/user/group/setting/config/user_group_config.php">設定</a></li>
                                    <li><a class="dropdown-item"
                                           href="/Interfaces/Views/Pages/user/group/setting/group_user/user_group_user_list.php">ユーザー一覧</a></li>
                                    <li><a class="dropdown-item"
                                           href="/Interfaces/Views/Pages/user/group/setting/category/user_group_category_list.php">カテゴリー一覧</a></li>
                                    <li><a class="dropdown-item"
                                           href="/Interfaces/Views/Pages/user/group/setting/member/user_group_member_list.php">メンバー一覧</a></li>
                                </ul>
                            </li>
                        <?php } ?>

                        <li class="nav-item dropdown">
                            <a class="nav-link dropdown-toggle menu-font" href="#" role="button"
                               data-bs-toggle="dropdown" aria-expanded="false">
                                予算
                            </a>
                            <ul class="dropdown-menu drop-font">
                                <li><a class="dropdown-item" href="/Interfaces/Views/Pages/test.php">入力</a></li>
                                <li><a class="dropdown-item" href="/Interfaces/Views/Pages/test.php">一覧</a></li>
                            </ul>
                        </li>

                        <li class="nav-item dropdown">
                            <a class="nav-link dropdown-toggle menu-font" href="#" role="button"
                               data-bs-toggle="dropdown" aria-expanded="false">
                                データ入力
                            </a>
                            <ul class="dropdown-menu drop-font">
                                <?php if ($_SESSION['user_group_leader'] == 1) { ?>
                                    <li><a class="dropdown-item"
                                           href="/Interfaces/Views/Pages/test.php">テンプレート入力</a></li>
                                <?php } ?>
                                <li><a class="dropdown-item" href="/Interfaces/Views/Pages/test.php">購入データ入力</a>
                                </li>
                            </ul>
                        </li>

                        <li class="nav-item dropdown">
                            <a class="nav-link dropdown-toggle menu-font" href="#" role="button"
                               data-bs-toggle="dropdown" aria-expanded="false">
                                データ管理
                            </a>
                            <ul class="dropdown-menu drop-font">
                                <li><a class="dropdown-item"
                                       href="/Interfaces/Views/Pages/test.php">購入データ一覧</a></li>
                                <li><a class="dropdown-item" href="/Interfaces/Views/Pages/test.php">データ確定</a>
                                </li>
                                <li><a class="dropdown-item" href="/Interfaces/Views/Pages/test.php">コメント一覧</a>
                                </li>
                            </ul>
                        </li>

                        <li class="nav-item dropdown">
                            <a class="nav-link dropdown-toggle menu-font" href="#" role="button"
                               data-bs-toggle="dropdown" aria-expanded="false">
                                データ分析
                            </a>
                            <ul class="dropdown-menu drop-font">
                                <li><a class="dropdown-item"
                                       href="/Interfaces/Views/Pages/test.php">予実対比</a></li>
                                <li><a class="dropdown-item" href="/Interfaces/Views/Pages/test.php">前年対比</a>
                                </li>
                            </ul>
                        </li>
                    </ul>

                <?php } ?>

                <ul class="navbar-nav ms-auto mb-2 mb-lg-0">
                    <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                        <li class="nav-item dropdown">
                            <a class="nav-link dropdown-toggle menu-font" href="#" role="button"
                               data-bs-toggle="dropdown" aria-expanded="false">
                                その他
                            </a>
                            <ul class="dropdown-menu drop-font">
                                <li><a class="dropdown-item" href="/Interfaces/Views/Pages/test.php">ユーザー設定</a>
                                </li>
                            </ul>
                        </li>
                    </ul>
                    <li class="nav-item">
                        <a class="nav-link menu-font" href="/Interfaces/Views/Pages/logout.php">
                            ログアウト
                        </a>
                    </li>
                </ul>

                <?php } else { ?>
                    <a class="navbar-brand title-font" href="<?php echo '#'; ?>">ホーム</a>
                    <button class="navbar-toggler" type="button" data-bs-toggle="collapse"
                            data-bs-target="#navbarSupportedContent" aria-controls="navbarSupportedContent"
                            aria-expanded="false" aria-label="Toggle navigation">
                        <span class="navbar-toggler-icon"></span>
                    </button>
                    <ul class="navbar-nav ms-auto mb-2 mb-lg-0">
                        <li class="nav-item">
                            <a class="nav-link menu-font" href="./logout.php">
                                ログアウト
                            </a>
                        </li>
                    </ul>

                <?php } ?>
            </div>
        </div>
    </nav>
<?php } ?>

<?php

// 共通フォント読込
require_once $_SERVER['DOCUMENT_ROOT'] . '/Interfaces/Assets/CSS/font/basic_font.php';

// ロード画面読込
if ($master_setting_lording_layout == 1) {
    require_once $_SERVER['DOCUMENT_ROOT'] . '/Interfaces/Views/Layouts/lord_start.php';
}
?>
