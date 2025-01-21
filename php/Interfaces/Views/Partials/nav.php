<?php $admin = True ?>

<!-- 共通 -->
<link rel="stylesheet" href="/新しいフォルダー/Interfaces/Assets/CSS/nav.css">

<!-- 管理者メニュー -->
<?php if($admin == True){ ?>
<nav class="navbar navbar-expand-lg bg-body-tertiary main-font">
    <div class="container-fluid">
        <a class="navbar-brand title-font" href="<?php echo './home_input_tmp.php';?>">管理者</a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>

        <div class="collapse navbar-collapse" id="navbarSupportedContent">
            <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                <li class="nav-item dropdown">
                    <a class="nav-link dropdown-toggle menu-font" href="#" role="button" data-bs-toggle="dropdown" aria-expanded="false">
                        システム設定
                    </a>
                    <ul class="dropdown-menu drop-font">
                        <li><a class="dropdown-item" href="#">マスタ設定</a></li>
                    </ul>
                </li>
                <li class="nav-item dropdown">
                    <a class="nav-link dropdown-toggle menu-font" href="#" role="button" data-bs-toggle="dropdown" aria-expanded="false">
                        データ管理
                    </a>
                    <ul class="dropdown-menu drop-font">
                        <li><a class="dropdown-item" href="#">ユーザー管理</a></li>
                    </ul>
                </li>
                <li class="nav-item dropdown">
                    <a class="nav-link dropdown-toggle menu-font" href="#" role="button" data-bs-toggle="dropdown" aria-expanded="false">
                        運用管理
                    </a>
                    <ul class="dropdown-menu drop-font">
                        <li><a class="dropdown-item" href="#">メンテナンス</a></li>
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
                    <a class="nav-link menu-font" href="./logout.php">
                        ログアウト
                    </a>
                </li>
            </ul>
        </div>
    </div>
</nav>

<!-- ユーザーメニュー -->
<?php }else{ ?>
<nav class="navbar navbar-expand-lg bg-body-tertiary">
    <div class="container-fluid">
        <a class="navbar-brand" href="<?php echo './home_input_tmp.php';?>">ホーム</a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>

        <div class="collapse navbar-collapse" id="navbarSupportedContent">
            <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                <li class="nav-item dropdown">
                    <a class="nav-link dropdown-toggle" href="#" role="button" data-bs-toggle="dropdown" aria-expanded="false">
                        Dropdown
                    </a>
                    <ul class="dropdown-menu">
                        <li><a class="dropdown-item" href="#">Action</a></li>
                        <li><a class="dropdown-item" href="#">Another action</a></li>
                    </ul>
                </li>
            </ul>
            <ul class="navbar-nav ms-auto mb-2 mb-lg-0">
                <li class="nav-item dropdown">
                    <a class="nav-link dropdown-toggle" href="#" role="button" data-bs-toggle="dropdown" aria-expanded="false">
                        Dropdown
                    </a>
                    <ul class="dropdown-menu">
                        <li><a class="dropdown-item" href="#">Action</a></li>
                        <li><a class="dropdown-item" href="#">Another action</a></li>
                    </ul>
                </li>
            </ul>
        </div>
    </div>
</nav>
<?php } ?>