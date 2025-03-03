package com.example.home.domain.entity.group.result

import com.example.home.domain.entity.group.GroupInfo

data class GroupInfoReferResult(
    val result: String,
    val groupInfoList: List<GroupInfo>? = null
)
