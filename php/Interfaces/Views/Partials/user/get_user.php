<?php
require_once $_SERVER['DOCUMENT_ROOT'] . '/Config/config.php';
require_once $_SERVER['DOCUMENT_ROOT'] . '/Application/Services/ApiService.php';
require_once $_SERVER['DOCUMENT_ROOT'] . '/Interfaces/Views/Partials/requireApi.php';

function isExistUser($user_name)
{
    $result = apiCallUserRefer(null, $user_name);
    if ($result['status'] == "success") {
        return true;
    }
    return false;
}

function getUserByUserId($user_id){
    $result = apiCallUserRefer($user_id);
    if ($result['status'] == "error") {
        $msg = UI_ITEM_USER . "取得：" . UI_ITEM_USER . "情報の取得に失敗しました";
        createLogs(LOG_TYPE_ERROR, $msg);
        return [
            'user_id' => UI_ITEM_LABEL_NULL,
            'user_name' => UI_ITEM_LABEL_NULL,
            'user_password' => UI_ITEM_LABEL_NULL,
            'user_permission' => UI_ITEM_LABEL_NULL,
            'user_approval_flg' => UI_ITEM_LABEL_NULL,
            'user_delete_flg' => UI_ITEM_LABEL_NULL
        ];
    }
    $data_list = $result['data']['user'];
    $first = $data_list[0]['user_info'];
    return [
        'user_id' => $first['user_id'],
        'user_name' => $first['user_name'],
        'password' => $first['password'],
        'permission' => $first['permission'],
        'approval' => $first['approval'],
        'delete' => $first['delete']
    ];
}

function getCurrentUser($user_name)
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

function getUserList(
    $user_id = null,
    $user_name = null,
    $password = null,
    $approval_param = null,
    $deleted_param = null,
    $groups_id = null,
    $group_approval = null,
    $group_member_type = null,
    $affiliation = null,
    $offset = null,
    $limit = null
)
{

    $result = apiCallUserRefer(
        $user_id,
        $user_name,
        $password,
        $approval_param,
        $deleted_param,
        $groups_id,
        $group_approval,
        $group_member_type,
        $affiliation,
        $offset,
        $limit
    );

    if ($result['status'] == "error") {
        $msg = UI_ITEM_USER . "取得：" . UI_ITEM_USER . "情報の取得に失敗しました";
        createLogs(LOG_TYPE_ERROR, $msg);
        return [];
    }
    return $result['data']['user'];
}