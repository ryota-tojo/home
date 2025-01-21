package com.example.home.infrastructure.persistence.repository.user

import com.example.home.domain.user.UserInfo
import com.example.home.domain.value_object.user.*

interface UserInfoRepository {
    fun refer(userId: UserId): List<UserInfo>
    fun referAll(): List<UserInfo>
    fun save(
        userName: UserName,
        password: UserPassword,
        permission: UserPermission,
        approvalFlg: UserApprovalFlg,
        deleteFlg: UserDeleteFlg
    ): UserInfo

    fun update(
        userId: UserId,
        userName: UserName,
        password: UserPassword,
        permission: UserPermission,
        approvalFlg: UserApprovalFlg,
        deleteFlg: UserDeleteFlg
    ): Int

    fun delete(userId: UserId): Int
}