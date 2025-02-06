package com.example.home.data.master


import com.example.home.domain.entity.master.MasterSetting
import com.example.home.domain.value_object.master.MasterSettingKey
import com.example.home.domain.value_object.master.MasterSettingValue

class FixtureMasterSetting {

    static マスター設定キー_正常() {
        new MasterSettingKey("master_setting_key")
    }

    static マスター設定キー_バリデーションエラー() {
        new MasterSettingKey("master_setting,_key")
    }

    static マスター設定値_正常() {
        new MasterSettingValue("master_setting_value")
    }

    static マスター設定値_バリデーションエラー() {
        new MasterSettingValue("master_setting_,value")
    }

    static マスター設定_正常() {
        return new MasterSetting(
                マスター設定キー_正常(),
                マスター設定値_正常()
        )

    }

}