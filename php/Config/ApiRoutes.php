<?php

// マスタ設定API
const API_MASTER_SETTING_REFER = ["HTTP_METHOD" => "GET", "API_PATH" => "api/master/setting/refer"];
const API_MASTER_SETTING_CREATE = ["HTTP_METHOD" => "POST", "API_PATH" => "api/master/setting/create"];
const API_MASTER_SETTING_UPDATE = ["HTTP_METHOD" => "POST", "API_PATH" => "api/master/setting/update"];
const API_MASTER_SETTING_DELETE = ["HTTP_METHOD" => "POST", "API_PATH" => "api/master/setting/delete"];

// お知らせAPI
const API_NOTICE_REFER = ["HTTP_METHOD" => "GET", "API_PATH" => "api/notice/refer"];
const API_NOTICE_COUNT = ["HTTP_METHOD" => "GET", "API_PATH" => "api/notice/count"];
const API_NOTICE_CREATE = ["HTTP_METHOD" => "POST", "API_PATH" => "api/notice/create"];
const API_NOTICE_UPDATE = ["HTTP_METHOD" => "POST", "API_PATH" => "api/notice/update"];
const API_NOTICE_DELETE = ["HTTP_METHOD" => "POST", "API_PATH" => "api/notice/delete"];

// ログインAPI
const API_LOGIN = ["HTTP_METHOD" => "POST", "API_PATH" => "api/login"];

// ユーザーAPI
const API_USER_REFER = ["HTTP_METHOD" => "GET", "API_PATH" => "api/user/refer"];
const API_USER_COUNT = ["HTTP_METHOD" => "GET", "API_PATH" => "api/user/count"];
const API_USER_CREATE = ["HTTP_METHOD" => "POST", "API_PATH" => "api/user/create"];
const API_USER_UPDATE_INFO = ["HTTP_METHOD" => "POST", "API_PATH" => "api/user/update/info"];
const API_USER_UPDATE_SETTING = ["HTTP_METHOD" => "POST", "API_PATH" => "api/user/update/setting"];
const API_USER_DELETE = ["HTTP_METHOD" => "POST", "API_PATH" => "api/user/delete"];

// 所属グループ一覧API
const API_GROUP_REFER = ["HTTP_METHOD" => "GET", "API_PATH" => "api/group/refer"];
const API_GROUP_COUNT = ["HTTP_METHOD" => "GET", "API_PATH" => "api/group/count"];
const API_GROUP_CREATE = ["HTTP_METHOD" => "POST", "API_PATH" => "api/group/create"];
const API_GROUP_UPDATE_LIST = ["HTTP_METHOD" => "POST", "API_PATH" => "api/group/update/list"];
const API_GROUP_UPDATE_SETTING = ["HTTP_METHOD" => "POST", "API_PATH" => "api/group/update/setting"];
const API_GROUP_DELETE = ["HTTP_METHOD" => "POST", "API_PATH" => "api/group/delete"];

// 所属グループ情報API
const API_GROUP_INFO_REFER = ["HTTP_METHOD" => "GET", "API_PATH" => "api/group/info/refer"];
const API_GROUP_INFO_COUNT = ["HTTP_METHOD" => "GET", "API_PATH" => "api/group/info/count"];
const API_GROUP_INFO_CREATE = ["HTTP_METHOD" => "POST", "API_PATH" => "api/group/info/create"];
const API_GROUP_INFO_UPDATE = ["HTTP_METHOD" => "POST", "API_PATH" => "api/group/info/update"];
const API_GROUP_INFO_DELETE = ["HTTP_METHOD" => "POST", "API_PATH" => "api/group/info/delete"];

