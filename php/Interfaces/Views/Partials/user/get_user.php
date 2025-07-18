<?php
require_once $_SERVER['DOCUMENT_ROOT'] . '/config/config.php';
require_once $_SERVER['DOCUMENT_ROOT'] . '/Application/Services/ApiService.php';
require_once $_SERVER['DOCUMENT_ROOT'] . '/Interfaces/Views/Partials/requireApi.php';

function getUser($user_name)
{

    $user_refer_api_result = apiCallUserRefer(null, $user_name);

    $_SESSION['user_id'] = null;
    $_SESSION['user_name'] = null;
    $_SESSION['user_password'] = null;
    $_SESSION['user_permission'] = null;
    $_SESSION['user_approval_flg'] = null;
    $_SESSION['user_delete_flg'] = null;

    $_SESSION['user_settings'] = null;
    $_SESSION['user_groups_id'] = null;
    $_SESSION['user_group_leader'] = null;
    $_SESSION['user_group_approval_flg'] = null;

    foreach ($user_refer_api_result['data']['user'] as $user) {
        $userInfo = $user['user_info'];
        $userSettings = $user['user_setting'];
        $groupInfoList = $user['group_info'];

        $_SESSION['user_id'] = $userInfo['user_id'];
        $_SESSION['user_name'] = $userInfo['user_name'];
        $_SESSION['user_password'] = $userInfo['password'];
        $_SESSION['user_permission'] = $userInfo['permission'];
        $_SESSION['user_approval_flg'] = $userInfo['approval'];
        $_SESSION['user_delete_flg'] = $userInfo['delete'];

        $_SESSION['user_setting'] = $userSettings;

        foreach ($groupInfoList as $groupInfo) {
            $_SESSION['user_groups_id'] = $groupInfo['groups_id'];
            $_SESSION['user_group_leader'] = $groupInfo['leader'];
            $_SESSION['user_group_approval_flg'] = $groupInfo['approval'];
        }
    }

}