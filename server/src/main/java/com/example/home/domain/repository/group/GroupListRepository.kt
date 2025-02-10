package com.example.home.domain.repository.group

import com.example.home.domain.entity.group.GroupList
import com.example.home.domain.value_object.group.GroupName
import com.example.home.domain.value_object.group.GroupPassword
import com.example.home.domain.value_object.group.GroupsId

interface GroupListRepository {
    fun refer(groupsId: GroupsId? = null): List<GroupList>
    fun certification(groupsId: GroupsId, groupPassword: GroupPassword): Boolean
    fun save(
        groupsId: GroupsId,
        groupName: GroupName,
        groupPassword: GroupPassword
    ): GroupList

    fun update(
        groupsId: GroupsId,
        groupName: GroupName? = null,
        groupPassword: GroupPassword? = null
    ): Int
    fun delete(groupsId: GroupsId) :Int
}