// カテゴリーAPI
const API_CATEGORY_REFER = ["HTTP_METHOD" => "GET", "API_PATH" => "api/category/refer"];
const API_CATEGORY_COUNT = ["HTTP_METHOD" => "GET", "API_PATH" => "api/category/count"];
const API_CATEGORY_CREATE = ["HTTP_METHOD" => "POST", "API_PATH" => "api/category/create"];
const API_CATEGORY_UPDATE = ["HTTP_METHOD" => "POST", "API_PATH" => "api/category/update"];
const API_CATEGORY_DELETE = ["HTTP_METHOD" => "POST", "API_PATH" => "api/category/delete"];
const API_CATEGORY_DISABLE = ["HTTP_METHOD" => "POST", "API_PATH" => "api/category/disable"];

// メンバーAPI
const API_MEMBER_REFER = ["HTTP_METHOD" => "GET", "API_PATH" => "api/member/refer"];
const API_MEMBER_COUNT = ["HTTP_METHOD" => "GET", "API_PATH" => "api/member/count"];
const API_MEMBER_CREATE = ["HTTP_METHOD" => "POST", "API_PATH" => "api/member/create"];
const API_MEMBER_UPDATE = ["HTTP_METHOD" => "POST", "API_PATH" => "api/member/update"];
const API_MEMBER_DELETE = ["HTTP_METHOD" => "POST", "API_PATH" => "api/member/delete"];
const API_MEMBER_DISABLE = ["HTTP_METHOD" => "POST", "API_PATH" => "api/member/disable"];

// テンプレートAPI
// - 登録テンプレート
const API_TEMPLATE_ENTRY_REFER = ["HTTP_METHOD" => "GET", "API_PATH" => "api/template/entry/refer"];
const API_TEMPLATE_ENTRY_CREATE = ["HTTP_METHOD" => "POST", "API_PATH" => "api/template/entry/create"];
const API_TEMPLATE_ENTRY_UPDATE = ["HTTP_METHOD" => "POST", "API_PATH" => "api/template/entry/update"];
const API_TEMPLATE_ENTRY_DELETE = ["HTTP_METHOD" => "POST", "API_PATH" => "api/template/entry/delete"];
const API_TEMPLATE_ENTRY_DISABLE = ["HTTP_METHOD" => "POST", "API_PATH" => "api/template/entry/disable"];
const API_TEMPLATE_ENTRY_USAGE = ["HTTP_METHOD" => "POST", "API_PATH" => "api/template/entry/usage"];
const API_TEMPLATE_ENTRY_UN_USAGE = ["HTTP_METHOD" => "POST", "API_PATH" => "api/template/entry/un_usage"];

// - 検索テンプレート
const API_TEMPLATE_SEARCH_REFER = ["HTTP_METHOD" => "GET", "API_PATH" => "api/template/search/refer"];
const API_TEMPLATE_SEARCH_CREATE = ["HTTP_METHOD" => "POST", "API_PATH" => "api/template/search/create"];
const API_TEMPLATE_SEARCH_UPDATE = ["HTTP_METHOD" => "POST", "API_PATH" => "api/template/search/update"];
const API_TEMPLATE_SEARCH_DELETE = ["HTTP_METHOD" => "POST", "API_PATH" => "api/template/search/delete"];
const API_TEMPLATE_SEARCH_DISABLE = ["HTTP_METHOD" => "POST", "API_PATH" => "api/template/search/disable"];
const API_TEMPLATE_SEARCH_USAGE = ["HTTP_METHOD" => "POST", "API_PATH" => "api/template/search/usage"];
const API_TEMPLATE_SEARCH_UN_USAGE = ["HTTP_METHOD" => "POST", "API_PATH" => "api/template/search/un_usage"];

// - 入力テンプレート
const API_TEMPLATE_INPUT_REFER = ["HTTP_METHOD" => "GET", "API_PATH" => "api/template/input/refer"];
const API_TEMPLATE_INPUT_CREATE = ["HTTP_METHOD" => "POST", "API_PATH" => "api/template/input/create"];
const API_TEMPLATE_INPUT_UPDATE = ["HTTP_METHOD" => "POST", "API_PATH" => "api/template/input/update"];
const API_TEMPLATE_INPUT_DELETE = ["HTTP_METHOD" => "POST", "API_PATH" => "api/template/input/delete"];
const API_TEMPLATE_INPUT_DISABLE = ["HTTP_METHOD" => "POST", "API_PATH" => "api/template/input/disable"];
const API_TEMPLATE_INPUT_USAGE = ["HTTP_METHOD" => "POST", "API_PATH" => "api/template/input/usage"];
const API_TEMPLATE_INPUT_UN_USAGE = ["HTTP_METHOD" => "POST", "API_PATH" => "api/template/input/un_usage"];

