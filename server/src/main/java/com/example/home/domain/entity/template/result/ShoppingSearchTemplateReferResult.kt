package com.example.home.domain.entity.template.result

import com.example.home.domain.entity.template.ShoppingSearchTemplate

data class ShoppingSearchTemplateReferResult(
    val result: String,
    val shoppingSearchTemplateList: List<ShoppingSearchTemplate>? = null
)
