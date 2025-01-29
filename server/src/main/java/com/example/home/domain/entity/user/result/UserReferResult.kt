package com.example.home.domain.entity.user.result

import com.example.home.domain.entity.group.GroupInfo
import com.example.home.domain.entity.user.UserInfo
import com.example.home.domain.entity.user.UserSetting

data class UserReferResult(
    val result: String,
    val userInfo: UserInfo? = null,
    val userSetting: List<UserSetting>? = null,
    val groupInfo: List<GroupInfo>? = null,
)
