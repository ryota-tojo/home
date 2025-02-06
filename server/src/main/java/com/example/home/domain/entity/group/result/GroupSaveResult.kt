package com.example.home.domain.entity.group.result

import com.example.home.domain.entity.group.GroupList
import com.example.home.domain.entity.group.GroupSetting

data class GroupSaveResult(
    val result: String,
    val groupList: com.example.home.domain.entity.group.GroupList? = null,
    val groupSettingList: List<com.example.home.domain.entity.group.GroupSetting>? = null
)

