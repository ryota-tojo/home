# 購入データ参照API

## 目的

購入データを参照する

## 処理概要

購入データを参照する

## APIパス

| パス                 |
|:-------------------|
| api/shopping/refer |

## リクエストパラメータ

| パラメータ名        | 必須 | 属性     | 桁数   | 説明                  |
|:--------------|:---|:-------|:-----|:--------------------|
| shopping_id   |    | Int    | -    | 購入データID             |
| groups_id     |    | String | 64   | 所属グループID            |
| user_id       |    | Int    | -    | ユーザーID              |
| shopping_date |    | String | 10   | 購入日（YYYY-MM-DD）     |
| member_id     |    | Int    | -    | メンバーID              |
| category_id   |    | Int    | -    | カテゴリーID             |
| type          |    | Int    | -    | 種別（0:収入, 1:出費）      |
| payment       |    | Int    | -    | 支払い方法               |
| settlement    |    | Int    | -    | 精算フラグ（0:未精算, 1:精算済） |
| amount        |    | Int    | -    | 金額                  |
| remarks       |    | String | 1024 | 備考                  |
| fixed         |    | Int    | -    | 確定フラグ（0:未確定, 1:確定）  |

## レスポンスパラメータ

### 成功時

| パラメータ名  | 属性     | 説明                  |
|:--------|:-------|:--------------------|
| status  | String | レスポンスステータス(success) |
| message | String | レスポンスメッセージ          |
| data    | Object | 後述の「dataオブジェクト」を参照  |

#### dataオブジェクト

| パラメータ名        | 属性          | 説明                          |
|:--------------|:------------|:----------------------------|
| shopping_list | List:Object | 後述の「shopping_listオブジェクト」を参照 |

#### shopping_listオブジェクト

| パラメータ名        | 属性     | 説明                  |
|:--------------|:-------|:--------------------|
| shopping_id   | Int    | 購入データID             |
| groups_id     | String | 所属グループID            |
| user_id       | Int    | ユーザーID              |
| shopping_date | String | 購入日（YYYY-MM-DD）     |
| member_id     | Int    | メンバーID              |
| category_id   | Int    | カテゴリーID             |
| type          | Int    | 種別                  |
| payment       | Int    | 支払い方法               |
| settlement    | Int    | 精算フラグ（0:未精算, 1:精算済） |
| amount        | Int    | 金額                  |
| remarks       | String | 備考                  |
| fixed         | Int    | 確定フラグ（0:未確定, 1:確定）  |

### 失敗時

[共通レスポンスパラメータ_エラー](../共通レスポンスパラメータ_エラー.md)
に従う

## その他

なし

## シーケンス図

※インターフェースやデータソース層の処理は省略して記載
