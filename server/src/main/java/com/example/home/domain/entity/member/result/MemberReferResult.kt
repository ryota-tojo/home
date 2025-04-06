package com.example.home.domain.entity.member.result

import com.example.home.domain.entity.member.Member

data class MemberReferResult(
    val result: String,
    val member: List<Member>? = null
)
