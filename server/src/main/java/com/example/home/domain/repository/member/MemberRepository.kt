package com.example.home.domain.repository.member

import com.example.home.domain.entity.member.Member
import com.example.home.domain.value_object.group.GroupsId
import com.example.home.domain.value_object.member.MemberId
import com.example.home.domain.value_object.member.MemberName
import com.example.home.domain.value_object.member.MemberNo

interface MemberRepository {
    fun refer(
        memberId: MemberId? = null,
        groupsId: GroupsId? = null,
        memberNo: MemberNo? = null
    ): List<Member>

    fun save(
        groupsId: GroupsId,
        memberNo: MemberNo,
        memberName: MemberName
    ): Member

    fun update(
        memberId: MemberId,
        memberNo: MemberNo? = null,
        memberName: MemberName? = null
    ): Int

    fun setDeleted(memberId: MemberId): Int

    fun delete(groupsId: GroupsId? = null, memberId: MemberId? = null): Int

}