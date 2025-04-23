<?php

require $_SERVER['DOCUMENT_ROOT'] . '/config/config.php';
require $_SERVER['DOCUMENT_ROOT'] . '/Application/Services/ApiService.php';
require $_SERVER['DOCUMENT_ROOT'] . '/Application/Services/base64Service.php';
require $_SERVER['DOCUMENT_ROOT'] . '/Interfaces/Views/Partials/requireApi.php';

require $_SERVER['DOCUMENT_ROOT'] . '/Interfaces/Views/Partials/initialization.php';

session_start();

//画面名
$screen_title = "メンテナンス中";

?>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><?php echo $screen_title; ?></title>
    <link rel="stylesheet" href="/Interfaces/Assets/CSS/font.css">
    <link rel="stylesheet" href="/Interfaces/Assets/CSS/login.css">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha3/dist/css/bootstrap.min.css" rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha3/dist/js/bootstrap.bundle.min.js"></script>
    <style>
        html, body {
            height: 100%;
            margin: 0;
        }

        body {
            display: flex;
            flex-direction: column;
            min-height: 100vh;
        }

        main {
            flex: 1;
            display: flex;
            justify-content: center;
            align-items: center;
            text-align: center;
        }

        footer {
            padding: 10px 0;
            text-align: center;
        }

        .contents {
            font-size: 2rem;
            color: #555;
        }
    </style>

</head>
<body style="height:100%;">
<header>
</header>
<main>
    <div class="title-area">
        <h2 class="title"></h2>
    </div>
    <div class="contents">
        <div class="content-row">
            <div class="left">
            </div>
            <div class="center">

            <div>
                メンテナンス中
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

</script>
</body>
</html>


