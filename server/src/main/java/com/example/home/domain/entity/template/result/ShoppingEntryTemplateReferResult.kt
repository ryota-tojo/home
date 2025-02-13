package com.example.home.domain.entity.template.result

import com.example.home.domain.entity.template.ShoppingEntryTemplate

data class ShoppingEntryTemplateReferResult(
    val result: String,
    val shoppingInputTemplateList: List<ShoppingEntryTemplate>? = null
)
