package com.example.home.domain.entity.analysis.result

import com.example.home.domain.entity.analysis.AnalysisYoY

data class AnalysisYoYResult(
    val result: String,
    val analysisYoY: AnalysisYoY? = null
)
