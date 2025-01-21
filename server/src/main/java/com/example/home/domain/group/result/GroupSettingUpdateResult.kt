package com.example.home.domain.group.result

import com.example.home.domain.group.GroupSetting

data class GroupSettingUpdateResult(
    val result: String,
    val updateRows: Int? = 0
)
