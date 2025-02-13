package com.example.home.domain.entity.template.result

import com.example.home.domain.entity.template.ShoppingInputTemplate

data class ShoppingInputTemplateReferResult(
    val result: String,
    val shoppingInputTemplateList: List<ShoppingInputTemplate>? = null
)
