<?php
// エラーレポートを有効化（開発中のみ）
ini_set('display_errors', 1);
error_reporting(E_ALL);

// ルーティングの処理
$requestUri = $_SERVER['REQUEST_URI'];
if ($requestUri == '/Public/') {
    header('Location: /Interfaces/Views/Pages/login.php');  // ログインページへのリダイレクト
    exit;
} else {
    echo 'ページが見つかりません';
}