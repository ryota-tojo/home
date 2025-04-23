<?php
if (session_status() === PHP_SESSION_NONE) {
    session_start();
}
require $_SERVER['DOCUMENT_ROOT'] . '/config/config.php';
require $_SERVER['DOCUMENT_ROOT'] . '/Application/Services/ApiService.php';
require $_SERVER['DOCUMENT_ROOT'] . '/Interfaces/Views/Partials/requireApi.php';


$screen_title = "タイトル";

// 管理者判定
$admin_flag = 0;

?>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><?php echo $screen_title; ?></title>
    <link rel="stylesheet" href="/Interfaces/Assets/CSS/font.css">
    <link rel="stylesheet" href="/Interfaces/Assets/CSS/home.css">
    <link rel="stylesheet" href="/Interfaces/Assets/CSS/input_form.css">
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
            XXXXXX<br>
            XXXXXX
        </div>
    </div>
    <div class="contents">
        <div class="content-row">
            <div class="left">
            </div>
            <div class="center">
                <?php
                if (session_status() == PHP_SESSION_NONE) {
                    // セッションは有効で、開始していないとき
                    session_start();
                }
                $sessionData = $_SESSION;

                // セッションIDを取得
                $sessionId = session_id();

                // セッションの全データをテーブルで表示
                echo "<table border='1'>";
                echo "<tr><th>キー</th><th>値</th></tr>";

                // セッション情報を1つずつ表示
                foreach ($sessionData as $key => $value) {
                    echo "<tr>";
                    echo "<td>" . htmlspecialchars($key) . "</td>"; // セッションキー
                    echo "<td>" . htmlspecialchars(print_r($value, true)) . "</td>"; // セッションの値（配列やオブジェクトも表示可能）
                    echo "</tr>";
                }

                echo "</table>";
                ?>

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
    document.addEventListener("DOMContentLoaded", function () {
        const todayButton = document.querySelector("button[name='today-btn']");
        const dateInput = document.querySelector("input[name='date']");

        todayButton.addEventListener("click", function () {
            const today = new Date();
            const year = today.getFullYear();
            const month = String(today.getMonth() + 1).padStart(2, '0');
            const day = String(today.getDate()).padStart(2, '0');
            const formattedDate = `${year}-${month}-${day}`;
            dateInput.value = formattedDate;
        });
    });
</script>
<!-- bootstrap-datepickerのjavascriptコード -->
<script>
    $('#sample1').datepicker();
</script>
</body>
</html>



