<?php

use Application\Services\ApiService;

require_once $_SERVER['DOCUMENT_ROOT'] . '/config/api_routes.php';

function apiCallGroupRefer($groups_id = null, $offset = 0, $limit = 100)
{

    $http_method = API_GROUP_REFER['HTTP_METHOD'];
    $api_path = API_GROUP_REFER['API_PATH'];
    $request_parameter = [
        "groups_id" => $groups_id,
        "offset" => $offset,
        "limit" => $limit,
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

function apiCallGroupCount($groups_id = null)
{

    $http_method = API_GROUP_COUNT['HTTP_METHOD'];
    $api_path = API_GROUP_COUNT['API_PATH'];
    $request_parameter = [
        "groups_id" => $groups_id
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

function apiCallGroupCreate($groups_id, $group_name, $group_password)
{

    $http_method = API_GROUP_CREATE['HTTP_METHOD'];
    $api_path = API_GROUP_CREATE['API_PATH'];
    $request_parameter = [
        "groups_id" => $groups_id,
        "group_name" => $group_name,
        "group_password" => $group_password
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

function apiCallGroupUpdateList($groups_id, $group_name = null, $group_password = null)
{

    $http_method = API_GROUP_UPDATE_LIST['HTTP_METHOD'];
    $api_path = API_GROUP_UPDATE_LIST['API_PATH'];
    $request_parameter = [
        "groups_id" => $groups_id,
        "group_name" => $group_name,
        "group_password" => $group_password
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

function apiCallGroupCreateSetting($groups_id, $setting_key, $setting_value = null)
{

    $http_method = API_GROUP_CREATE_SETTING['HTTP_METHOD'];
    $api_path = API_GROUP_CREATE_SETTING['API_PATH'];
    $request_parameter = [
        "groups_id" => $groups_id,
        "setting_key" => $setting_key,
        "setting_value" => $setting_value
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

function apiCallGroupUpdateSetting($groups_id, $setting_key = null, $setting_value = null)
{

    $http_method = API_GROUP_UPDATE_SETTING['HTTP_METHOD'];
    $api_path = API_GROUP_UPDATE_SETTING['API_PATH'];
    $request_parameter = [
        "groups_id" => $groups_id,
        "setting_key" => $setting_key,
        "setting_value" => $setting_value
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

function apiCallGroupDeleteSetting($groups_id, $setting_key = null)
{

    $http_method = API_GROUP_DELETE_SETTING['HTTP_METHOD'];
    $api_path = API_GROUP_DELETE_SETTING['API_PATH'];
    $request_parameter = [
        "groups_id" => $groups_id,
        "setting_key" => $setting_key
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

function apiCallGroupDelete($groups_id)
{

    $http_method = API_GROUP_DELETE['HTTP_METHOD'];
    $api_path = API_GROUP_DELETE['API_PATH'];
    $request_parameter = [
        "groups_id" => $groups_id
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