<?php

use Application\Services\ApiService;

require_once $_SERVER['DOCUMENT_ROOT'] . '/Config/api_routes.php';

function apiCallShoppingRefer($shopping_id = null, $groups_id = null, $user_id = null, $shopping_date = null, $member_id = null, $category_id = null, $type = null, $payment = null, $settlement = null, $min_amount = null, $max_amount = null, $remarks = null, $offset = null, $limit = null)
{

    $http_method = API_SHOPPING_REFER['HTTP_METHOD'];
    $api_path = API_SHOPPING_REFER['API_PATH'];
    $request_parameter = [
        "shopping_id"   => $shopping_id,
        "groups_id"     => $groups_id,
        "user_id"       => $user_id,
        "shopping_date" => $shopping_date,
        "member_id"     => $member_id,
        "category_id"   => $category_id,
        "type"          => $type,
        "payment"       => $payment,
        "settlement"    => $settlement,
        "min_amount"    => $min_amount,
        "max_amount"    => $max_amount,
        "remarks"       => $remarks,
        "offset"        => $offset,
        "limit"         => $limit
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

function apiCallShoppingCount($shopping_id = null, $groups_id = null, $user_id = null, $shopping_date = null, $member_id = null, $category_id = null, $type = null, $payment = null, $settlement = null, $min_amount = null, $max_amount = null, $remarks = null, $offset = null, $limit = null)
{

    $http_method = API_SHOPPING_COUNT['HTTP_METHOD'];
    $api_path = API_SHOPPING_COUNT['API_PATH'];
    $request_parameter = [
        "shopping_id"   => $shopping_id,
        "groups_id"     => $groups_id,
        "user_id"       => $user_id,
        "shopping_date" => $shopping_date,
        "member_id"     => $member_id,
        "category_id"   => $category_id,
        "type"          => $type,
        "payment"       => $payment,
        "settlement"    => $settlement,
        "min_amount"    => $min_amount,
        "max_amount"    => $max_amount,
        "remarks"       => $remarks,
        "offset"        => $offset,
        "limit"         => $limit
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

function apiCallShoppingAllCategoryRefer($groups_id, $yyyy, $mm)
{

    $http_method = API_SHOPPING_ALL_CATEGORY['HTTP_METHOD'];
    $api_path = API_SHOPPING_ALL_CATEGORY['API_PATH'];
    $request_parameter = [
        "groups_id"   => $groups_id,
        "yyyy"     => $yyyy,
        "mm"       => $mm
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

function apiCallShoppingAllMemberRefer($groups_id, $yyyy, $mm)
{

    $http_method = API_SHOPPING_ALL_MEMBER['HTTP_METHOD'];
    $api_path = API_SHOPPING_ALL_MEMBER['API_PATH'];
    $request_parameter = [
        "groups_id"   => $groups_id,
        "yyyy"     => $yyyy,
        "mm"       => $mm
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

function apiCallShoppingDuplicationCheck($groups_id, $shopping_date, $member_id, $category_id, $type, $payment, $settlement, $amount, $remarks)
{

    $http_method = API_SHOPPING_DUPLICATION_CHECK['HTTP_METHOD'];
    $api_path = API_SHOPPING_DUPLICATION_CHECK['API_PATH'];
    $request_parameter = [
        "groups_id" => $groups_id,
        "shopping_date" => $shopping_date,
        "member_id" => $member_id,
        "category_id" => $category_id,
        "type" => $type,
        "payment" => $payment,
        "settlement" => $settlement,
        "amount" => $amount,
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

function apiCallShoppingCreate($groups_id, $user_id, $shopping_date, $member_id, $category_id, $type, $payment, $settlement, $amount, $remarks)
{

    $http_method = API_SHOPPING_CREATE['HTTP_METHOD'];
    $api_path = API_SHOPPING_CREATE['API_PATH'];
    $request_parameter = [
        "groups_id" => $groups_id,
        "user_id" => $user_id,
        "shopping_date" => $shopping_date,
        "member_id" => $member_id,
        "category_id" => $category_id,
        "type" => $type,
        "payment" => $payment,
        "settlement" => $settlement,
        "amount" => $amount,
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

function apiCallShoppingUpdate($shopping_id, $groups_id, $user_id, $shopping_date = null, $member_id = null, $category_id = null, $type = null, $payment = null, $settlement = null, $amount = null, $remarks = null, $fixed = null)
{

    $http_method = API_SHOPPING_UPDATE['HTTP_METHOD'];
    $api_path = API_SHOPPING_UPDATE['API_PATH'];
    $request_parameter = [
        "shopping_id" => $shopping_id,
        "groups_id" => $groups_id,
        "user_id" => $user_id,
        "shopping_date" => $shopping_date,
        "member_id" => $member_id,
        "category_id" => $category_id,
        "type" => $type,
        "payment" => $payment,
        "settlement" => $settlement,
        "amount" => $amount,
        "remarks" => $remarks,
        "fixed" => $fixed
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

function apiCallShoppingDelete($shopping_id = null, $groups_id = null, $yyyy = null, $mm = null)
{

    $http_method = API_SHOPPING_DELETE['HTTP_METHOD'];
    $api_path = API_SHOPPING_DELETE['API_PATH'];
    $request_parameter = [
        "shopping_id" => $shopping_id,
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