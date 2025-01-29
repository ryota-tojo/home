package com.example.home.domain.repository.member

import com.example.home.domain.entity.member.Member
import com.example.home.domain.value_object.group.GroupsId
import com.example.home.domain.value_object.member.MemberName
import com.example.home.domain.value_object.member.MemberNo

interface MemberRepository {
    fun refer(groupsId: GroupsId): List<com.example.home.domain.entity.member.Member>
    fun referAll(): List<com.example.home.domain.entity.member.Member>
    fun save(
        groupsId: GroupsId,
        memberNo: MemberNo,
        memberName: MemberName
    ): com.example.home.domain.entity.member.Member

    fun update(
        groupsId: GroupsId,
        memberNo: MemberNo,
        memberName: MemberName
    ): Int

    fun delete(groupsId: GroupsId, memberNo: MemberNo? = null, memberName: MemberName? = null): Int
}