package com.example.home.domain.user.result

import com.example.home.domain.group.GroupInfo
import com.example.home.domain.user.UserInfo
import com.example.home.domain.user.UserSetting

data class UserSaveResult(
    val result: String,
    val userInfo: UserInfo? = null,
    val userSetting: UserSetting? = null,
    val groupInfo: GroupInfo? = null
)
