package com.example.home.data.user

import com.example.home.domain.entity.user.UserSetting
import com.example.home.domain.value_object.user.UserSettingKey
import com.example.home.domain.value_object.user.UserSettingValue

class FixtureUserSetting {

    static 設定キー_正常() {
        new UserSettingKey("user_setting_key")
    }

    static 設定キー_バリデーションエラー() {
        new UserSettingKey("user_setting,_key")
    }

    static 設定値_正常() {
        new UserSettingValue("user_setting_value")
    }

    static 設定値_バリデーションエラー() {
        new UserSettingValue("user_setting_,value")
    }

    static ユーザー設定_正常() {
        return [
                new UserSetting(
                        1,
                        FixtureUserInfo.ユーザーID_正常(),
                        設定キー_正常(),
                        設定値_正常()
                )
        ]
    }

}