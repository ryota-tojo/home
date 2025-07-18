<?php
if (session_status() === PHP_SESSION_NONE) {
    session_start();
}

require_once $_SERVER['DOCUMENT_ROOT'] . '/config/log_config.php';
require_once $_SERVER['DOCUMENT_ROOT'] . '/Interfaces/Views/Partials/systems/logs/create_logs.php';
require_once $_SERVER['DOCUMENT_ROOT'] . '/Interfaces/Views/Partials/systems/screen/get_screen.php';

$screen_items = getScreen( basename(__FILE__));
$screen_title = $screen_items['name'];
$screen_remarks = $screen_items['remarks'];

$_SESSION = array();
session_destroy();

header("Location: ./login.php");
exit;
?>