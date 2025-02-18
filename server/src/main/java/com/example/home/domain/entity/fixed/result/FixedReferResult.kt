package com.example.home.domain.entity.fixed.result

import com.example.home.domain.entity.fixed.FixedRefer

data class FixedReferResult(
    val result: String,
    val fixedRefer: List<FixedRefer>
)
