<?php
require $_SERVER['DOCUMENT_ROOT'] . '/config/config.php';
require $_SERVER['DOCUMENT_ROOT'] . '/Application/Services/ApiService.php';
require $_SERVER['DOCUMENT_ROOT'] . '/Interfaces/Views/Partials/requireApi.php';

use Application\Services\ApiService;

//$api_s = new ApiService("group1");
//$arr = [
//    "a"=>"b"
//];
//$res = $api_s->httpRequest("GET",$arr);
//echo $res . "<br>";




$a = apiCallUserRefer();

foreach ($a['data']['user'] as $user){
    $userInfo = $user['user_info'];
    $userSettings = $user['user_setting'];
    $groupInfo = $user['group_info'];

    echo "ユーザー名: " . $userInfo['user_name'] . PHP_EOL;
    echo "パスワード: " . $userInfo['password'] . PHP_EOL;

    foreach ($userSettings as $setting) {
        echo $setting['setting_key'] . " = " . $setting['setting_value'] . PHP_EOL;
    }
    echo "<br>";

}








//$api_s = new ApiService("/test");
//$arr = [
//    "a"=>"b"
//];
//$res = $api_s->httpRequest("GET",$arr);
//echo $res . "<br>";



?>