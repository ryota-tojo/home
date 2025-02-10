package com.example.home.domain.entity.budgets.result

import com.example.home.domain.entity.budgets.Budgets

data class BudgetsSaveResult(
    val result: String,
    val budgets: Budgets? = null
)
