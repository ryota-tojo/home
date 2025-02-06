package com.example.home.data

import com.example.home.domain.entity.group.GroupSetting
import com.example.home.domain.value_object.group.GroupSettingKey
import com.example.home.domain.value_object.group.GroupSettingValue

class FixtureGroupSetting {

    static 所属グループ設定キー_正常() {
        new GroupSettingKey("user_setting_key")
    }

    static 所属グループ設定キー_バリデーションエラー() {
        new GroupSettingKey("user_setting,_key")
    }

    static 所属グループ設定値_正常() {
        new GroupSettingValue("user_setting_value")
    }

    static 所属グループ設定値_バリデーションエラー() {
        new GroupSettingValue("user_setting_,value")
    }

    static 所属グループ設定_正常() {
        return [
                new GroupSetting(
                        1,
                        FixtureGroupList.所属グループID_正常(),
                        所属グループ設定キー_正常(),
                        所属グループ設定値_正常()
                )
        ]
    }

}