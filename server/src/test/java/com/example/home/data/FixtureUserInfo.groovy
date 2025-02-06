package com.example.home.data


import com.example.home.domain.entity.user.UserInfo
import com.example.home.domain.value_object.user.*

import java.time.LocalDateTime

class FixtureUserInfo {

    static ユーザーID_正常() {
        new UserId(1)
    }

    static ユーザー名_正常() {
        new UserName("user_name")
    }

    static ユーザー名_バリデーションエラー() {
        new UserName("user_,name")
    }

    static パスワード_正常() {
        new UserPassword("password")
    }

    static パスワード_バリデーションエラー() {
        new UserPassword("pass,word")
    }

    static 権限フラグ_一般() {
        new UserPermission(0)
    }

    static 権限フラグ_管理者() {
        new UserPermission(1)
    }

    static 権限フラグ_特権管理者() {
        new UserPermission(2)
    }

    static 承認フラグ_未承認() {
        new UserApprovalFlg(0)
    }

    static 承認フラグ_承認済() {
        new UserApprovalFlg(1)
    }

    static 削除フラグ_未削除() {
        new UserDeleteFlg(0)
    }

    static 削除フラグ_削除済() {
        new UserDeleteFlg(1)
    }

    static 作成日_正常() {
        LocalDateTime.of(2025, 2, 2, 12, 34, 56, 789000000)
    }

    static 更新日_正常() {
        LocalDateTime.of(2025, 2, 2, 12, 34, 56, 789000000)
    }

    static 承認日_正常() {
        LocalDateTime.of(2025, 2, 2, 12, 34, 56, 789000000)
    }

    static 削除日_正常() {
        LocalDateTime.of(2025, 2, 2, 12, 34, 56, 789000000)
    }

    static ユーザー情報_正常() {
        return new UserInfo(
                new UserId(1),
                ユーザー名_正常(),
                パスワード_正常(),
                権限フラグ_一般(),
                承認フラグ_承認済(),
                削除フラグ_未削除(),
                作成日_正常(),
                更新日_正常(),
                承認日_正常(),
                null
        )
    }

    static ユーザー情報_未承認() {
        return new UserInfo(
                new UserId(1),
                ユーザー名_正常(),
                パスワード_正常(),
                権限フラグ_一般(),
                承認フラグ_未承認(),
                削除フラグ_未削除(),
                作成日_正常(),
                更新日_正常(),
                null,
                null
        )
    }

    static ユーザー情報_削除済() {
        return new UserInfo(
                new UserId(1),
                ユーザー名_正常(),
                パスワード_正常(),
                権限フラグ_一般(),
                承認フラグ_承認済(),
                削除フラグ_削除済(),
                作成日_正常(),
                更新日_正常(),
                承認日_正常(),
                削除日_正常()
        )
    }
}