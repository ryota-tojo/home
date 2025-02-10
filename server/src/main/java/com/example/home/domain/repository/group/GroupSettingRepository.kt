package com.example.home.domain.repository.group

import com.example.home.domain.entity.group.GroupSetting
import com.example.home.domain.value_object.group.GroupSettingKey
import com.example.home.domain.value_object.group.GroupSettingValue
import com.example.home.domain.value_object.group.GroupsId

interface GroupSettingRepository {
    fun refer(groupsId: GroupsId? = null): List<GroupSetting>
    fun save(
        groupsId: GroupsId,
        settingKey: GroupSettingKey,
        settingValue: GroupSettingValue
    ): GroupSetting

    fun update(
        groupsId: GroupsId,
        settingKey: GroupSettingKey,
        settingValue: GroupSettingValue
    ): Int

    fun delete(groupsId: GroupsId): Int
}