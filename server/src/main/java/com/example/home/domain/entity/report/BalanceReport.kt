package com.example.home.domain.entity.report

import com.example.home.domain.value_object.etc.Amount

data class BalanceReport(
    val categoryAndAmountList: List<CategoryAndAmount>,
    val totalAmount: Amount
)
