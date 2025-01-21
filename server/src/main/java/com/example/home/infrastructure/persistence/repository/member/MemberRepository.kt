package com.example.home.infrastructure.persistence.repository.member

import com.example.home.domain.member.Member
import com.example.home.domain.value_object.group.GroupsId
import com.example.home.domain.value_object.member.MemberName
import com.example.home.domain.value_object.member.MemberNo

interface MemberRepository {
    fun refer(groupsId: GroupsId): List<Member>
    fun referAll(): List<Member>
    fun save(
        groupsId: GroupsId,
        memberNo: MemberNo,
        memberName: MemberName
    ): Member

    fun update(
        groupsId: GroupsId,
        memberNo: MemberNo,
        memberName: MemberName
    ): Int

    fun delete(groupsId: GroupsId, memberNo: MemberNo? = null, memberName: MemberName? = null): Int
}