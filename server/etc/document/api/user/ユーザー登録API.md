# ユーザー登録API

## 目的

ユーザー情報の登録を行う。

## 処理概要

ユーザー情報の登録を行う。

## HTTPメソッド

| HTTPメソッド |
|:---------|
| POST     |

## APIパス

| パス              |
|:----------------|
| api/user/create |

## リクエストパラメータ

| パラメータ名     | 必須 | 属性     | 桁数 | 説明                                |
|:-----------|:---|:-------|:---|:----------------------------------|
| user_name  | ○  | String | 64 | ユーザー名                             |
| password   | ○  | String | 64 | パスワード                             |
| permission | ○  | Int    | -  | 権限フラグ（0:一般権限, 1:管理者権限, 2:最高管理者権限） |
| approval   | ○  | Int    | -  | 承認フラグ（0:未承認, 1:承認済）               |
| delete     | ○  | Int    | -  | 削除フラグ（0:使用中, 1:削除済）               |

## レスポンスパラメータ

### 成功時

| パラメータ名  | 属性     | 説明                  |
|:--------|:-------|:--------------------|
| status  | String | レスポンスステータス(success) |
| message | String | レスポンスメッセージ          |
| data    | Object | 後述の「dataオブジェクト」を参照  |

#### dataオブジェクト

| パラメータ名 | 属性     | 説明                 |
|:-------|:-------|:-------------------|
| user   | Object | 後述の「userオブジェクト」を参照 |

#### userオブジェクト

| パラメータ名       | 属性          | 説明                         |
|:-------------|:------------|:---------------------------|
| user_info    | Object      | 後述の「user_infoオブジェクト」を参照    |
| user_setting | List:Object | 後述の「user_settingオブジェクト」を参照 |
| group_info   | List:Object | 後述の「group_infoオブジェクト」を参照   |

#### user_infoオブジェクト

| パラメータ名        | 属性     | 説明                                |    
|:--------------|:-------|:----------------------------------|
| user_id       | Int    | ユーザーID                            |
| user_name     | String | ユーザー名                             |
| password      | String | パスワード                             |
| permission    | Int    | 権限フラグ（0:一般権限, 1:管理者権限, 2:最高管理者権限） |
| approval      | Int    | 承認フラグ（0:未承認, 1:承認済）               |
| delete        | Int    | 削除フラグ（0:使用中, 1:削除済）               |
| create_date   | String | 作成日（YYYY-MM-DD）                   |
| update_date   | String | 更新日（YYYY-MM-DD）                   |
| approval_date | String | 承認日（YYYY-MM-DD）                   |
| delete_date   | String | 削除日（YYYY-MM-DD）                   |

#### user_settingオブジェクト

| パラメータ名        | 属性     | 説明     |
|:--------------|:-------|:-------|
| id            | Int    | 設定ID   |
| user_id       | Int    | ユーザーID |
| setting_key   | String | 設定キー   |
| setting_value | String | 設定値    |

#### group_infoオブジェクト

| パラメータ名      | 属性     | 説明                      |
|:------------|:-------|:------------------------|
| groups_id   | String | 所属グループID                |
| user_id     | Int    | ユーザーID                  |
| leader      | Int    | リーダーフラグ（0:メンバー, 1:リーダー） |
| approval    | Int    | 承認フラグ（0:未承認, 1:承認済）     |
| create_date | String | 作成日（YYYY-MM-DD）         |
| update_date | String | 更新日（YYYY-MM-DD）         |

### 失敗時

[共通レスポンスパラメータ_エラー](../共通レスポンスパラメータ_エラー.md)
に従う

## その他

なし

## シーケンス図

※インターフェースやデータソース層の処理は省略して記載
