package com.example.home.domain.entity.analysis

import com.example.home.domain.value_object.category.CategoryId
import com.example.home.domain.value_object.category.CategoryName

data class AnalysisPACategory(
    val categoryId: CategoryId,
    val categoryName: CategoryName,
    val analysisPAMonthData: List<AnalysisPAData>,
    val analysisPAYearData: AnalysisPAData
)





