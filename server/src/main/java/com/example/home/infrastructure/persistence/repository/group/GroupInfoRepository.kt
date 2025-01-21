package com.example.home.infrastructure.persistence.repository.group

import com.example.home.domain.group.GroupInfo
import com.example.home.domain.value_object.group.GroupsId
import com.example.home.domain.value_object.user.UserId
import com.example.home.domain.value_object.user.UserLeaderFlg

interface GroupInfoRepository {
    fun refer(groupsId: GroupsId, userId: UserId?): List<GroupInfo>
    fun referAll(): List<GroupInfo>
    fun save(
        groupsId: GroupsId,
        userId: UserId,
        userLeaderFlg: UserLeaderFlg
    ): GroupInfo
    fun update(
        groupsId: GroupsId,
        userId: UserId,
        userLeaderFlg: UserLeaderFlg
    ): Int

    fun delete(userId: UserId): Int
}