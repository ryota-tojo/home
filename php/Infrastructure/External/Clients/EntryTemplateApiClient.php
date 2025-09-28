<?php

use Application\Services\ApiService;

require_once $_SERVER['DOCUMENT_ROOT'] . '/Config/api_routes.php';

function apiCallEntryTemplateRefer($groups_id = null, $template_id = null)
{

    $http_method = API_ENTRY_TEMPLATE_REFER['HTTP_METHOD'];
    $api_path = API_ENTRY_TEMPLATE_REFER['API_PATH'];
    $request_parameter = [
        "groups_id" => $groups_id,
        "template_id" => $template_id
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

function apiCallEntryTemplateCreate($groups_id, $template_no, $template_id, $template_name, $member_id, $category_id, $type, $payment, $settlement, $amount, $remarks, $use
)
{

    $http_method = API_ENTRY_TEMPLATE_CREATE['HTTP_METHOD'];
    $api_path = API_ENTRY_TEMPLATE_CREATE['API_PATH'];
    $request_parameter = [
        "groups_id" => $groups_id,
        "template_no" => $template_no,
        "template_id" => $template_id,
        "template_name" => $template_name,
        "member_id" => $member_id,
        "category_id" => $category_id,
        "type" => $type,
        "payment" => $payment,
        "settlement" => $settlement,
        "amount" => $amount,
        "remarks" => $remarks,
        "use" => $use,
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

function apiCallEntryTemplateUpdate($groups_id, $template_id, $template_no=null, $template_name = null, $member_id = null, $category_id = null, $type = null, $payment = null, $settlement = null, $amount = null, $remarks = null, $use = null
)
{

    $http_method = API_ENTRY_TEMPLATE_UPDATE['HTTP_METHOD'];
    $api_path = API_ENTRY_TEMPLATE_UPDATE['API_PATH'];
    $request_parameter = [
        "groups_id" => $groups_id,
        "template_no" => $template_no,
        "template_id" => $template_id,
        "template_name" => $template_name,
        "member_id" => $member_id,
        "category_id" => $category_id,
        "type" => $type,
        "payment" => $payment,
        "settlement" => $settlement,
        "amount" => $amount,
        "remarks" => $remarks,
        "use" => $use,
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

function apiCallEntryTemplateUsage($groups_id, $template_id)
{

    $http_method = API_ENTRY_TEMPLATE_USAGE['HTTP_METHOD'];
    $api_path = API_ENTRY_TEMPLATE_USAGE['API_PATH'];
    $request_parameter = [
        "groups_id" => $groups_id,
        "template_id" => $template_id
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

function apiCallEntryTemplateUnUsage($groups_id, $template_id)
{

    $http_method = API_ENTRY_TEMPLATE_UN_USAGE['HTTP_METHOD'];
    $api_path = API_ENTRY_TEMPLATE_UN_USAGE['API_PATH'];
    $request_parameter = [
        "groups_id" => $groups_id,
        "template_id" => $template_id
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

function apiCallEntryTemplateDisable($groups_id, $template_id)
{

    $http_method = API_ENTRY_TEMPLATE_DISABLE['HTTP_METHOD'];
    $api_path = API_ENTRY_TEMPLATE_DISABLE['API_PATH'];
    $request_parameter = [
        "groups_id" => $groups_id,
        "template_id" => $template_id
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

function apiCallEntryTemplateUnDisable($groups_id, $template_id)
{

    $http_method = API_ENTRY_TEMPLATE_UN_DISABLE['HTTP_METHOD'];
    $api_path = API_ENTRY_TEMPLATE_UN_DISABLE['API_PATH'];
    $request_parameter = [
        "groups_id" => $groups_id,
        "template_id" => $template_id
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

function apiCallEntryTemplateDelete($groups_id, $template_id=null)
{

    $http_method = API_ENTRY_TEMPLATE_DELETE['HTTP_METHOD'];
    $api_path = API_ENTRY_TEMPLATE_DELETE['API_PATH'];
    $request_parameter = [
        "groups_id" => $groups_id,
        "template_id" => $template_id
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

