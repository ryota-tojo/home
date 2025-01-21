<?php
require $_SERVER['DOCUMENT_ROOT'] . '/config/config.php';
require $_SERVER['DOCUMENT_ROOT'] . '/Application/Services/ApiService.php';
use Application\Services\ApiService;

$api_s = new ApiService("group1");
$arr = [
    "a"=>"b"
];
$res = $api_s->httpRequest("GET",$arr);
echo $res . "<br>";

//$api_s = new ApiService("/test");
//$arr = [
//    "a"=>"b"
//];
//$res = $api_s->httpRequest("GET",$arr);
//echo $res . "<br>";



?>