<?php
if (session_status() === PHP_SESSION_NONE) {
    session_start();
}

// 管理者判定
$admin_flag = 0;
if ($_SESSION['user_permission'] == 2) {
    $admin_flag = 1;
}

// マスター設定
$master_setting_maintenance = 0;
$master_setting_refer_api_result = apiCallMasterSettingRefer();
foreach ($master_setting_refer_api_result['data']['setting_list'] as $setting) {
    if ($setting['setting_key'] == 'maintenance') {
        $master_setting_maintenance = $setting['setting_value'];
    }
}

?>

<!-- 共通 -->
<link rel="stylesheet" href="/Interfaces/Assets/CSS/nav.css">

<!-- 管理者メニュー -->
<?php if($admin_flag == 1){ ?>
<nav class="navbar navbar-expand-lg bg-body-tertiary main-font <?php if($master_setting_maintenance == "1"){ echo "maintenance-nav"; }?> ">
    <div class="container-fluid">
        <?php if(!isset($_SESSION['access_error'])){ ?>
        <a class="navbar-brand title-font" href="<?php echo './home.php';?>">管理者</a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>

        <div class="collapse navbar-collapse" id="navbarSupportedContent">
            <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                <li class="nav-item dropdown">
                    <a class="nav-link dropdown-toggle menu-font" href="#" role="button" data-bs-toggle="dropdown" aria-expanded="false">
                        テンプレート
                    </a>
                    <ul class="dropdown-menu drop-font">
                        <li><a class="dropdown-item" href="/Interfaces/Views/Pages/test.php">テスト用ページ</a></li>
                        <li><a class="dropdown-item" href="/Interfaces/Views/Pages/home_setting_tmp.php">設定フォーム</a></li>
                        <li><a class="dropdown-item" href="/Interfaces/Views/Pages/home_input_tmp.php">入力フォーム</a></li>
                    </ul>
                </li>
                <li class="nav-item dropdown">
                    <a class="nav-link dropdown-toggle menu-font" href="#" role="button" data-bs-toggle="dropdown" aria-expanded="false">
                        マスタ設定
                    </a>
                    <ul class="dropdown-menu drop-font">
                        <li><a class="dropdown-item" href="/Interfaces/Views/Pages/admin/admin_master_setting_system.php">システム設定</a></li>
                        <li><a class="dropdown-item" href="/Interfaces/Views/Pages/admin/admin_master_setting_send.php">通知設定</a></li>
                    </ul>
                </li>
                <li class="nav-item dropdown">
                    <a class="nav-link dropdown-toggle menu-font" href="#" role="button" data-bs-toggle="dropdown" aria-expanded="false">
                        データ管理
                    </a>
                    <ul class="dropdown-menu drop-font">
                        <li><a class="dropdown-item" href="/Interfaces/Views/Pages/admin/admin_user_control.php">ユーザー管理</a></li>
                        <li><a class="dropdown-item" href="/Interfaces/Views/Pages/admin/admin_group_control.php">所属グループ管理</a></li>
                    </ul>
                </li>
                <li class="nav-item dropdown">
                    <a class="nav-link dropdown-toggle menu-font" href="#" role="button" data-bs-toggle="dropdown" aria-expanded="false">
                        運用管理
                    </a>
                    <ul class="dropdown-menu drop-font">
                        <li><a class="dropdown-item" href="/Interfaces/Views/Pages/admin/admin_notice_entry.php">お知らせ登録</a></li>
                        <li><a class="dropdown-item" href="/Interfaces/Views/Pages/admin/admin_maintenance.php">メンテナンス</a></li>
                        <li><a class="dropdown-item" href="/Interfaces/Views/Pages/session_list.php">セッション管理</a></li>
                    </ul>
                </li>
                <li class="nav-item dropdown">
                    <a class="nav-link dropdown-toggle menu-font" href="#" role="button" data-bs-toggle="dropdown" aria-expanded="false">
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
            <?php }else{ ?>
                <a class="navbar-brand title-font" href="<?php echo '#';?>">管理者</a>
                <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
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
<?php }else{ ?>
<nav class="navbar navbar-expand-lg bg-body-tertiary <?php if($master_setting_maintenance == "1"){ echo "maintenance-nav"; }?> ">
    <div class="container-fluid">

        <?php if(!isset($_SESSION['access_error'])){ ?>
        <a class="navbar-brand" href="<?php echo './home.php';?>">ホーム</a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>

        <div class="collapse navbar-collapse" id="navbarSupportedContent">
            <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                <li class="nav-item dropdown">
                    <a class="nav-link dropdown-toggle menu-font" href="#" role="button" data-bs-toggle="dropdown" aria-expanded="false">
                        テンプレート
                    </a>
                    <ul class="dropdown-menu drop-font">
                        <li><a class="dropdown-item" href="/Interfaces/Views/Pages/test.php">テスト用ページ</a></li>
                        <li><a class="dropdown-item" href="/Interfaces/Views/Pages/home_setting_tmp.php">設定フォーム</a></li>
                        <li><a class="dropdown-item" href="/Interfaces/Views/Pages/home_input_tmp.php">入力フォーム</a></li>
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

            <?php }else{ ?>
                <a class="navbar-brand title-font" href="<?php echo '#';?>">管理者</a>
                <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
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