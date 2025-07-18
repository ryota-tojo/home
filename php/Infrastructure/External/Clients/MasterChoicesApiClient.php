<?php

use Application\Services\ApiService;

require_once $_SERVER['DOCUMENT_ROOT'] . '/config/api_routes.php';

function apiCallMasterChoicesRefer()
{

    $http_method = API_MASTER_CHOICES_REFER['HTTP_METHOD'];
    $api_path = API_MASTER_CHOICES_REFER['API_PATH'];
    $request_parameter = null;

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

function apiCallMasterChoicesCreate($type,$no,$name_pc,$name_sp)
{

    $http_method = API_MASTER_CHOICES_CREATE['HTTP_METHOD'];
    $api_path = API_MASTER_CHOICES_CREATE['API_PATH'];
    $request_parameter = [
        "item_type" => $type,
        "item_no" => $no,
        "item_name_pc" => $name_pc,
        "item_name_sp" => $name_sp
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

function apiCallMasterChoicesUpdate($id,$type,$no,$name_pc,$name_sp)
{

    $http_method = API_MASTER_CHOICES_UPDATE['HTTP_METHOD'];
    $api_path = API_MASTER_CHOICES_UPDATE['API_PATH'];
    $request_parameter = [
        "id" => $id,
        "item_type" => $type,
        "item_no" => $no,
        "item_name_pc" => $name_pc,
        "item_name_sp" => $name_sp
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

function apiCallMasterChoicesDelete($id)
{

    $http_method = API_MASTER_CHOICES_DELETE['HTTP_METHOD'];
    $api_path = API_MASTER_CHOICES_DELETE['API_PATH'];
    $request_parameter = [
        "id" => $id
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