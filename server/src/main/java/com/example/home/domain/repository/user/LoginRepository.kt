package com.example.home.domain.repository.user

import com.example.home.domain.entity.user.UserInfo
import com.example.home.domain.value_object.user.UserName
import com.example.home.domain.value_object.user.UserPassword

interface LoginRepository {
    fun refer(userName: UserName, password: UserPassword): com.example.home.domain.entity.user.UserInfo?
}