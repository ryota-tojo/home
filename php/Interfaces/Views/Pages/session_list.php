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
</html>