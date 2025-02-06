package com.example.home.infrastructure.persistence.repository.group

import com.example.home.domain.group.GroupList
import com.example.home.domain.value_object.group.GroupName
import com.example.home.domain.value_object.group.GroupsId

interface GroupListRepository {
    fun refer(groupsId: GroupsId): List<GroupList>
    fun referAll(): List<GroupList>
    fun save(groupsId: GroupsId, groupName: GroupName): GroupList
    fun update(groupsId: GroupsId, groupName: GroupName): Int
    fun delete(groupsId: GroupsId) :Int
}