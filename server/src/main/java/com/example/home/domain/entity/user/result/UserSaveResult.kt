package com.example.home.domain.entity.user.result

import com.example.home.domain.entity.group.GroupInfo
import com.example.home.domain.entity.user.UserInfo
import com.example.home.domain.entity.user.UserRefer
import com.example.home.domain.entity.user.UserSetting

data class UserSaveResult(
    val result: String,
    val userRefer: UserRefer? = null
)
