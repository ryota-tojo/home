<?php
require_once $_SERVER['DOCUMENT_ROOT'] . '/config/config.php';
require_once $_SERVER['DOCUMENT_ROOT'] . '/Application/Services/ApiService.php';
require_once $_SERVER['DOCUMENT_ROOT'] . '/Interfaces/Views/Partials/requireApi.php';
require_once $_SERVER['DOCUMENT_ROOT'] . '/Interfaces/Views/Partials/group/groupEntry.php';

function initialization(){

    $master_setting_refer_result = apiCallMasterSettingRefer();
    if($master_setting_refer_result['status'] == "error"){

        // 設定マスタ
        // 初期設定
        apiCallMasterSettingCreate("initialization","1","初期設定フラグ");

        // メンテナンス
        apiCallMasterSettingCreate("maintenance", "0", "メンテナンス判定");

        // 通知
        apiCallMasterSettingCreate("notification_send_flg", "0", "通知フラグ");
        apiCallMasterSettingCreate("notification_url", "https://default", "通知URL");
        apiCallMasterSettingCreate("notification_token", "", "通知トークン");

        // システム設定
        apiCallMasterSettingCreate("login_failure_limit", "5", "ログイン失敗許容回数");
        apiCallMasterSettingCreate("lording_layout", "0", "ロード画面レイアウトパターン");
        apiCallMasterSettingCreate("admin_userdata_view", "10", "管理者画面: ユーザーデータ表示数/1ページ");
        apiCallMasterSettingCreate("admin_groupdata_view", "10", "管理者画面: 所属グループデータ表示数/1ページ");
        apiCallMasterSettingCreate("admin_notice_view", "5", "管理者画面: お知らせデフォルト表示数");
        apiCallMasterSettingCreate("admin_notice_default_title", "お知らせ", "管理者画面: お知らせタイトルデフォルト値");
        apiCallMasterSettingCreate("admin_notice_default_content", "XXXXX", "管理者画面: お知らせ内容デフォルト値");
        apiCallMasterSettingCreate("user_input_history_view", "10", "ユーザー画面： 購入データ入力 - 入力履歴表示数");
        apiCallMasterSettingCreate("user_management_view", "20", "ユーザー画面： 購入データ管理 - データ表示数/1ページ");
        apiCallMasterSettingCreate("user_analysis_graph_size_pc_width", "750", "ユーザー画面： 購入データ分析 - PC表示: グラフ幅");
        apiCallMasterSettingCreate("user_analysis_graph_size_pc_height", "200", "ユーザー画面： 購入データ分析 - PC表示: グラフ高さ");
        apiCallMasterSettingCreate("user_analysis_graph_size_sp_width", "320", "ユーザー画面： 購入データ分析 - スマホ表示: グラフ幅");
        apiCallMasterSettingCreate("user_analysis_graph_size_sp_height", "200", "ユーザー画面： 購入データ分析 - スマホ表示: グラフ高さ");
        apiCallMasterSettingCreate("user_analysis_graph_size_tb_width", "680", "ユーザー画面： 購入データ分析 - タブレット表示: グラフ幅");
        apiCallMasterSettingCreate("user_analysis_graph_size_tb_height", "200", "ユーザー画面： 購入データ分析 - タブレット表示: グラフ高さ");
        apiCallMasterSettingCreate("user_communication_input_history_view", "10", "ユーザー画面： お付き合い帳入力 - 入力履歴表示数");
        apiCallMasterSettingCreate("user_communication_list_view", "20", "ユーザー画面： お付き合い帳一覧 - データ表示数/1ページ");
        apiCallMasterSettingCreate("user_communication_list_view_conditions", "0", "ユーザー画面： お付き合い帳一覧 - データ検索条件デフォルト設定");

        // 選択肢マスタ
        apiCallMasterChoicesCreate("type",0,"収入","収入");
        apiCallMasterChoicesCreate("type",1,"支出","支出");
        apiCallMasterChoicesCreate("payment",0,"現金","現金");
        apiCallMasterChoicesCreate("payment",1,"カード","カード");
        apiCallMasterChoicesCreate("payment",2,"引落し","引落し");
        apiCallMasterChoicesCreate("payment",3,"振込み","振込み");
        apiCallMasterChoicesCreate("settlement",0,"未精算","未");
        apiCallMasterChoicesCreate("settlement",1,"精算済","済");

        // お知らせ
        apiCallNoticeCreate("サービス開始","本サービスを開始しました。");

        // adminユーザー
        apiCallUserCreate("admin","adminadmin",2,1);
        $admin_user_id = null;
        $user_refer_result = apiCallUserRefer(null,"admin");
        foreach ($user_refer_result['data']['user'] as $user) {
            $userInfo = $user['user_info'];
            $admin_user_id = $userInfo['user_id'];
        }

        // admin用所属グループ
        $groups_id="admin_group";
        $group_name="管理者グループ";
        $group_password="maintenance";
        groupEntry($admin_user_id,$groups_id,$group_name,$group_password);

        // テストデータ
        for ($i = 1; $i <= 100; $i++) {
            $user_name = "user" . str_pad($i, 3, "0", STR_PAD_LEFT); // 001, 002, ..., 100
            $groups_id="group" . str_pad($i, 3, "0", STR_PAD_LEFT);;
            $group_name="グループ" . str_pad($i, 3, "0", STR_PAD_LEFT);;
            $group_password="1234567890";
            apiCallUserCreate($user_name, "1234567890", 0, 1);
            $user_refer_result = apiCallUserRefer(null,$user_name);
            foreach ($user_refer_result['data']['user'] as $user) {
                $userInfo = $user['user_info'];
                $user_id = $userInfo['user_id'];
            }
            groupEntry($user_id,$groups_id,$group_name,$group_password);
        }

        for ($i = 1; $i <= 100; $i++) {
            $user_id = "user" . str_pad($i, 3, "0", STR_PAD_LEFT); // 001, 002, ..., 100
            apiCallUserCreate($user_id, "1234567890", 0, 1);
        }

        for ($i = 2; $i <= 15; $i++) {
            $groups_id="group001";
            $user_id = "";
            $user_name = "user" . str_pad($i, 3, "0", STR_PAD_LEFT); // 001, 002, ..., 100
            $user_refer_result = apiCallUserRefer(null,$user_name);
            foreach ($user_refer_result['data']['user'] as $user) {
                $userInfo = $user['user_info'];
                $user_id = $userInfo['user_id'];
            }

            apiCallGroupInfoCreate($groups_id, $user_id, 0);
        }



    }
}