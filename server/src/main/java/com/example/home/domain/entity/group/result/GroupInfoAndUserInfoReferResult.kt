package com.example.home.domain.entity.group.result

import com.example.home.domain.entity.group.GroupInfoAndUserInfo

data class GroupInfoAndUserInfoReferResult(
    val result: String,
    val groupInfoAndUserInfoList: List<GroupInfoAndUserInfo>? = null
)
