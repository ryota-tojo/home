package com.example.home.domain.entity.user

import com.example.home.domain.value_object.group.GroupsId
import com.example.home.domain.value_object.user.UserName
import com.example.home.domain.value_object.user.UserPassword


data class LoginInfo(
    val groupsId: GroupsId,
    val username: UserName,
    val password: UserPassword
)
