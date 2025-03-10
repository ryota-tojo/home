package com.example.home.domain.entity.analysis

import com.example.home.domain.value_object.category.CategoryId
import com.example.home.domain.value_object.category.CategoryName

data class AnalysisYoYCategory(
    val categoryId: CategoryId,
    val categoryName: CategoryName,
    val analysisYoYCategoryMonthData: List<AnalysisYoYData>,
    val analysisYoYCategoryAverageData: AnalysisYoYData,
    val analysisYoYCategoryYearData: AnalysisYoYData
)





