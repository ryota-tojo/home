package com.example.home.domain.entity.template.result

import com.example.home.domain.entity.template.ShoppingInputTemplate

data class ShoppingInputTemplateSaveResult(
    val result: String,
    val shoppingInputTemplate: ShoppingInputTemplate? = null
)
