package com.example.home.domain.entity.user.result

import com.example.home.domain.entity.user.UserRefer

data class UserReferResult(
    val result: String,
    val userRefer: List<UserRefer>? = null
)
