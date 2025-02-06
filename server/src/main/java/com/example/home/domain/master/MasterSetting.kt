package com.example.home.domain.master

import com.example.home.domain.value_object.master.MasterSettingKey
import com.example.home.domain.value_object.master.MasterSettingValue

data class MasterSetting(
    val masterSettingKey: MasterSettingKey,
    val masterSettingValue: MasterSettingValue
)