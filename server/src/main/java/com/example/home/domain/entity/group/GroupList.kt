package com.example.home.domain.entity.group

import com.example.home.domain.value_object.group.GroupName
import com.example.home.domain.value_object.group.GroupsId
import com.example.home.domain.value_object.group.GroupPassword

data class GroupList(
    val id : Int,
    val groupsId: GroupsId,
    val groupName: GroupName,
    val groupPassword: GroupPassword
)