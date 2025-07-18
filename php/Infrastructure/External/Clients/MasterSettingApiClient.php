<?php

use Application\Services\ApiService;

require_once $_SERVER['DOCUMENT_ROOT'] . '/config/api_routes.php';

function apiCallMasterSettingRefer($key = null)
{

    $http_method = API_MASTER_SETTING_REFER['HTTP_METHOD'];
    $api_path = API_MASTER_SETTING_REFER['API_PATH'];
    $request_parameter = [
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

function apiCallMasterSettingCreate($key,$value,$remarks)
{

    $http_method = API_MASTER_SETTING_CREATE['HTTP_METHOD'];
    $api_path = API_MASTER_SETTING_CREATE['API_PATH'];
    $request_parameter = [
        "setting_key" => $key,
        "setting_value" => $value,
        "remarks" => $remarks
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

function apiCallMasterSettingUpdate($key, $value = null,$remarks=null)
{

    $http_method = API_MASTER_SETTING_UPDATE['HTTP_METHOD'];
    $api_path = API_MASTER_SETTING_UPDATE['API_PATH'];
    $request_parameter = [
        "setting_key" => $key,
        "setting_value" => $value,
        "remarks" => $remarks
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

function apiCallMasterSettingDelete($key = null)
{

    $http_method = API_MASTER_SETTING_DELETE['HTTP_METHOD'];
    $api_path = API_MASTER_SETTING_DELETE['API_PATH'];
    $request_parameter = [
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