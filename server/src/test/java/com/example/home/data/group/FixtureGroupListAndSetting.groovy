package com.example.home.data.group


import com.example.home.domain.entity.group.GroupListAndSetting

class FixtureGroupListAndSetting {
    static 所属グループ一覧と設定_正常() {
        return new GroupListAndSetting(
                [FixtureGroupList.所属グループ一覧_正常()],
                FixtureGroupSetting.所属グループ設定_正常()
        )
    }
}