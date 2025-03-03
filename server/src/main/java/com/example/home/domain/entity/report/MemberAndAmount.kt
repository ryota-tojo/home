package com.example.home.domain.entity.report

import com.example.home.domain.value_object.etc.Amount
import com.example.home.domain.value_object.member.MemberName

data class MemberAndAmount(
    val member: MemberName,
    val amount: Amount,
)
