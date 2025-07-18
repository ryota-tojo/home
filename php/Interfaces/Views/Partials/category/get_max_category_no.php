<?php

if (session_status() === PHP_SESSION_NONE) {
    session_start();
}

require_once $_SERVER['DOCUMENT_ROOT'] . '/config/config.php';
require_once $_SERVER['DOCUMENT_ROOT'] . '/config/log_config.php';
require_once $_SERVER['DOCUMENT_ROOT'] . '/Application/Services/ApiService.php';
require_once $_SERVER['DOCUMENT_ROOT'] . '/Interfaces/Views/Partials/requireApi.php';

function getMaxCategoryNo($groups_is)
{
    $category_max_no=0;
    $result = apiCallCategoryRefer(null, $groups_is);
    if ($result['status'] != "error") {
        $datas = $result['data']['category_list'];
    }
    foreach ($datas as $data){
        if((Int)$data['category_no'] == 999){
            continue;
        }
        if($category_max_no<(Int)$data['category_no']){
            $category_max_no=(Int)$data['category_no'];
        }
    }
    return $category_max_no;
}