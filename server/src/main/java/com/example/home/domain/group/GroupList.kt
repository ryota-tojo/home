package com.example.home.domain.group

import com.example.home.domain.value_object.group.GroupName
import com.example.home.domain.value_object.group.GroupsId

data class GroupList(
    val id : Int,
    val groupsId: GroupsId,
    val groupName: GroupName
)