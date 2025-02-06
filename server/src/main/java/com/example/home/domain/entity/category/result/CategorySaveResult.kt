package com.example.home.domain.entity.category.result

import com.example.home.domain.entity.category.Category

data class CategorySaveResult(
    val result: String,
    val category: Category? = null
)
