package com.example.home.infrastructure.persistence.repository.group

import com.example.home.domain.group.GroupList
import com.example.home.domain.group.GroupSetting
import com.example.home.domain.value_object.group.*

interface GroupSettingRepository {
    fun refer(groupsId: GroupsId): List<GroupSetting>
    fun referAll(): List<GroupSetting>
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