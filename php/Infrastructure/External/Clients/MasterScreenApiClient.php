<?php

use Application\Services\ApiService;

require_once $_SERVER['DOCUMENT_ROOT'] . '/Config/api_routes.php';

function apiCallMasterScreenRefer($id = null)
{

    $http_method = API_MASTER_SCREEN_REFER['HTTP_METHOD'];
    $api_path = API_MASTER_SCREEN_REFER['API_PATH'];
    $request_parameter = [
        "screen_id" => $id
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

function apiCallMasterScreenCreate($id,$name,$remarks)
{

    $http_method = API_MASTER_SCREEN_CREATE['HTTP_METHOD'];
    $api_path = API_MASTER_SCREEN_CREATE['API_PATH'];
    $request_parameter = [
        "screen_id" => $id,
        "screen_name" => $name,
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