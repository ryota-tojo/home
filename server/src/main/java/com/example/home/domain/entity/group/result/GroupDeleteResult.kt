package com.example.home.domain.entity.group.result

data class GroupDeleteResult(
    val result: String,
    val groupListDeletedResult: Int? = 0,
    val groupSettingDeletedResult: Int? = 0
)
