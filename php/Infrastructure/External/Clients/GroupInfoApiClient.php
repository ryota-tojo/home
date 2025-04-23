<?php

use Application\Services\ApiService;

require_once $_SERVER['DOCUMENT_ROOT'] . '/config/ApiRoutes.php';

function apiCallGroupInfoRefer($groups_id = null, $user_id = null, $offset = 0, $limit = 100)
{

    $http_method = API_GROUP_INFO_REFER['HTTP_METHOD'];
    $api_path = API_GROUP_INFO_REFER['API_PATH'];
    $request_parameter = [
        "groups_id" => $groups_id,
        "user_id" => $user_id,
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

function apiCallGroupInfoCount($groups_id = null, $user_id = null)
{

    $http_method = API_GROUP_INFO_COUNT['HTTP_METHOD'];
    $api_path = API_GROUP_INFO_COUNT['API_PATH'];
    $request_parameter = [
        "groups_id" => $groups_id,
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

function apiCallGroupInfoCreate($groups_id, $user_id, $leader)
{

    $http_method = API_GROUP_INFO_CREATE['HTTP_METHOD'];
    $api_path = API_GROUP_INFO_CREATE['API_PATH'];
    $request_parameter = [
        "groups_id" => $groups_id,
        "user_id" => $user_id,
        "leader" => $leader
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

function apiCallGroupInfoUpdate($groups_id, $user_id, $leader = null, $approval = null)
{

    $http_method = API_GROUP_INFO_UPDATE['HTTP_METHOD'];
    $api_path = API_GROUP_INFO_UPDATE['API_PATH'];
    $request_parameter = [
        "groups_id" => $groups_id,
        "user_id" => $user_id,
        "leader" => $leader,
        "$approval" => $approval
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

function apiCallGroupInfoDelete($groups_id = null, $user_id = null)
{

    $http_method = API_GROUP_INFO_DELETE['HTTP_METHOD'];
    $api_path = API_GROUP_INFO_DELETE['API_PATH'];
    $request_parameter = [
        "groups_id" => $groups_id,
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