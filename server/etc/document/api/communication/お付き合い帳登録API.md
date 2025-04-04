# お付き合い帳登録API

## 目的

お付き合い帳を登録する

## 処理概要

お付き合い帳を登録する

## HTTPメソッド

| HTTPメソッド |
|:---------|
| POST     |

## APIパス

| パス                            |
|:------------------------------|
| api/master/communication/save |

## リクエストパラメータ

| パラメータ名      | 必須 | 属性     | 桁数    | 説明                           |
|:------------|:---|:-------|:------|:-----------------------------|
| groups_id   | ○  | String | 64    | 所属グループID                     |
| date        | ○  | String | 10    | お届け日（YYYY-MM-DD）             |
| from        | ○  | String | 64    | お届け人                         |
| to          | ○  | String | 64    | 受取人                          |
| item        | ○  | String | 64    | 贈り物                          |
| amount      | ○  | Int    | -     | 金額                           |
| remarks     | ○  | String | 65535 | 備考                           |
| rtn_date    | ○  | String | 10    | 返却日（YYYY-MM-DD）              |
| rtn_item    | ○  | String | 64    | 返却物                          |
| rtn_amount  | ○  | Int    | -     | 金額                           |
| rtn_remarks | ○  | String | 65535 | 備考                           |
| return      | ○  | Int    | -     | 返却フラグ（0:未返却, 1:返却済, -1:返却不要） |

## レスポンスパラメータ

### 成功時

| パラメータ名  | 属性     | 説明                  |
|:--------|:-------|:--------------------|
| status  | String | レスポンスステータス(success) |
| message | String | レスポンスメッセージ          |
| data    | Object | 後述の「dataオブジェクト」を参照  |

#### dataオブジェクト

| パラメータ名        | 属性     | 説明                          |
|:--------------|:-------|:----------------------------|
| communication | Object | 後述の「communicationオブジェクト」を参照 |

#### communicationオブジェクト

| パラメータ名      | 属性     | 説明                           |
|:------------|:-------|:-----------------------------|
| gift_id     | Int    | お付き合い帳ID                     |
| groups_id   | String | 所属グループID                     |
| date        | String | お届け日                         |
| from        | String | お届け人                         |
| to          | String | 受取人                          |
| item        | String | 贈り物                          |
| amount      | Int    | 金額                           |
| remarks     | String | 備考                           |
| rtn_date    | String | 返却日                          |
| rtn_item    | String | 返却物                          |
| rtn_amount  | Int    | 金額                           |
| rtn_remarks | String | 備考                           |
| return      | Int    | 返却フラグ（0:未返却, 1:返却済, -1:返却不要） |

### 失敗時

[共通レスポンスパラメータ_エラー](../共通レスポンスパラメータ_エラー.md)
に従う

## その他

なし

## シーケンス図

※インターフェースやデータソース層の処理は省略して記載
