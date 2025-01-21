<?php

namespace Application\Services;

class ApiService
{
    public function __construct($path)
    {
        $this->url = API_BASE_URL .$path ;
    }
    public function httpRequest($http_method, $dictionary_data )
    {
        $json_data = json_encode($dictionary_data);
        $ch = curl_init($this->url);

        curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
        curl_setopt($ch, CURLOPT_HTTPHEADER, [
            "Content-Type: application/json; charset=UTF-8",
            "Accept: application/json"
        ]);
        curl_setopt($ch, CURLOPT_CUSTOMREQUEST, $http_method);
        curl_setopt($ch, CURLOPT_POSTFIELDS, $json_data);

        $response = curl_exec($ch);
        if (curl_errno($ch)) {
            echo 'Error:' . curl_error($ch);
        }
        curl_close($ch);

        return $response;

    }

}