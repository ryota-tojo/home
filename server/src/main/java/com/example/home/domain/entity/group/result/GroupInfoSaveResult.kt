package com.example.home.domain.entity.group.result

import com.example.home.domain.entity.group.GroupInfo

data class GroupInfoSaveResult(
    val result: String,
    val groupInfo: GroupInfo? = null
)
