package com.example.home.domain.repository.group;

import com.example.home.domain.entity.group.GroupInfoAndUserInfo
import com.example.home.domain.value_object.group.GroupsId
import com.example.home.domain.value_object.user.UserId
import com.example.home.domain.value_object.user.UserLeaderFlg

interface GroupInfoAndUserInfoRepository {
    fun refer(
        groupsId: GroupsId?,
        userId: UserId?,
        leader: UserLeaderFlg? = null,
        offset: Long?,
        limit: Int?
    ): List<GroupInfoAndUserInfo>
}
