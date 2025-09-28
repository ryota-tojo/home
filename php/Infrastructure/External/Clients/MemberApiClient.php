<?php

use Application\Services\ApiService;

require_once $_SERVER['DOCUMENT_ROOT'] . '/Config/api_routes.php';

function apiCallMemberRefer($member_id = null, $groups_id = null, $member_no = null, $offset = null, $limit = null)
{

    $http_method = API_MEMBER_REFER['HTTP_METHOD'];
    $api_path = API_MEMBER_REFER['API_PATH'];
    $request_parameter = [
        "member_id" => $member_id,
        "groups_id" => $groups_id,
        "member_no" => $member_no,
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

function apiCallMemberCount($member_id = null, $groups_id = null, $member_no = null)
{

    $http_method = API_MEMBER_COUNT['HTTP_METHOD'];
    $api_path = API_MEMBER_COUNT['API_PATH'];
    $request_parameter = [
        "member_id" => $member_id,
        "groups_id" => $groups_id,
        "member_no" => $member_no,
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

function apiCallMemberCreate($groups_id = null, $member_no = null, $member_name = null)
{

    $http_method = API_MEMBER_CREATE['HTTP_METHOD'];
    $api_path = API_MEMBER_CREATE['API_PATH'];
    $request_parameter = [
        "groups_id" => $groups_id,
        "member_no" => $member_no,
        "member_name" => $member_name,
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

function apiCallMemberUpdate($member_id = null, $member_no = null, $member_name = null)
{

    $http_method = API_MEMBER_UPDATE['HTTP_METHOD'];
    $api_path = API_MEMBER_UPDATE['API_PATH'];
    $request_parameter = [
        "member_id" => $member_id,
        "member_no" => $member_no,
        "member_name" => $member_name,
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

function apiCallMemberDelete($groups_id = null, $member_id = null)
{

    $http_method = API_MEMBER_DELETE['HTTP_METHOD'];
    $api_path = API_MEMBER_DELETE['API_PATH'];
    $request_parameter = [
        "groups_id" => $groups_id,
        "member_id" => $member_id
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

function apiCallMemberDisable($member_id = null)
{

    $http_method = API_MEMBER_DISABLE['HTTP_METHOD'];
    $api_path = API_MEMBER_DISABLE['API_PATH'];
    $request_parameter = [
        "member_id" => $member_id
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

function apiCallMemberUnDisable($member_id = null)
{

    $http_method = API_MEMBER_UN_DISABLE['HTTP_METHOD'];
    $api_path = API_MEMBER_UN_DISABLE['API_PATH'];
    $request_parameter = [
        "member_id" => $member_id
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