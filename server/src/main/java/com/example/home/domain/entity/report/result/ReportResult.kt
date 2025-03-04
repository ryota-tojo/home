package com.example.home.domain.entity.report.result

import com.example.home.domain.entity.report.Report

data class ReportResult(
    val result: String,
    val report: Report? = null
)
