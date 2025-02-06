package com.example.home.domain.repository.user

import com.example.home.domain.entity.user.UserSetting
import com.example.home.domain.value_object.user.UserId
import com.example.home.domain.value_object.user.UserSettingKey
import com.example.home.domain.value_object.user.UserSettingValue

interface UserSettingRepository {

    fun refer(userId: UserId, settingKey: UserSettingKey): List<UserSetting>

    fun referAll(userId: UserId): List<UserSetting>

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