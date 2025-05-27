<?php

return [
    // [設定キー, 設定値, 備考]

    // 初期設定
    ['initialization', '1', '初期設定フラグ'],

    // メンテナンス
    ['maintenance', '0', 'メンテナンス判定'],

    // 通知
    ['notification_send_flg', '0', '通知フラグ'],
    ['notification_url', 'https://default', '通知URL'],
    ['notification_token', '', '通知トークン'],

    // システム設定
    ['login_failure_limit', '5', 'ログイン失敗許容回数'],
    ['font_family', 'system-ui', 'メインフォント'],
    ['random_font_family', '', 'ランダムフォント'],
    ['random_font_probability', '', 'ランダムフォント確率最大値'],
    ['loading_delay_seconds', '1', 'ロード画面を表示するまでの秒数'],
    ['lording_layout', '0', 'ロード画面レイアウトパターン'],

    // 管理者画面
    ['admin_userdata_view', '10', '管理者画面: ユーザーデータ表示数/1ページ'],
    ['admin_groupdata_view', '10', '管理者画面: 所属グループデータ表示数/1ページ'],
    ['admin_groupinfodata_view', '10', '管理者画面: 所属グループ情報データ表示数/1ページ'],
    ['admin_notice_view', '5', '管理者画面: お知らせデフォルト表示数'],
    ['admin_notice_default_title', 'お知らせ', '管理者画面: お知らせタイトルデフォルト値'],
    ['admin_notice_default_content', 'XXXXX', '管理者画面: お知らせ内容デフォルト値'],

    // ユーザー画面：購入データ
    ['user_input_history_view', '10', 'ユーザー画面：購入データ入力 - 入力履歴表示数'],
    ['user_management_view', '20', 'ユーザー画面：購入データ管理 - データ表示数/1ページ'],
    ['user_analysis_graph_size_pc_width', '750', 'ユーザー画面：購入データ分析 - PC表示: グラフ幅'],
    ['user_analysis_graph_size_pc_height', '200', 'ユーザー画面：購入データ分析 - PC表示: グラフ高さ'],
    ['user_analysis_graph_size_sp_width', '320', 'ユーザー画面：購入データ分析 - スマホ表示: グラフ幅'],
    ['user_analysis_graph_size_sp_height', '200', 'ユーザー画面：購入データ分析 - スマホ表示: グラフ高さ'],
    ['user_analysis_graph_size_tb_width', '680', 'ユーザー画面：購入データ分析 - タブレット表示: グラフ幅'],
    ['user_analysis_graph_size_tb_height', '200', 'ユーザー画面：購入データ分析 - タブレット表示: グラフ高さ'],

    // ユーザー画面：お付き合い帳
    ['user_communication_input_history_view', '10', 'ユーザー画面：お付き合い帳入力 - 入力履歴表示数'],
    ['user_communication_list_view', '20', 'ユーザー画面：お付き合い帳一覧 - データ表示数/1ページ'],
    ['user_communication_list_view_conditions', '0', 'ユーザー画面：お付き合い帳一覧 - データ検索条件デフォルト設定'],
];
