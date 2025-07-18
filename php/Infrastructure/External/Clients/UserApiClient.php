<?php

use Application\Services\ApiService;

require_once $_SERVER['DOCUMENT_ROOT'] . '/config/api_routes.php';

function apiCallLoginCheck($user_name, $password)
{

    $http_method = API_LOGIN['HTTP_METHOD'];
    $api_path = API_LOGIN['API_PATH'];
    $request_parameter = [
        "user_name" => $user_name,
        "password" => $password
    ];

    $api_service = new ApiService($api_path);
    $response = $api_service->httpRequest($http_method, $request_parameter);

    $data = json_decode($response, true);

    $response_status = $data['status'];
    $response_message = $data['message'];
    $response_data = $data['data'];

    return [
        "status" => $response_status,
        "message" => $response_message,
        "data" => $response_data
    ];

}

function apiCallUserRefer($user_id = null, $user_name = null, $permission = null, $approval = null, $deleted = null, $groups_id = null, $group_approval = null, $leader = null, $group_affiliation = null, $offset = 0, $limit = 100)
{

    $http_method = API_USER_REFER['HTTP_METHOD'];
    $api_path = API_USER_REFER['API_PATH'];
    $request_parameter = [
        "user_id" => $user_id,
        "user_name" => $user_name,
        "permission" => $permission,
        "approval" => $approval,
        "deleted" => $deleted,
        "groups_id" => $groups_id,
        "group_approval" => $group_approval,
        "leader" => $leader,
        "group_affiliation" => $group_affiliation,
        "offset" => $offset,
        "limit" => $limit
    ];


    $api_service = new ApiService($api_path);
    $response = $api_service->httpRequest($http_method, $request_parameter);

    $data = json_decode($response, true);

    $response_status = $data['status'];
    $response_message = $data['message'];
    $response_data = $data['data'];

    return [
        "status" => $response_status,
        "message" => $response_message,
        "data" => $response_data
    ];
}

function apiCallUserCount($user_id = null, $user_name = null, $permission = null, $approval = null, $deleted = null, $groups_id = null, $group_approval = null, $leader = null,$group_affiliation = null)
{

    $http_method = API_USER_COUNT['HTTP_METHOD'];
    $api_path = API_USER_COUNT['API_PATH'];
    $request_parameter = [
        "user_id" => $user_id,
        "user_name" => $user_name,
        "permission" => $permission,
        "approval" => $approval,
        "deleted" => $deleted,
        "groups_id" => $groups_id,
        "group_approval" => $group_approval,
        "leader" => $leader,
        "group_affiliation" => $group_affiliation,
    ];

    $api_service = new ApiService($api_path);
    $response = $api_service->httpRequest($http_method, $request_parameter);

    $data = json_decode($response, true);

    $response_status = $data['status'];
    $response_message = $data['message'];
    $response_data = $data['data'];

    return [
        "status" => $response_status,
        "message" => $response_message,
        "data" => $response_data
    ];
}

function apiCallUserCreate($user_name = null, $password = null, $permission = null, $approval = null, $delete = null)
{

    $http_method = API_USER_CREATE['HTTP_METHOD'];
    $api_path = API_USER_CREATE['API_PATH'];
    $request_parameter = [
        "user_name" => $user_name,
        "password" => $password,
        "permission" => $permission,
        "approval" => $approval,
        "delete" => $delete
    ];

    $api_service = new ApiService($api_path);
    $response = $api_service->httpRequest($http_method, $request_parameter);

    $data = json_decode($response, true);

    $response_status = $data['status'];
    $response_message = $data['message'];
    $response_data = $data['data'];

    return [
        "status" => $response_status,
        "message" => $response_message,
        "data" => $response_data
    ];
}

function apiCallUserUpdateInfo($user_id, $user_name = null, $password = null, $permission = null, $approval = null, $delete = null)
{

    $http_method = API_USER_UPDATE_INFO['HTTP_METHOD'];
    $api_path = API_USER_UPDATE_INFO['API_PATH'];
    $request_parameter = [
        "user_id" => $user_id,
        "user_name" => $user_name,
        "password" => $password,
        "permission" => $permission,
        "approval" => $approval,
        "delete" => $delete
    ];

    $api_service = new ApiService($api_path);
    $response = $api_service->httpRequest($http_method, $request_parameter);

    $data = json_decode($response, true);

    $response_status = $data['status'];
    $response_message = $data['message'];
    $response_data = $data['data'];

    return [
        "status" => $response_status,
        "message" => $response_message,
        "data" => $response_data
    ];
}

function apiCallUserCreateSetting($user_id, $key, $value = null)
{

    $http_method = API_USER_CREATE_SETTING['HTTP_METHOD'];
    $api_path = API_USER_CREATE_SETTING['API_PATH'];
    $request_parameter = [
        "user_id" => $user_id,
        "setting_key" => $key,
        "setting_value" => $value
    ];

    $api_service = new ApiService($api_path);
    $response = $api_service->httpRequest($http_method, $request_parameter);

    $data = json_decode($response, true);

    $response_status = $data['status'];
    $response_message = $data['message'];
    $response_data = $data['data'];

    return [
        "status" => $response_status,
        "message" => $response_message,
        "data" => $response_data
    ];
}

function apiCallUserUpdateSetting($user_id, $key, $value = null)
{

    $http_method = API_USER_UPDATE_SETTING['HTTP_METHOD'];
    $api_path = API_USER_UPDATE_SETTING['API_PATH'];
    $request_parameter = [
        "user_id" => $user_id,
        "setting_key" => $key,
        "setting_value" => $value
    ];

    $api_service = new ApiService($api_path);
    $response = $api_service->httpRequest($http_method, $request_parameter);

    $data = json_decode($response, true);

    $response_status = $data['status'];
    $response_message = $data['message'];
    $response_data = $data['data'];

    return [
        "status" => $response_status,
        "message" => $response_message,
        "data" => $response_data
    ];
}

function apiCallUserDeleteSetting($user_id, $key = null)
{

    $http_method = API_USER_DELETE_SETTING['HTTP_METHOD'];
    $api_path = API_USER_DELETE_SETTING['API_PATH'];
    $request_parameter = [
        "user_id" => $user_id,
        "setting_key" => $key
    ];

    $api_service = new ApiService($api_path);
    $response = $api_service->httpRequest($http_method, $request_parameter);

    $data = json_decode($response, true);

    $response_status = $data['status'];
    $response_message = $data['message'];
    $response_data = $data['data'];

    return [
        "status" => $response_status,
        "message" => $response_message,
        "data" => $response_data
    ];
}

function apiCallUserDelete($user_id = null)
{

    $http_method = API_USER_DELETE['HTTP_METHOD'];
    $api_path = API_USER_DELETE['API_PATH'];
    $request_parameter = [
        "user_id" => $user_id
    ];

    $api_service = new ApiService($api_path);
    $response = $api_service->httpRequest($http_method, $request_parameter);

    $data = json_decode($response, true);

    $response_status = $data['status'];
    $response_message = $data['message'];
    $response_data = $data['data'];

    return [
        "status" => $response_status,
        "message" => $response_message,
        "data" => $response_data
    ];
}