<?php

use Application\Services\ApiService;

require_once $_SERVER['DOCUMENT_ROOT'] . '/config/ApiRoutes.php';

function apiCallNoticeRefer($id = null,$offset = null,$limit=null)
{

    $http_method = API_NOTICE_REFER['HTTP_METHOD'];
    $api_path = API_NOTICE_REFER['API_PATH'];
    $request_parameter = [
        "notice_id" => $id,
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

function apiCallNoticeCount($id = null)
{

    $http_method = API_NOTICE_COUNT['HTTP_METHOD'];
    $api_path = API_NOTICE_COUNT['API_PATH'];
    $request_parameter = [];

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

function apiCallNoticeCreate($title,$content)
{

    $http_method = API_NOTICE_CREATE['HTTP_METHOD'];
    $api_path = API_NOTICE_CREATE['API_PATH'];
    $request_parameter = [
        "title" => $title,
        "content" => $content
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

function apiCallNoticeUpdate($id, $title = null,$content=null)
{

    $http_method = API_NOTICE_UPDATE['HTTP_METHOD'];
    $api_path = API_NOTICE_UPDATE['API_PATH'];
    $request_parameter = [
        "notice_id" => $id,
        "title" => $title,
        "content" => $content
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

function apiCallNoticeDelete($id)
{

    $http_method = API_NOTICE_DELETE['HTTP_METHOD'];
    $api_path = API_NOTICE_DELETE['API_PATH'];
    $request_parameter = [
        "notice_id" => $id
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