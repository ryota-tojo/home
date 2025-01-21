package com.example.home.infrastructure.persistence.repository.user

import com.example.home.domain.user.UserInfo
import com.example.home.domain.value_object.user.UserName
import com.example.home.domain.value_object.user.UserPassword

interface LoginRepository {
    fun refer(userName: UserName, password: UserPassword): UserInfo
}