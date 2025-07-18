<?php
require_once $_SERVER['DOCUMENT_ROOT'] . '/config/config.php';
require_once $_SERVER['DOCUMENT_ROOT'] . '/Application/Services/ApiService.php';
require_once $_SERVER['DOCUMENT_ROOT'] . '/Interfaces/Views/Partials/requireApi.php';
$bf_base_path = "/Interfaces/Assets/CSS/font/";
$bf_font_family = "";
$bf_file = "";
$bf_transform = 1;

$bf_keyword = "";
$bf_random_keyword = "";
$bf_random_probability = "";

$bf_master_setting_api_result = apiCallMasterSettingRefer();
foreach ($bf_master_setting_api_result['data']['setting_list'] as $bf_setting) {
    if ($bf_setting['setting_key'] == 'font_family') {
        $bf_keyword = $bf_setting['setting_value'];
    }
    if ($bf_setting['setting_key'] == 'random_font_family') {
        $bf_random_keyword = $bf_setting['setting_value'];
    }
    if ($bf_setting['setting_key'] == 'random_font_probability') {
        $bf_random_probability = $bf_setting['setting_value'];
    }
}

if($bf_random_probability != "" and $bf_random_keyword != ""){
    $random = rand(1, (int)$bf_random_probability);
    if($random == 1){
        $bf_keyword = $bf_random_keyword;
    }
}

switch ($bf_keyword) {
    case 'anzuiro':
        // あんずいろ
        $bf_font_family = "anzuiro";
        $bf_file = "APJapanesefont.ttf";
        $bf_transform = 1.1;
        break;

    case 'koharuiro':
        // こはるいろサンレイ
        $bf_font_family = "koharuiro";
        $bf_file = "GN-Koharuiro_Sunray.ttf";
        $bf_transform = 1.15;
        break;

    case 'toyomare':
        // とよまーる
        $bf_font_family = "toyomare";
        $bf_file = "Toyomare-Medium.ttf";
        $bf_transform = 0.9;
        break;

    case 'shirokuma':
        // しろくま
        $bf_font_family = "shirokuma";
        $bf_file = "001Shirokuma-Regular.otf";
        $bf_transform = 1;
        break;

    case 'joya':
        // 和音Joyo-R
        $bf_font_family = "joya";
        $bf_file = "WaonJoyo-R.otf";
        $bf_transform = 1;
        break;

    case 'chikara-yowaku':
        // チカラヨワク
        $bf_font_family = "chikara-yowaku";
        $bf_file = "851CHIKARA-YOWAKU_002.ttf";
        $bf_transform = 1;
        break;

    case 'logo-type-gothic':
        // チカラヨワク
        $bf_font_family = "logo-type-gothic";
        $bf_file = "LogoTypeGothic.otf";
        $bf_transform = 1;
        break;

    case 'onryou':
        // 怨霊
        $bf_font_family = "onryou";
        $bf_file = "onryou.TTF";
        $bf_transform = 1;
        break;

    case 'obake':
        // おばけ（カタカナのみ）
        $bf_font_family = "onryou";
        $bf_file = "obake.TTF";
        $bf_transform = 1;
        break;

    default:
        // デフォルト
        $bf_font_family = "system-ui";
        $bf_file = "";
        $bf_transform = 0.9;
        break;
}

$bf_margin = 5;
if($bf_transform < 1){
    $bf_margin = -7;
}

echo "
<style>
    @font-face {
        font-family: '$bf_font_family';
        src: url('{$bf_base_path}{$bf_file}');
    }
    .login-form-title{
        font-family: $bf_font_family;
        transform: scale($bf_transform);
    }
    .title-font{
        font-family: $bf_font_family;
        transform: scale($bf_transform);
    }
    
    .menu-font{
        font-family: $bf_font_family;
        transform: scale($bf_transform);
        margin-left: {$bf_margin}px;
        margin-right: {$bf_margin}px;
    }
    .drop-font{
        font-family: $bf_font_family;
        transform: scale($bf_transform);
    }
        
    .title{
        font-family: $bf_font_family;
        transform: scale($bf_transform);
    }  
        
    .loading-msg{
        font-family: $bf_font_family;
        transform: scale($bf_transform);
    }
    .footer-top, .footer-divider, .footer-bottom{
        font-family: $bf_font_family;
        transform: scale($bf_transform);
    }  
    

    @media (max-width: 1024px) {
        
    }
    
    @media (min-width: 1024px) {
        
    }  
</style>

";

if($bf_keyword == "onryou"){
    echo "
    <style>
    
    @keyframes flash {
      0%, 100% {
        background-color: #1e1c1a;
        text-shadow: none;
      }
      9% {
        background-color: #1e1c1a;
        text-shadow: none;
      }
      9.5% {
        background-color: #fdf8c4;
        text-shadow:
            1px 1px 0 #000,
            2px 2px 0 #000,
            3px 3px 0 #000,
            4px 4px 0 #000,
            5px 5px 5px rgba(0, 0, 0, 0.8);
      }
      10.5% {
        background-color: #1e1c1a;
        text-shadow: none;
      }
      11% {
        background-color: #ffffff;
        text-shadow:
            1px 1px 0 #000,
            2px 2px 0 #000,
            3px 3px 0 #000,
            4px 4px 0 #000,
            5px 5px 5px rgba(0, 0, 0, 0.8);
      }
      18% {
        background-color: #1e1c1a;
        text-shadow: none;
      }
      49% {
        background-color: #1e1c1a;
        text-shadow: none;
      }
      50% {
        background-color: #e4dab2;
        text-shadow:
            1px 1px 0 #000,
            2px 2px 0 #000,
            3px 3px 0 #000,
            4px 4px 0 #000,
            5px 5px 5px rgba(0, 0, 0, 0.8);
      }
      65% {
        background-color: #1e1c1a;
        text-shadow: none;
      }
    }

    .admin-body{
        background-color: #1e1c1a;
        color: #801c24;
        animation: flash 15s infinite;
        transition: background-color 0.2s;
    }
    .body{
        background-color: #1e1c1a;
        color: #801c24;
        animation: flash 7s infinite;
        transition: background-color 0.2s;
    }

    </style>
    ";
}

?>