// 予算API
const API_BUDGETS_REFER = ["HTTP_METHOD" => "GET", "API_PATH" => "api/budgets/refer"];
const API_BUDGETS_CREATE = ["HTTP_METHOD" => "POST", "API_PATH" => "api/budgets/create"];
const API_BUDGETS_UPDATE = ["HTTP_METHOD" => "POST", "API_PATH" => "api/budgets/update"];
const API_BUDGETS_DELETE = ["HTTP_METHOD" => "POST", "API_PATH" => "api/budgets/delete"];

// 購入データAPI
const API_SHOPPING_REFER = ["HTTP_METHOD" => "GET", "API_PATH" => "api/budgets/refer"];
const API_SHOPPING_COUNT = ["HTTP_METHOD" => "GET", "API_PATH" => "api/budgets/count"];
const API_SHOPPING_ALL_CATEGORY = ["HTTP_METHOD" => "GET", "API_PATH" => "api/budgets/all_category"];
const API_SHOPPING_ALL_MEMBER = ["HTTP_METHOD" => "GET", "API_PATH" => "api/budgets/all_member"];
const API_SHOPPING_DUPLICATION_CHECK = ["HTTP_METHOD" => "GET", "API_PATH" => "api/budgets/duplication_check"];
const API_SHOPPING_CREATE = ["HTTP_METHOD" => "POST", "API_PATH" => "api/budgets/create"];
const API_SHOPPING_UPDATE = ["HTTP_METHOD" => "POST", "API_PATH" => "api/budgets/update"];
const API_SHOPPING_DELETE = ["HTTP_METHOD" => "POST", "API_PATH" => "api/budgets/delete"];

// コメントAPI
const API_COMMENT_REFER = ["HTTP_METHOD" => "GET", "API_PATH" => "api/comment/refer"];
const API_COMMENT_CREATE = ["HTTP_METHOD" => "POST", "API_PATH" => "api/comment/create"];
const API_COMMENT_UPDATE = ["HTTP_METHOD" => "POST", "API_PATH" => "api/comment/update"];
const API_COMMENT_DELETE = ["HTTP_METHOD" => "POST", "API_PATH" => "api/comment/delete"];

// お付き合い帳API
const API_COMMUNICATION_REFER = ["HTTP_METHOD" => "GET", "API_PATH" => "api/communication/refer"];
const API_COMMUNICATION_CREATE = ["HTTP_METHOD" => "POST", "API_PATH" => "api/communication/create"];
const API_COMMUNICATION_UPDATE = ["HTTP_METHOD" => "POST", "API_PATH" => "api/communication/update"];
const API_COMMUNICATION_DELETE = ["HTTP_METHOD" => "POST", "API_PATH" => "api/communication/delete"];

// 確定データAPI
const API_FIXED_REFER = ["HTTP_METHOD" => "GET", "API_PATH" => "api/fixed/refer"];
const API_FIXED_FIXED = ["HTTP_METHOD" => "POST", "API_PATH" => "api/fixed/fixed"];
const API_FIXED_UN_FIXED = ["HTTP_METHOD" => "POST", "API_PATH" => "api/fixed/un_fixed"];

// レポートAPI
const API_REPORT = ["HTTP_METHOD" => "GET", "API_PATH" => "api/report"];

// 分析API
const API_ANALYSIS_PA = ["HTTP_METHOD" => "GET", "API_PATH" => "api/analysis/p_a"];
const API_ANALYSIS_YOY = ["HTTP_METHOD" => "GET", "API_PATH" => "api/analysis/yoy"];


