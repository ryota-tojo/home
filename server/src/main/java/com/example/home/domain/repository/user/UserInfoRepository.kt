package com.example.home.domain.repository.user

import com.example.home.domain.entity.user.UserInfo
import com.example.home.domain.value_object.user.*

interface UserInfoRepository {
    fun refer(userId: UserId? = null, userName: UserName? = null): List<UserInfo>
    fun save(
        userName: UserName,
        password: UserPassword,
        permission: UserPermission,
        approvalFlg: UserApprovalFlg,
        deleteFlg: UserDeleteFlg
    ): UserInfo

    fun isDuplication(userName: UserName): Boolean

    fun update(
        userId: UserId,
        userName: UserName? = null,
        password: UserPassword? = null,
        permission: UserPermission? = null,
        approvalFlg: UserApprovalFlg? = null,
        deleteFlg: UserDeleteFlg? = null
    ): Int

    fun delete(userId: UserId): Int
}