package com.example.home.domain.entity.group.result

import com.example.home.domain.entity.group.GroupSetting

data class GroupSettingCreateResult(
    val result: String,
    val groupSetting: GroupSetting? = null
)
