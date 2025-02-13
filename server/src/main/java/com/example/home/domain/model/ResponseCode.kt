package com.example.home.domain.model

enum class ResponseCode(val status: String, val code: String, val message: String) {
    成功("success", "SUCCESS", "処理成功"),
    成功_条件付き("conditional_success_%s", "CONDITIONAL_SUCCESS_%s", "処理成功_%s"),
    バリデーションエラー("error", "VALIDATION_ERROR", "使用できない文字列が含まれています"),
    重複エラー("error", "DUPLICATION_ERROR", "既に登録されているデータです"),
    データ不在エラー("error", "DATA_NOT_FOUND_ERROR", "対象のデータが存在しません"),
    データ不正エラー("error", "DATA_ERROR", "不正データが存在します"),
    キー未設定エラー("error", "KEY_NOT_FOUND_ERROR", "キーが存在しません"),

    グループ認証エラー("error", "GROUP_CERTIFICATION_ERROR", "グループの認証に失敗しました"),

    ユーザーエラー_グループリーダー削除("error", "USER_ERROR_LEADER_DELETED", "所属グループのリーダーは削除できません"),
    ログインエラー_データ照合("error", "LOGIN_ERROR_COLLATION", "ユーザーの照合に失敗しました"),
    ログインエラー_削除済ユーザー("error", "LOGIN_ERROR_DELETED_USER", "ログイン不可能なユーザーです"),
    ログインエラー_未承認ユーザー("error", "LOGIN_ERROR_UNAPPROVED_USER", "未承認ユーザーです"),
    ログインエラー_所属グループ不正("error", "LOGIN_ERROR_GROUP_FRAUD", "所属グループが間違っています"),

    重複するテンプレートID(
        "error",
        "TEMPLATE_ID_IS_DUPLICATION_ERROR",
        "既に登録されたテンプレートIDが指定されています"
    ),
    存在しないメンバー("error", "MEMBER_NOT_FOUND_ERROR", "存在しないメンバーが指定されています"),
    存在しないカテゴリー("error", "CATEGORY_NOT_FOUND_ERROR", "存在しないカテゴリーが指定されています"),
    存在しない購入種別("error", "SHOPPING_TYPE_NOT_FOUND_ERROR", "存在しない購入種別が指定されています"),
    存在しない支払い方法("error", "SHOPPING_PAYMENT_NOT_FOUND_ERROR", "存在しない支払い方法が指定されています"),
    存在しない精算状況("error", "SHOPPING_SETTLEMENT_NOT_FOUND_ERROR", "存在しない精算状況が指定されています");

}