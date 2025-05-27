<?php
require_once $_SERVER['DOCUMENT_ROOT'] . '/config/config.php';
require_once $_SERVER['DOCUMENT_ROOT'] . '/Application/Services/api_service.php';
require_once $_SERVER['DOCUMENT_ROOT'] . '/Interfaces/Views/Partials/requireApi.php';
require_once $_SERVER['DOCUMENT_ROOT'] . '/Interfaces/Views/Partials/group/group_create.php';
require_once $_SERVER['DOCUMENT_ROOT'] . '/Interfaces/Views/Partials/user/user_create.php';

function initialization(){

    $master_setting_refer_result = apiCallMasterSettingRefer();
    if($master_setting_refer_result['status'] == "error"){

        // 設定マスタ
        $master_setting_items = require_once $_SERVER['DOCUMENT_ROOT'] . '/config/InitializationConfig/master_setting_items.php';
        foreach ($master_setting_items as [$key, $value, $description]) {
            apiCallMasterSettingCreate($key, $value, $description);
        }

        // 選択肢マスタ
        $master_choices_items = require_once $_SERVER['DOCUMENT_ROOT'] . '/config/InitializationConfig/master_choices_items.php';
        foreach ($master_choices_items as [$type, $no, $name_pc, $name_sp]) {
            apiCallMasterChoicesCreate($type, $no, $name_pc, $name_sp);
        }

        // お知らせ
        $notice_items = require_once $_SERVER['DOCUMENT_ROOT'] . '/config/InitializationConfig/notice_items.php';
        foreach ($notice_items as [$title, $content]) {
            apiCallNoticeCreate($title, $content);
        }

        // adminユーザー
        $admin_user_id = null;
        $admin_user_items = require_once $_SERVER['DOCUMENT_ROOT'] . '/config/InitializationConfig/admin_user_items.php';
        foreach ($admin_user_items as [$user_name, $password,$permission,$approval,$deleted]) {
            userEntry($user_name, $password,$permission,$approval,$deleted);
        }

        // admin用所属グループ
        $admin_group_items = require_once $_SERVER['DOCUMENT_ROOT'] . '/config/InitializationConfig/admin_group_items.php';
        foreach ($admin_group_items as [$groups_id,$name, $pw]) {
            groupEntry($admin_user_id,$groups_id,$name, $pw);
        }

//        $groups_id="admin_group";
//        $group_name="管理者グループ";
//        $group_password="maintenance";
//        groupEntry($admin_user_id,$groups_id,$group_name,$group_password);

//        // テストデータ
//        // 未所属
//        apiCallUserCreate("hoge001","1234567890",0,1);
//        $admin_user_id = null;
//        $user_refer_result = apiCallUserRefer(null,"hoge001");
//        foreach ($user_refer_result['data']['user'] as $user) {
//            $userInfo = $user['user_info'];
//            $admin_user_id = $userInfo['user_id'];
//        }
//
//        // 所属
//        apiCallUserCreate("hoge002","1234567890",0,1);
//        $admin_user_id = null;
//        $user_refer_result = apiCallUserRefer(null,"hoge002");
//        foreach ($user_refer_result['data']['user'] as $user) {
//            $userInfo = $user['user_info'];
//            $admin_user_id = $userInfo['user_id'];
//        }
//        $groups_id="hoge002";
//        $group_name="hoge002";
//        $group_password="hoge002";
//        groupEntry($admin_user_id,$groups_id,$group_name,$group_password);
//
        for ($i = 1; $i <= 109; $i++) {
            $user_name = "user" . str_pad($i, 3, "0", STR_PAD_LEFT); // 001, 002, ..., 100
            $groups_id="group" . str_pad($i, 3, "0", STR_PAD_LEFT);;
            $group_name="グループ" . str_pad($i, 3, "0", STR_PAD_LEFT);;
            $group_password="1234567890";

            $result=userEntry($user_name,"1234567890",0,1,0);
            $data = json_decode($result, true);
            groupEntry($data['user_id'],$groups_id,$group_name,$group_password);
        }
    }
}