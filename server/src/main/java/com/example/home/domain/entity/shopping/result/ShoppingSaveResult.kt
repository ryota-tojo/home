package com.example.home.domain.entity.shopping.result

import com.example.home.domain.entity.shopping.Shopping

data class ShoppingSaveResult(
    val result: String,
    val noticeList: Shopping? = null
)
