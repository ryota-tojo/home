package com.example.home.data.group

import com.example.home.domain.entity.group.GroupInfo
import com.example.home.domain.value_object.group.GroupsId
import com.example.home.domain.value_object.user.UserId
import com.example.home.domain.value_object.user.UserLeaderFlg

import java.time.LocalDateTime

class FixtureGroupInfo {

    static リーダーフラグ_メンバー() {
        new UserLeaderFlg(0)
    }

    static リーダーフラグ_リーダー() {
        new UserLeaderFlg(1)
    }

    static 作成日_正常() {
        LocalDateTime.of(2025, 2, 2, 12, 34, 56, 789000000)
    }

    static 更新日_正常() {
        LocalDateTime.of(2025, 2, 2, 12, 34, 56, 789000000)
    }

    static 所属グループ情報_正常() {
        return new GroupInfo(
                new GroupsId("groups_id"),
                new UserId(1),
                リーダーフラグ_メンバー(),
                作成日_正常(),
                更新日_正常()
        )
    }

    static 所属グループ情報_リーダー() {
        return new GroupInfo(
                new GroupsId("groups_id"),
                new UserId(1),
                リーダーフラグ_リーダー(),
                作成日_正常(),
                更新日_正常()
        )
    }
}