package com.example.home.domain.user

import com.example.home.domain.value_object.user.*
import java.time.LocalDateTime

data class UserInfo(
    val userName: UserName,
    val password: UserPassword,
    val permission: UserPermission,
    val approvalFlg: UserApprovalFlg,
    val deleteFlg: UserDeleteFlg,
    val createDate: LocalDateTime,
    val updateDate: LocalDateTime,
    val approvalDate: LocalDateTime,
    val deleteDate: LocalDateTime
)