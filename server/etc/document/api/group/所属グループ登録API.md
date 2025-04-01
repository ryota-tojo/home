# 所属グループ登録API

## 目的

所属グループに関連する情報を登録する

## 処理概要

所属グループの一覧と設定の情報を登録する

## HTTPメソッド

| HTTPメソッド |
|:---------|
| POST     |

## APIパス

| パス               |
|:-----------------|
| api/group/create |

## リクエストパラメータ

| パラメータ名         | 必須 | 属性     | 桁数 | 説明          |
|:---------------|:---|:-------|:---|:------------|
| groups_id      | ○  | String | 64 | 所属グループID    |
| group_name     | ○  | String | 64 | 所属グループ名     |
| group_password | ○  | String | 64 | 所属グループパスワード |

## レスポンスパラメータ

### 成功時

| パラメータ名  | 属性     | 説明                  |
|:--------|:-------|:--------------------|
| status  | String | レスポンスステータス(success) |
| message | String | レスポンスメッセージ          |
| data    | Object | 後述の「dataオブジェクト」を参照  |

#### dataオブジェクト

| パラメータ名         | 属性     | 説明            |
|:---------------|:-------|:--------------|
| id             | Int    | 所属グループ一覧の識別ID |
| groups_id      | String | 所属グループID      |
| group_name     | String | 所属グループ名       |
| group_password | String | 所属グループパスワード   |

### 失敗時

[共通レスポンスパラメータ_エラー](../共通レスポンスパラメータ_エラー.md)
に従う

## その他

なし

## シーケンス図

※インターフェースやデータソース層の処理は省略して記載

```mermaid
sequenceDiagram
    participant API
    participant Service
    participant データベース
    activate API
    Note over API: パラメータ<br>+ groups_id<br>+ group_name<br>+ group_password
    Note over API: 処理開始
    API ->> Service: 実行
    deactivate API
    activate Service
    Note right of Service: + groups_id<br>+ group_name
    Service ->> Service: バリデーションエラーチェック
    opt ValidationCheck == false
        Note over Service: 処理結果<br>GroupSaveResult(result, null)
        Service ->> API: 処理結果を返却
    end
    Service ->> データベース: 参照
    deactivate Service
    activate データベース
    Note over データベース: 所属グループ一覧<br>GroupList
    データベース -->> Service: 取得(n)
    deactivate データベース
    activate Service
    opt GroupList.isNotEmpty
        Note over Service: 処理結果<br>GroupSaveResult(result, null)
        Service ->> API: 処理結果を返却
    end

    Service ->> データベース: 登録
    deactivate Service
    activate データベース
    Note over データベース: 所属グループ一覧(1)<br>GroupList
    データベース -->> Service: 取得
    deactivate データベース
    activate Service
    Service ->> データベース: 登録
    deactivate Service
    activate データベース
    Note over データベース: 所属グループ設定(n)<br>List<GroupSetting>
    データベース -->> Service: 取得(n)
    deactivate データベース
    activate Service
    Service ->> データベース: 登録
    deactivate Service
    activate データベース
    Note over データベース: カテゴリー(n)
    データベース -->> Service: 未取得
    deactivate データベース
    activate Service
    Service ->> データベース: 登録
    deactivate Service
    activate データベース
    Note over データベース: メンバー(n)
    データベース -->> Service: 未取得
    deactivate データベース
    activate Service
    Service ->> データベース: 登録
    deactivate Service
    activate データベース
    Note over データベース: コメント(n)
    データベース -->> Service: 未取得
    deactivate データベース
    activate Service
    Note over Service: 処理結果<br>GroupSaveResult(result, GroupList, List<GroupSetting>)
    Service -->> API: 処理結果を返却
    deactivate Service
    activate API
    Note over API: 処理終了
    deactivate API

```
