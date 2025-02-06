package com.example.home.data.user


import com.example.home.domain.entity.user.LoginInfo
import com.example.home.domain.value_object.group.GroupsId
import com.example.home.domain.value_object.user.UserName
import com.example.home.domain.value_object.user.UserPassword

class FixtureLoginInfo {
    static 正常値() {
        return new LoginInfo(
                new GroupsId("groups_id"),
                new UserName("user_name"),
                new UserPassword("password")
        )
    }
}