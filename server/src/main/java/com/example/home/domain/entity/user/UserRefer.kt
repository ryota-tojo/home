package com.example.home.domain.entity.user

import com.example.home.domain.entity.group.GroupInfo

data class UserRefer(
    val userInfo: UserInfo? = null,
    val userSetting: List<UserSetting>? = null,
    val groupInfo: List<GroupInfo>? = null,
)
