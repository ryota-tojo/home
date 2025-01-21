<?php
$screen_title = "タイトル";

// 管理者判定
$admin_flag = 0

?>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><?php echo $screen_title; ?></title>
    <link rel="stylesheet" href="/新しいフォルダー/Interfaces/Assets/CSS/font.css">
    <link rel="stylesheet" href="/新しいフォルダー/Interfaces/Assets/CSS/home.css">
    <link rel="stylesheet" href="/新しいフォルダー/Interfaces/Assets/CSS/input_form.css">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha3/dist/css/bootstrap.min.css" rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha3/dist/js/bootstrap.bundle.min.js"></script>
</head>

<body class="<?php if ($admin_flag == 1) {
    echo 'admin-body';
} else {
    echo 'body';
} ?>">
<header>
    <?php require $_SERVER['DOCUMENT_ROOT'] . '/includes/nav.php'; ?>
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
                <form action="" method="post">
                    <div class="form-area">


                        <h6 class="form-title">入力フォーム</h6>
                        <hr>

                        <!-- 入力パターン -->
                        <div class="form-item">
                            <div class="form-item-label">
                                <label class="item-label">入力パターン</label>
                            </div>
                            <div class="input-group form-item">
                                <div class="button-items">
                                    <button type="button" class="btn btn-secondary" name="">button1</button>
                                    <button type="button" class="btn btn-secondary" name="">button2</button>
                                </div>
                            </div>
                        </div>

                        <!-- 日付 -->
                        <div class="form-item">
                            <div class="form-item-label">
                                <label class="item-label">日付</label>
                            </div>
                            <div class="input-group form-item">
                                <div class="date-form">
                                    <input type="date" class="form-control date-item" name="date"/>
                                </div>
                                <div class="date-btn-form">
                                    <button type="button" class="btn btn-primary date-btn-item" name="today-btn">
                                        当日
                                    </button>
                                </div>
                            </div>
                        </div>


                        <!-- 購入者 -->
                        <div class="form-item member-item">
                            <div class="form-item-label">
                                <label class="item-label">購入者</label>
                            </div>
                            <div class="input-group form-item">
                                <select class="form-select" name="member">
                                    <option selected>選択してください</option>
                                    <option value="1">購入者１</option>
                                    <option value="2">購入者２</option>
                                    <option value="3">購入者３</option>
                                </select>
                            </div>
                        </div>

                        <div class="pc-form">
                            <!-- 分類 -->
                            <div class="form-item category-item">
                                <div class="form-item-label">
                                    <label class="item-label">分類</label>
                                </div>
                                <div class="input-group form-item">
                                    <select class="form-select" name="category">
                                        <option selected>選択してください</option>
                                        <option value="1">分類１</option>
                                        <option value="2">分類２</option>
                                        <option value="3">分類３</option>
                                    </select>
                                </div>
                            </div>

                            <!-- 種別 -->
                            <div class="form-item type-item">
                                <div class="form-item-label">
                                    <label class="item-label">種別</label>
                                </div>
                                <div class="input-group form-item">
                                    <select class="form-select" name="type">
                                        <option selected>選択してください</option>
                                        <option value="1">出費</option>
                                        <option value="2">収入</option>
                                    </select>
                                </div>
                            </div>

                            <!-- 支払 -->
                            <div class="form-item payment-item">
                                <div class="form-item-label">
                                    <label class="item-label">支払</label>
                                </div>
                                <div class="input-group form-item">
                                    <select class="form-select" name="payment">
                                        <option selected>選択してください</option>
                                        <option value="1">現金</option>
                                        <option value="2">カード</option>
                                        <option value="3">振込</option>
                                        <option value="4">引落し</option>
                                    </select>
                                </div>
                            </div>

                            <!-- 精算 -->
                            <div class="form-item settlement-item">
                                <div class="form-item-label">
                                    <label class="item-label">精算</label>
                                </div>
                                <div class="input-group form-item">
                                    <select class="form-select" name="settlement">
                                        <option selected>選択してください</option>
                                        <option value="1">未精算</option>
                                        <option value="2">精算済</option>
                                    </select>
                                </div>
                            </div>
                        </div>

                        <div class="pc-form">
                            <!-- 金額 -->
                            <div class="form-item amount-item">
                                <div class="form-item-label">
                                    <label class="item-label">金額</label>
                                </div>
                                <div class="input-group form-item">
                                    <input type="text" class="form-control" name="amount">
                                </div>
                            </div>

                            <!-- 備考 -->
                            <div class="form-item remarks-item">
                                <div class="form-item-label">
                                    <label class="item-label">備考</label>
                                </div>
                                <div class="input-group form-item">
                                    <input type="text" class="form-control" name="remarks">
                                </div>
                            </div>
                        </div>
                    </div>

                    <!-- 登録ボタン -->
                    <div class="form-item">
                        <div class="submit-area">
                            <button type="button" class="btn btn-primary" name="entry">登録</button>
                        </div>
                    </div>
                </form>
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



