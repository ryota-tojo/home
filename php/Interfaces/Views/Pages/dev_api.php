<?php

require $_SERVER['DOCUMENT_ROOT'] . '/config/config.php';
require $_SERVER['DOCUMENT_ROOT'] . '/Application/Services/ApiService.php';

use Application\Services\ApiService;

session_start();

//画面名
$screen_title = "API実行ツール";

// APIパス
$resource_path_list = [
    "api/group/refer",
    "api/group/entry",
    "api/group/update/list",
    "api/group/update/setting",
    "api/group/delete",
    "test"
];

$request_item_no = 10;

// ボタン押下時の処理
$post_http_status = '';
$post_resource_path = '';
if (isset($_POST['request-btn'])) {
    $post_http_status = $_POST['http-status'] ?? '';
    $post_resource_path = $_POST['resource-path'] ?? '';

    $arr = [];
    for ($i = 0; $i < $request_item_no; ++$i) {
        $key = $_POST["key_$i"] ?? '';
        $value = $_POST["value_$i"] ?? '';
        if ($key) {
            $arr[$key] = $value;
        }
    }
    $api_s = new ApiService($post_resource_path);
    $res = $api_s->httpRequest($post_http_status, $arr);

}

?>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><?php echo $screen_title; ?></title>
    <link rel="stylesheet" href="/Interfaces/Assets/CSS/font.css">
    <link rel="stylesheet" href="/Interfaces/Assets/CSS/control.css">
    <link rel="stylesheet" href="/Interfaces/Assets/CSS/login.css">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha3/dist/css/bootstrap.min.css" rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha3/dist/js/bootstrap.bundle.min.js"></script>
</head>
<body>
<header>
</header>
<main>
    <div class="title-area">
        <h2 class="title"><?php echo $screen_title; ?></h2>
    </div>
    <div class="contents">
        <div class="content-row">
            <div class="left">
            </div>
            <div class="center">
                <div class="form-menu-bl1">
                    <div class="form-menu-bl2">
                        <form action="" method="post">
                            <div class="entry-form">
                                <div class="form-area">
                                    <div class="login-form-label">
                                        APIリクエスト
                                    </div>
                                    <hr>
                                    <div class="form-item member-item">
                                        <div class="form-item-label">
                                            <label class="item-label">HTTP メソッド</label>
                                        </div>
                                        <div class="input-group form-item">
                                            <select class="form-select" name="http-status">
                                                <option selected>選択してください</option>
                                                <?php
                                                $post_http_status = $_POST['http-status'] ?? '';
                                                if ($post_http_status == "GET") {
                                                    echo "<option selected value='GET'>GET</option>";
                                                } else {
                                                    echo "<option value='GET'>GET</option>";
                                                }
                                                if ($post_http_status == "POST") {
                                                    echo "<option selected value='POST'>POST</option>";
                                                } else {
                                                    echo "<option value='POST'>POST</option>";
                                                }
                                                ?>
                                            </select>
                                        </div>
                                    </div>
                                </div>

                                <div class="form-area">
                                    <div class="login-form-label">
                                        ベースURL
                                    </div>
                                    <div class="login-form-input">
                                        <input type="text" disabled
                                               class="form-control"
                                               name="base-url"
                                               value=<?php echo API_BASE_URL; ?>>
                                    </div>
                                </div>

                                <div class="form-area">
                                    <div class="form-item member-item">
                                        <div class="form-item-label">
                                            <label class="item-label">リソースパス</label>
                                        </div>
                                        <div class="input-group form-item">
                                            <select class="form-select" name="resource-path" id="src-path">
                                                <?php
                                                $post_resource_path = $_POST['resource-path'] ?? '';
                                                $cnt = 0;
                                                echo "<option selected>選択してください</option>";
                                                foreach ($resource_path_list as $resource_path) {
                                                    if ($post_resource_path == $resource_path) {
                                                        echo "<option selected value='{$resource_path}'>{$resource_path}</option>";
                                                    } else {
                                                        echo "<option value='{$resource_path}'>{$resource_path}</option>";
                                                    }
                                                    $cnt += 1;
                                                }
                                                ?>
                                            </select>
                                        </div>
                                    </div>
                                </div>

                                <br><br>
                                <div class="login-form-label">
                                    リクエストボディ
                                </div>
                                <hr>
                                <?php

                                for ($i = 0; $i < $request_item_no; ++$i) {
                                    $post_key = $_POST["key_$i"] ?? '';
                                    $post_value = $_POST["value_$i"] ?? '';
                                    $hidden = "";
                                    if($post_key==""){
                                        $hidden = "hidden";
                                    }
                                    echo "
                                    <div style='display: flex; margin-bottom: 5px' id='request-form-$i' $hidden>
                                        <div class='form-area'>
                                            <div class='login-form-label'>
                                                key
                                            </div>
                                            <div class='login-form-input'>
                                                <input type='text'
                                                       class='form-control'
                                                       name='key_$i'
                                                       value='$post_key'
                                                >
                                            </div>
                                        </div>
                                        <div class='form-area' style='margin-left: 10px;'>
                                            <div class='login-form-label'>
                                                value
                                            </div>
                                            <div class='login-form-input'>
                                                <input type='text'
                                                       class='form-control'
                                                       name='value_$i'
                                                       value='$post_value'
                                                >
                                            </div>
                                        </div>
                                    </div>
                                    ";
                                }
                                ?>

                                <br>
                                <div class="form-area">
                                    <div class="login-form-btn">
                                        <button type="submit" class="btn btn-primary date-btn-item" name="request-btn">
                                            リクエスト
                                        </button>
                                    </div>
                                </div>
                            </div>
                        </form>

                        <div class="login-form-label">
                            レスポンス
                        </div>
                        <hr>
                        <div>
                            <?php echo $post_http_status . " " . API_BASE_URL . $post_resource_path . "<br>"; ?>
                            <textarea class='form-control' style="height: 200px"><?php $text = $res ?? '';
                                echo $text; ?></textarea>
                        </div>
                        <?php
                        if(isset($res)){
                            $data = json_decode($text, true);
                            echo $data['message'];
                        }
                        ?>
                    </div>
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

