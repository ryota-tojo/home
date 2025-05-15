package com.example.home.domain.entity.group

import com.example.home.domain.value_object.group.GroupApprovalFlg
import com.example.home.domain.value_object.group.GroupsId
import com.example.home.domain.value_object.user.*

data class GroupInfoAndUserInfo(
    val groupsId: GroupsId,
    val userId: UserId,
    val userName: UserName,
    val userPermission: UserPermission,
    val userApprovalFlg: UserApprovalFlg,
    val userDeleteFlg: UserDeleteFlg,
    val userLeaderFlg: UserLeaderFlg,
    val groupApprovalFlg: GroupApprovalFlg
)
