<?php
require_once $_SERVER['DOCUMENT_ROOT'] . '/config/config.php';
require_once $_SERVER['DOCUMENT_ROOT'] . '/Application/Services/ApiService.php';
require_once $_SERVER['DOCUMENT_ROOT'] . '/Interfaces/Views/Partials/requireApi.php';
require_once $_SERVER['DOCUMENT_ROOT'] . '/Interfaces/Views/Partials/user/create_init_user.php';

function create()
{
    // テストユーザー、所属グループ
    $user_items = require_once $_SERVER['DOCUMENT_ROOT'] . '/Interfaces/Views/Partials/test/test_data/test_user_items.php';
    createInitUser($user_items);
}