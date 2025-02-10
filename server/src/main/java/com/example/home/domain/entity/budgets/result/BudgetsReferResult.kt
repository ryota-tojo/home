package com.example.home.domain.entity.budgets.result

import com.example.home.domain.entity.budgets.Budgets

data class BudgetsReferResult(
    val result: String,
    val budgets: List<Budgets>? = null
)
