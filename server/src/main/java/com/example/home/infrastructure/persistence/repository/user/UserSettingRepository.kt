package com.example.home.infrastructure.persistence.repository.user

import com.example.home.domain.group.GroupList
import com.example.home.domain.value_object.group.GroupName
import com.example.home.domain.value_object.group.GroupsId
import com.example.home.domain.value_object.user.*
import java.time.LocalDateTime

interface UserSettingRepository {
    fun refer(userId: UserId): GroupList
    fun save(
        SettingKey: UserSettingKey,
        Settingvalue: UserSettingValue
    ): Int
    fun update(
        SettingKey: UserSettingKey,
        Settingvalue: UserSettingValue
    ): Int
    fun delete(userId: UserId) :Boolean
}