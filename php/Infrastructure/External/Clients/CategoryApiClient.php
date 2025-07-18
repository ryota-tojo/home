<?php

use Application\Services\ApiService;

require_once $_SERVER['DOCUMENT_ROOT'] . '/config/api_routes.php';

function apiCallCategoryRefer($category_id = null, $groups_id = null, $category_no = null, $offset = null, $limit = null)
{

    $http_method = API_CATEGORY_REFER['HTTP_METHOD'];
    $api_path = API_CATEGORY_REFER['API_PATH'];
    $request_parameter = [
        "category_id" => $category_id,
        "groups_id" => $groups_id,
        "category_no" => $category_no,
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

function apiCallCategoryCount($category_id = null, $groups_id = null, $category_no = null)
{

    $http_method = API_CATEGORY_COUNT['HTTP_METHOD'];
    $api_path = API_CATEGORY_COUNT['API_PATH'];
    $request_parameter = [
        "category_id" => $category_id,
        "groups_id" => $groups_id,
        "category_no" => $category_no,
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

function apiCallCategoryCreate($groups_id = null, $category_no = null, $category_name = null)
{

    $http_method = API_CATEGORY_CREATE['HTTP_METHOD'];
    $api_path = API_CATEGORY_CREATE['API_PATH'];
    $request_parameter = [
        "groups_id" => $groups_id,
        "category_no" => $category_no,
        "category_name" => $category_name,
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

function apiCallCategoryUpdate($category_id = null, $category_no = null, $category_name = null)
{

    $http_method = API_CATEGORY_UPDATE['HTTP_METHOD'];
    $api_path = API_CATEGORY_UPDATE['API_PATH'];
    $request_parameter = [
        "category_id" => $category_id,
        "category_no" => $category_no,
        "category_name" => $category_name,
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

function apiCallCategoryDelete($groups_id = null, $category_id = null)
{

    $http_method = API_CATEGORY_DELETE['HTTP_METHOD'];
    $api_path = API_CATEGORY_DELETE['API_PATH'];
    $request_parameter = [
        "groups_id" => $groups_id,
        "category_id" => $category_id
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

function apiCallCategoryDisable($category_id = null)
{

    $http_method = API_CATEGORY_DISABLE['HTTP_METHOD'];
    $api_path = API_CATEGORY_DISABLE['API_PATH'];
    $request_parameter = [
        "category_id" => $category_id
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

function apiCallCategoryUnDisable($category_id = null)
{

    $http_method = API_CATEGORY_UN_DISABLE['HTTP_METHOD'];
    $api_path = API_CATEGORY_UN_DISABLE['API_PATH'];
    $request_parameter = [
        "category_id" => $category_id
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