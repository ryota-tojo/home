package com.example.home.domain.entity.template.result

import com.example.home.domain.entity.template.ShoppingEntryTemplate

data class ShoppingEntryTemplateSaveResult(
    val result: String,
    val shoppingEntryTemplate: ShoppingEntryTemplate? = null
)
