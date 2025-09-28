<?php

use Application\Services\ApiService;

require_once $_SERVER['DOCUMENT_ROOT'] . '/Config/api_routes.php';

function apiCallFixedRefer($groups_id, $yyyy)
{

    $http_method = API_FIXED_REFER['HTTP_METHOD'];
    $api_path = API_FIXED_REFER['API_PATH'];
    $request_parameter = [
        "groups_id" => $groups_id,
        "yyyy" => $yyyy
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

function apiCallFixedFixed($groups_id, $yyyy, $mm)
{

    $http_method = API_FIXED_FIXED['HTTP_METHOD'];
    $api_path = API_FIXED_FIXED['API_PATH'];
    $request_parameter = [
        "groups_id" => $groups_id,
        "yyyy" => $yyyy,
        "mm" => $mm
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

function apiCallFixedUnFixed($groups_id, $yyyy, $mm)
{

    $http_method = API_FIXED_UN_FIXED['HTTP_METHOD'];
    $api_path = API_FIXED_UN_FIXED['API_PATH'];
    $request_parameter = [
        "groups_id" => $groups_id,
        "yyyy" => $yyyy,
        "mm" => $mm
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