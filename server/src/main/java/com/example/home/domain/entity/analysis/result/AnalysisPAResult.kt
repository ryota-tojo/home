package com.example.home.domain.entity.analysis.result

import com.example.home.domain.entity.analysis.AnalysisPA

data class AnalysisPAResult(
    val result: String,
    val analysisPA: AnalysisPA? = null
)
