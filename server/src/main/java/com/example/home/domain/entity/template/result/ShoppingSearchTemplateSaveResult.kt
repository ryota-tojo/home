package com.example.home.domain.entity.template.result

import com.example.home.domain.entity.template.ShoppingSearchTemplate

data class ShoppingSearchTemplateSaveResult(
    val result: String,
    val shoppingInputTemplate: ShoppingSearchTemplate? = null
)
