package com.example.home.domain.entity.report

import com.example.home.domain.entity.member.Member
import com.example.home.domain.value_object.etc.Amount

data class MemberAndAmount(
    val member: Member,
    val amount: Amount,
)
