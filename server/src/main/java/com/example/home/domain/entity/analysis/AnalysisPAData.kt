package com.example.home.domain.entity.analysis

import com.example.home.domain.value_object.analysis.UseRate
import com.example.home.domain.value_object.etc.Amount

data class AnalysisPAData(
    val timeFrame: String,
    val expenseAmount: Amount,
    val incomeAndBudgetsAmount: Amount,
    val averageAmount: Amount,
    val useRate: UseRate,
    val balanceAmount: Amount,
)
