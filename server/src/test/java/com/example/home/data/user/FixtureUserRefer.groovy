package com.example.home.data.user

import com.example.home.data.group.FixtureGroupInfo
import com.example.home.domain.entity.user.UserRefer

class FixtureUserRefer {

    static ユーザー参照_正常() {
        return new UserRefer(
                FixtureUserInfo.ユーザー情報_正常(),
                FixtureUserSetting.ユーザー設定_正常(),
                [FixtureGroupInfo.所属グループ情報_正常()]
        )
    }

    static ユーザー参照_ユーザー設定なし() {
        return new UserRefer(
                FixtureUserInfo.ユーザー情報_正常(),
                [],
                [FixtureGroupInfo.所属グループ情報_正常()]
        )
    }

    static ユーザー参照_所属グループ情報なし() {
        return new UserRefer(
                FixtureUserInfo.ユーザー情報_正常(),
                FixtureUserSetting.ユーザー設定_正常(),
                null
        )
    }

}