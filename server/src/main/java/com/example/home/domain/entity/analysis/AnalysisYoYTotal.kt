package com.example.home.domain.entity.analysis

data class AnalysisYoYTotal(
    val label: String,
    val analysisYoYTotalMonthData: List<AnalysisYoYData>,
    val analysisYoYTotalAverageData: AnalysisYoYData,
    val analysisYoYTotalYearData: AnalysisYoYData
)





