package com.example.home.domain.entity.analysis

import com.example.home.domain.value_object.analysis.Ratio
import com.example.home.domain.value_object.etc.Amount

data class AnalysisYoYData(
    val timeFrame: String,
    val lastYearExpenseAmount: Amount,
    val thisYearExpenseAmount: Amount,
    val difference: Amount,
    val yoyRatio: Ratio,
)