</script>

<!--APIごとの設定-->
<script>
    // 値の初期化
    document.getElementById('src-path').addEventListener('change', function () {
        for (let i = 0; i < 10; i++) {
            document.querySelector(`input[name='key_${i}']`).value = "";
            document.querySelector(`input[name='value_${i}']`).value = "";
            document.getElementById(`request-form-${i}`).hidden = true;
        }
    });
    // 初期値の設定
    document.getElementById('src-path').addEventListener('change', function () {
        var selectedValue = this.value;
        if (selectedValue === 'api/group/refer') {
            document.getElementById(`request-form-0`).hidden = false;
            document.querySelector("input[name='key_0']").value = "groups_id";
        }
        if (selectedValue === 'api/group/entry') {
            document.getElementById(`request-form-0`).hidden = false;
            document.getElementById(`request-form-1`).hidden = false;
            document.querySelector("input[name='key_0']").value = "groups_id";
            document.querySelector("input[name='key_1']").value = "group_name";
        }
        if (selectedValue === 'api/group/update/list') {
            document.getElementById(`request-form-0`).hidden = false;
            document.getElementById(`request-form-1`).hidden = false;
            document.querySelector("input[name='key_0']").value = "groups_id";
            document.querySelector("input[name='key_1']").value = "group_name";
        }
        if (selectedValue === 'api/group/update/setting') {
            document.getElementById(`request-form-0`).hidden = false;
            document.getElementById(`request-form-1`).hidden = false;
            document.getElementById(`request-form-2`).hidden = false;
            document.querySelector("input[name='key_0']").value = "groups_id";
            document.querySelector("input[name='key_1']").value = "group_setting_key";
            document.querySelector("input[name='key_2']").value = "group_setting_value";
        }
        if (selectedValue === 'api/group/delete') {
            document.getElementById(`request-form-0`).hidden = false;
            document.querySelector("input[name='key_0']").value = "groups_id";
        }
    });

</script>
</body>
</html>



