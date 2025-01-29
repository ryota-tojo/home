package com.example.home.data

import com.example.home.domain.entity.user.LoginInfo
import com.example.home.domain.entity.user.UserInfo
import com.example.home.domain.value_object.group.GroupsId
import com.example.home.domain.value_object.user.UserName
import com.example.home.domain.value_object.user.UserPassword
import io.mockk.every
import io.mockk.mockk

object LoginTestData {
    fun ログイン情報_正常値(): LoginInfo {
        return LoginInfo(
            username = UserName("admin"),
            password = UserPassword("adminadmin"),
            groupsId = GroupsId("maintenance")
        )
    }

    fun ユーザー情報_正常値(): UserInfo {
        return mockk<UserInfo> {
            every { userId } returns 1
            every { deleteFlg.value } returns 0
            every { approvalFlg.value } returns 1
        }
    }

    fun ユーザー情報_削除済み(): UserInfo {
        return mockk<UserInfo> {
            every { userId } returns 1
            every { deleteFlg.value } returns 1
            every { approvalFlg.value } returns 1
        }
    }

    fun ユーザー情報_未承認(): UserInfo {
        return mockk<UserInfo> {
            every { userId } returns 1
            every { deleteFlg.value } returns 0
            every { approvalFlg.value } returns 0
        }
    }
}
