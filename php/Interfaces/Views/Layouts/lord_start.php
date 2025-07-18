<?php
require_once $_SERVER['DOCUMENT_ROOT'] . '/config/config.php';
require_once $_SERVER['DOCUMENT_ROOT'] . '/Application/Services/ApiService.php';
require_once $_SERVER['DOCUMENT_ROOT'] . '/Interfaces/Views/Partials/requireApi.php';

$loading_delay_seconds = "";
$bf_master_setting_api_result = apiCallMasterSettingRefer();
foreach ($bf_master_setting_api_result['data']['setting_list'] as $bf_setting) {
    if ($bf_setting['setting_key'] == 'loading_delay_seconds') {
        $loading_delay_seconds = $bf_setting['setting_value'];
    }
}

?>

<link rel="stylesheet" href="/Interfaces/Assets/CSS/load.css">
<style>
    .loading {
        animation: suddenFadeIn <?php echo (int)$loading_delay_seconds; ?>s forwards;
    }
</style>
    <div class="loading" id="loading">
    <div class="loading-area">
        <div class="img-area">
            <img src='/Public/img/yuzuki_hirameki.gif' alt='No Image' class='load-icon' style='margin-left: 30px'>
        </div>
        <div class="loading-content">
            <p class="loading-msg">よみこみ中...</p>
        </div>
    </div>
</div>