package com.example.home.data.group

import com.example.home.domain.entity.group.GroupList
import com.example.home.domain.value_object.group.GroupName
import com.example.home.domain.value_object.group.GroupPassword
import com.example.home.domain.value_object.group.GroupsId

class FixtureGroupList {

    static 所属グループID_正常() {
        new GroupsId("groups_id")
    }

    static 所属グループ名_正常() {
        new GroupName("group_name")
    }

    static 所属グループパスワード_正常() {
        new GroupPassword("group_password")
    }

    static 所属グループ一覧_正常() {
        return new GroupList(
                1,
                所属グループID_正常(),
                所属グループ名_正常(),
                所属グループパスワード_正常()
        )
    }

}