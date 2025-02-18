package com.example.home.domain.entity.fixed

import com.example.home.domain.value_object.etc.MM
import com.example.home.domain.value_object.etc.YYYY

data class FixedRefer(
    val yyyy: YYYY,
    val mm: MM,
    val fixed: Boolean
)