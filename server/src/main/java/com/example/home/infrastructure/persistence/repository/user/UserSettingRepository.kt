package com.example.home.infrastructure.persistence.repository.user

import com.example.home.domain.user.UserSetting
import com.example.home.domain.value_object.user.UserId
import com.example.home.domain.value_object.user.UserSettingKey
import com.example.home.domain.value_object.user.UserSettingValue

interface UserSettingRepository {
    fun refer(userId: UserId): List<UserSetting>
    fun referAll(): List<UserSetting>
    fun save(
        userId: UserId,
        settingKey: UserSettingKey,
        settingValue: UserSettingValue
    ): UserSetting

    fun update(
        userId: UserId,
        settingKey: UserSettingKey,
        settingValue: UserSettingValue
    ): Int

    fun delete(userId: UserId): Int
}