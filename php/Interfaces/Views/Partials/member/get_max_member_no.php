<?php

if (session_status() === PHP_SESSION_NONE) {
    session_start();
}

require_once $_SERVER['DOCUMENT_ROOT'] . '/config/config.php';
require_once $_SERVER['DOCUMENT_ROOT'] . '/config/log_config.php';
require_once $_SERVER['DOCUMENT_ROOT'] . '/Application/Services/ApiService.php';
require_once $_SERVER['DOCUMENT_ROOT'] . '/Interfaces/Views/Partials/requireApi.php';

function getMaxMemberNo($groups_is)
{
    $member_max_no=0;
    $result = apiCallMemberRefer(null, $groups_is);
    if ($result['status'] != "error") {
        $datas = $result['data']['member_list'];
    }
    foreach ($datas as $data){
        if((Int)$data['member_no'] == 999){
            continue;
        }
        if($member_max_no<(Int)$data['member_no']){
            $member_max_no=(Int)$data['member_no'];
        }
    }
    return $member_max_no;
}