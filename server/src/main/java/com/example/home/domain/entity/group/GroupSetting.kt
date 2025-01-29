package com.example.home.domain.entity.group

import com.example.home.domain.value_object.group.GroupSettingKey
import com.example.home.domain.value_object.group.GroupSettingValue
import com.example.home.domain.value_object.group.GroupsId

data class GroupSetting(
    val id : Int,
    val groupsId: GroupsId,
    val settingKey: GroupSettingKey,
    val settingValue: GroupSettingValue
)