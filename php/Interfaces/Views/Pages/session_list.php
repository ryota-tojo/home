<?php
if (session_status() === PHP_SESSION_NONE) {
    session_start();
}
require $_SERVER['DOCUMENT_ROOT'] . '/config/config.php';
require $_SERVER['DOCUMENT_ROOT'] . '/Application/Services/ApiService.php';
require $_SERVER['DOCUMENT_ROOT'] . '/Interfaces/Views/Partials/requireApi.php';


$screen_title = "セッション一覧";

// 管理者判定
$admin_flag = 0;
if(isset($_SESSION['user_permission'])){
    if ($_SESSION['user_permission'] == 2) {
        $admin_flag = 1;
    }
}

?>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><?php echo $screen_title; ?></title>
    <link rel="stylesheet" href="/Interfaces/Assets/CSS/font.css">
    <link rel="stylesheet" href="/Interfaces/Assets/CSS/home.css">
    <link rel="stylesheet" href="/Interfaces/Assets/CSS/shopping_input_form.css">
    <link rel="stylesheet" href="/Interfaces/Assets/CSS/message.css">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha3/dist/css/bootstrap.min.css" rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha3/dist/js/bootstrap.bundle.min.js"></script>
    <style>
        table {
            width: 100%;
            max-width: 800px;
            margin: 20px auto;
            border-collapse: collapse;
            font-family: 'Arial', sans-serif;
            background-color: #fff;
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
        }

        th, td {
            padding: 12px 16px;
            text-align: left;
            border: 1px solid #ddd;
        }

        th {
            background-color: #f8f9fa;
            font-weight: bold;
            color: #333;
        }

        tr:nth-child(even) {
            background-color: #f2f2f2;
        }

        .center {
            display: flex;
            justify-content: center;
            align-items: center;
            flex-direction: column;
        }

        .title-area, .summary-area {
            text-align: center;
            margin-top: 20px;
        }

        .title {
            font-size: 24px;
            font-weight: bold;
            color: #333;
        }

        .summary {
            font-size: 16px;
            color: #666;
        }

        .contents {
            padding: 20px;
        }
    </style>

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
            定義されているセッション一覧を表示する
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



