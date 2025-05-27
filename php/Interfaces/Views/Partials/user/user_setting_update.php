<?php
require_once $_SERVER['DOCUMENT_ROOT'] . '/config/config.php';
require_once $_SERVER['DOCUMENT_ROOT'] . '/Application/Services/api_service.php';
require_once $_SERVER['DOCUMENT_ROOT'] . '/Interfaces/Views/Partials/requireApi.php';

function userSettingUpdate()
{

    $user_api_count_result = apiCallUserCount();
    $total = $user_api_count_result['data']['recode_count'];
    $perPage = 10;
    $pages = ceil($total / $perPage);

    for ($i = 0; $i < $pages; $i++) {
        $offset = $i * $perPage;

        $user_api_refer_result = apiCallUserRefer(null, null, null, null, null, null, $offset, $perPage);

        if ($user_api_refer_result['status'] !== 'success') {
            echo "ユーザー取得に失敗しました<br>";
            continue;
        }

        $user_data = $user_api_refer_result['data']['user'] ?? [];

        if (!is_array($user_data)) {
            echo "無効なユーザーデータ<br>";
            continue;
        }

        foreach ($user_data as $user) {
            $user_id = $user['user_info']['user_id'] ?? null;
            if ($user_id) {

                $user_settings = [];
                foreach ($user['user_setting'] as $setting) {
                    $user_settings[] = $setting['setting_key'];
                }

                $user_setting_items = require $_SERVER['DOCUMENT_ROOT'] . '/config/InitializationConfig/user_setting_items.php';
                foreach ($user_setting_items as [$key, $value]) {

                    if (in_array($key, $user_settings, true)) {
                        continue;
                    }

                    $result = apiCallUserCreateSetting($user_id, $key, $value);
                    if ($result['status'] !== 'success') {
                        $errors[] = "設定 {$key} の登録に失敗しました";
                    }
                }
                if (!empty($errors)) {
                    return json_encode([
                        'status' => 'error',
                        'message' => implode("\n", $errors)
                    ]);
                }


            }
        }
    }
}
