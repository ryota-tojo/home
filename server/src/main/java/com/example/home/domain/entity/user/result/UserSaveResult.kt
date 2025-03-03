package com.example.home.domain.entity.user.result

import com.example.home.domain.entity.user.UserRefer

data class UserSaveResult(
    val result: String,
    val userRefer: UserRefer? = null
)
