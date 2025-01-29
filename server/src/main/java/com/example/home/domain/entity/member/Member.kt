package com.example.home.domain.entity.member

import com.example.home.domain.value_object.group.GroupsId
import com.example.home.domain.value_object.member.MemberId
import com.example.home.domain.value_object.member.MemberName
import com.example.home.domain.value_object.member.MemberNo

data class Member(
    val id: MemberId,
    val groupsId: GroupsId,
    val memberyNo: MemberNo,
    val memberyName: MemberName
)