package com.example.home.domain.entity.group.result

import com.example.home.domain.value_object.user.UserId

data class GroupInfoLeaderChangeResult(
    val result: String,
    val leader: UserId,
    val newLeader: UserId
)
