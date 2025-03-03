package com.example.home.domain.entity.report

import com.example.home.domain.entity.category.Category
import com.example.home.domain.value_object.etc.Amount

data class CategoryAndAmount(
    val category: Category,
    val amount: Amount
)
