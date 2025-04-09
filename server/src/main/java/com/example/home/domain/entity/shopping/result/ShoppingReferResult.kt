package com.example.home.domain.entity.shopping.result

import com.example.home.domain.entity.shopping.Shopping

data class ShoppingReferResult(
    val result: String,
    val shoppingList: List<Shopping>? = null
)
