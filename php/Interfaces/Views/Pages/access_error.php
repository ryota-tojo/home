<?php

// 管理者判定
$admin_flag = 1

?>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>ページタイトル</title>
    <link rel="stylesheet" href="/新しいフォルダー/Interfaces/Assets/CSS/font.css">
    <link rel="stylesheet" href="/新しいフォルダー/Interfaces/Assets/CSS/home.css">
    <link rel="stylesheet" href="/新しいフォルダー/Interfaces/Assets/CSS/input_form.css">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha3/dist/css/bootstrap.min.css" rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha3/dist/js/bootstrap.bundle.min.js"></script>
</head>

<body class="<?php if($admin_flag == 1){echo 'admin-body';}else{echo 'body';}?>">
<header>
    <?php require $_SERVER['DOCUMENT_ROOT'] . '/includes/nav.php'; ?>
</header>


<main>
    <div class="title-area">
        <h2 class="title err_msg">不正遷移エラー</h2>
    </div>
    <div class="summary-area">
        <div class="summary err_msg">
            セッション切れ、または画面に不正アクセスしました。<br>
            再ログインしてください。
        </div>
    </div>
    <div class="contents">
        <div class="content-row">
            <div class="left">
            </div>
            <div class="center">
            </div>
            <div class="right">
            </div>
        </div>
    </div>
</main>

<footer>
    <?php require $_SERVER['DOCUMENT_ROOT'] . '/includes/footer.php'; ?>
</footer>
<!-- bootstrap-datepickerのjavascriptコード -->
<script>
    $('#sample1').datepicker();
</script>
</body>
</html>



