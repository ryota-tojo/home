package com.example.home.domain.entity.category.result

import com.example.home.domain.entity.category.Category

data class CategoryReferResult(
    val result: String,
    val category: List<Category>? = null
)
