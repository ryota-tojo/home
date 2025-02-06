package com.example.home.domain.entity.group.result

import com.example.home.domain.entity.group.GroupSetting

data class GroupSettingUpdateResult(
    val result: String,
    val updateRows: Int? = 0
)
