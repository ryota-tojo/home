package com.example.home.domain.entity.user.result

import com.example.home.domain.entity.user.UserInfo

data class UserReferAllResult(
    val result: String,
    val userInfo: List<UserInfo>? = null
)
