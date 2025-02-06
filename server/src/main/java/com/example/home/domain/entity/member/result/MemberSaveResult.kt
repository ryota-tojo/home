package com.example.home.domain.entity.member.result

import com.example.home.domain.entity.member.Member

data class MemberSaveResult(
    val result: String,
    val category: Member? = null
)
