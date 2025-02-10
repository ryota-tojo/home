package com.example.home.domain.repository.master

import com.example.home.domain.entity.master.MasterSetting
import com.example.home.domain.value_object.master.MasterSettingKey
import com.example.home.domain.value_object.master.MasterSettingValue

interface MasterSettingRepository {
    fun refer(masterSettingKey: MasterSettingKey? = null): List<MasterSetting>
    fun save(masterSettingKey: MasterSettingKey, masterSettingValue: MasterSettingValue): MasterSetting
    fun update(masterSettingKey: MasterSettingKey, masterSettingValue: MasterSettingValue): Int
    fun delete(masterSettingKey: MasterSettingKey): Int
}