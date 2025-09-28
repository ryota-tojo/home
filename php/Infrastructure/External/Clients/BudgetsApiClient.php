<?php

use Application\Services\ApiService;

require_once $_SERVER['DOCUMENT_ROOT'] . '/Config/api_routes.php';

function apiCallBudgetsRefer($groups_id = null, $yyyy = null, $mm = null, $category_id = null)
{

    $http_method = API_BUDGETS_REFER['HTTP_METHOD'];
    $api_path = API_BUDGETS_REFER['API_PATH'];
    $request_parameter = [
        "groups_id" => $groups_id,
        "yyyy" => $yyyy,
        "mm" => $mm,
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

function apiCallBudgetsCreate($groups_id, $yyyy, $mm, $category_id, $amount)
{

    $http_method = API_BUDGETS_CREATE['HTTP_METHOD'];
    $api_path = API_BUDGETS_CREATE['API_PATH'];
    $request_parameter = [
        "groups_id" => $groups_id,
        "yyyy" => $yyyy,
        "mm" => $mm,
        "category_id" => $category_id,
        "amount" => $amount
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

function apiCallBudgetsUpdate($groups_id, $yyyy, $mm, $category_id, $amount = null)
{

    $http_method = API_BUDGETS_UPDATE['HTTP_METHOD'];
    $api_path = API_BUDGETS_UPDATE['API_PATH'];
    $request_parameter = [
        "groups_id" => $groups_id,
        "yyyy" => $yyyy,
        "mm" => $mm,
        "category_id" => $category_id,
        "amount" => $amount
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

function apiCallBudgetsDelete($groups_id, $yyyy = null, $mm = null, $category_id = null)
{

    $http_method = API_BUDGETS_DELETE['HTTP_METHOD'];
    $api_path = API_BUDGETS_DELETE['API_PATH'];
    $request_parameter = [
        "groups_id" => $groups_id,
        "yyyy" => $yyyy,
        "mm" => $mm,
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