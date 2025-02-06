package com.example.home.domain.user

import com.example.home.domain.value_object.user.UserId
import com.example.home.domain.value_object.user.UserSettingKey
import com.example.home.domain.value_object.user.UserSettingValue

data class UserSetting(
    val userId: UserId,
    val userSettingKey: UserSettingKey,
    val userSettingvalue: UserSettingValue
)
