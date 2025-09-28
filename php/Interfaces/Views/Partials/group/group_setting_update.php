<?php
require_once $_SERVER['DOCUMENT_ROOT'] . '/Config/config.php';
require_once $_SERVER['DOCUMENT_ROOT'] . '/Application/Services/ApiService.php';
require_once $_SERVER['DOCUMENT_ROOT'] . '/Interfaces/Views/Partials/requireApi.php';

function groupSettingUpdate()
{

    $group_api_count_result = apiCallGroupCount();
    $total = $group_api_count_result['data']['recode_count'];
    $perPage = 10;
    $pages = ceil($total / $perPage);

    for ($i = 0; $i < $pages; $i++) {
        $offset = $i * $perPage;

        $group_api_refer_result = apiCallGroupRefer(null, $offset, $perPage);

        if ($group_api_refer_result['status'] !== 'success') {
            echo "グループ取得に失敗しました<br>";
            continue;
        }

        $group_data = $group_api_refer_result['data']['group'] ?? [];

        if (!is_array($group_data)) {
            echo "無効なグループデータ<br>";
            continue;
        }

        foreach ($group_data as $group) {
            $groups_id = $group['group_list']['groups_id'] ?? null;
            if ($groups_id) {

                $group_settings = [];
                foreach ($group['group_setting'] as $setting) {
                    $group_settings[] = $setting['setting_key'];
                }

                $group_setting_items = require $_SERVER['DOCUMENT_ROOT'] . '/config/InitializationConfig/group_setting_items.php';
                foreach ($group_setting_items as [$key, $value]) {

                    if (in_array($key, $group_settings, true)) {
                        continue;
                    }

                    $result = apiCallGroupCreateSetting($groups_id, $key, $value);
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

    $data = [
        "status" => "success",
        "message" => "すべての所属グループに設定を反映しました",
    ];
    return json_encode($data);
}
