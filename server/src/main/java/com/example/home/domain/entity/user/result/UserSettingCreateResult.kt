package com.example.home.domain.entity.user.result

import com.example.home.domain.entity.user.UserSetting

data class UserSettingCreateResult(
    val result: String,
    val userSetting: UserSetting? = null
)
