package com.example.home.infrastructure.persistence.repository.user

import com.example.home.domain.group.GroupList
import com.example.home.domain.value_object.group.GroupName
import com.example.home.domain.value_object.group.GroupsId
import com.example.home.domain.value_object.user.*
import java.time.LocalDateTime

interface UserInfoRepository {
    fun find(userId: UserId): Boolean
    fun refer(userId: UserId): GroupList
    fun save(
        userName: UserName,
        password: UserPassword,
        permission: UserPermission,
        approvalFlg: UserApprovalFlg,
        deleteFlg: UserDeleteFlg
    ): Int
    fun update(
        userName: UserName,
        password: UserPassword,
        permission: UserPermission,
        approvalFlg: UserApprovalFlg,
        deleteFlg: UserDeleteFlg,
        createDate: LocalDateTime,
        updateDate: LocalDateTime,
        approvalDate: LocalDateTime,
        deleteDate: LocalDateTime
    ): Int
    fun delete(userId: UserId) :Boolean
}