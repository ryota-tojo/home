<?php

namespace Application\Services;
require_once $_SERVER['DOCUMENT_ROOT'] . '/Config/log_config.php';
require_once $_SERVER['DOCUMENT_ROOT'] . '/Interfaces/Views/Partials/systems/logs/create_logs.php';

class ApiService
{
    public function __construct($path)
    {
        $this->url = API_BASE_URL . $path;
    }

    public function httpRequest($http_method, $dictionary_data)
    {

        if ($dictionary_data != null) {
            $request = json_encode($dictionary_data);
        } else {
            $request = "{}";
        }

        $ch = curl_init($this->url);

        curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
        curl_setopt($ch, CURLOPT_HTTPHEADER, [
            "Content-Type: application/json; charset=UTF-8",
            "Accept: application/json"
        ]);
        curl_setopt($ch, CURLOPT_CUSTOMREQUEST, $http_method);
        curl_setopt($ch, CURLOPT_POSTFIELDS, $request);

        $response = curl_exec($ch);
        if (curl_errno($ch)) {
            echo 'Error:' . curl_error($ch);
        }
        curl_close($ch);

        $log_type = LOG_TYPE_INFO;
        $data = json_decode($response, true);
        if($data['status']!="success"){
            // 参照件数0件ケースはエラーから除外
            if($data['data']['message'] != "対象のデータが存在しません"){
                $log_type = LOG_TYPE_ERROR;
            }
        }
        createLogs($log_type, "APIリクエストログ $http_method $this->url $request");
        createLogs($log_type, "APIレスポンスログ $http_method $this->url $response");

        return $response;

    }

}