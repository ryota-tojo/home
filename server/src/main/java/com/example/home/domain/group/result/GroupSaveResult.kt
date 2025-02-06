package com.example.home.domain.group.result

import com.example.home.domain.group.GroupList
import com.example.home.domain.group.GroupSetting

data class GroupSaveResult(
    val result: String,
    val groupList: GroupList? = null,
    val groupSettingList: List<GroupSetting>? = null
)

