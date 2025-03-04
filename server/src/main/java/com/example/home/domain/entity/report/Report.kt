package com.example.home.domain.entity.report

import com.example.home.domain.value_object.etc.MM
import com.example.home.domain.value_object.etc.YYYY

data class Report(
    val yyyy: YYYY,
    val mm: MM,
    val settlementReport: SettlementReport,
    val balanceReport: BalanceReport
)
