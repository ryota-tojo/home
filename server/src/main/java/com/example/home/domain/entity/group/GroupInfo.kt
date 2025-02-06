package com.example.home.domain.entity.group

import com.example.home.domain.value_object.group.GroupsId
import com.example.home.domain.value_object.user.UserId
import com.example.home.domain.value_object.user.UserLeaderFlg
import java.time.LocalDateTime

data class GroupInfo(
    val groupsId: GroupsId,
    val userId: UserId,
    val userLeaderFlg: UserLeaderFlg,
    val createDate: LocalDateTime,
    val updateDate: LocalDateTime
)
