package com.example.home.infrastructure.persistence.repository.group

import com.example.home.domain.group.GroupList
import com.example.home.domain.value_object.group.GroupName
import com.example.home.domain.value_object.group.GroupsId
import com.example.home.domain.value_object.user.*
import java.time.LocalDateTime

interface GroupInfoRepository {
    fun find(groupsId: GroupsId): Boolean
    fun refer(groupsId: GroupsId): GroupList
    fun save(
        groupsId: GroupsId,
        userId: UserId,
        userLeaderFlg: UserLeaderFlg,
        createDate: LocalDateTime,
        updateDate: LocalDateTime
    ): Int
    fun update(
        groupsId: GroupsId,
        userId: UserId,
        userLeaderFlg: UserLeaderFlg,
        createDate: LocalDateTime,
        updateDate: LocalDateTime
    ): Int
    fun delete(userId: UserId) :Boolean
}