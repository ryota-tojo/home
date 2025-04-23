package com.example.home.domain.repository.master

import com.example.home.domain.entity.master.MasterSetting
import com.example.home.domain.value_object.master.MasterSettingKey
import com.example.home.domain.value_object.master.MasterSettingRemarks
import com.example.home.domain.value_object.master.MasterSettingValue

interface MasterSettingRepository {
    fun refer(masterSettingKey: MasterSettingKey? = null): List<MasterSetting>
    fun save(
        masterSettingKey: MasterSettingKey,
        masterSettingValue: MasterSettingValue,
        masterSettingRemarks: MasterSettingRemarks
    ): MasterSetting

    fun update(
        masterSettingKey: MasterSettingKey,
        masterSettingValue: MasterSettingValue? = null,
        masterSettingRemarks: MasterSettingRemarks? = null
    ): Int

    fun delete(masterSettingKey: MasterSettingKey): Int
}
