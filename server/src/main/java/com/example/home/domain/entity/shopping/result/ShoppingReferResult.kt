package com.example.home.domain.entity.shopping.result

import com.example.home.domain.entity.shopping.Shopping

data class ShoppingReferResult(
    val result: String,
    val noticeList: List<Shopping>? = null
)
