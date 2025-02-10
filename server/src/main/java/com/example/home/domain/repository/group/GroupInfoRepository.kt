package com.example.home.domain.repository.group

import com.example.home.domain.entity.group.GroupInfo
import com.example.home.domain.value_object.group.GroupsId
import com.example.home.domain.value_object.user.UserId
import com.example.home.domain.value_object.user.UserLeaderFlg

interface GroupInfoRepository {
    fun refer(groupsId: GroupsId? = null, userId: UserId? = null): List<GroupInfo>
    fun getGroupsId(userId: UserId): GroupsId?
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

    fun delete(groupsId: GroupsId? = null, userId: UserId? = null): Int
}