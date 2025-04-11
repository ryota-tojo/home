package com.example.home.api.analysis.pa.response

import com.fasterxml.jackson.annotation.JsonProperty

data class AnalysisPAResponse(
    @JsonProperty("status")
    val status: String,

    @JsonProperty("message")
    val message: String,

    @JsonProperty("data")
    val data: DataObject

) {
    data class DataObject(
        @JsonProperty("category_list")
        val categoryList: List<CategoryObject>? = null,

        @JsonProperty("total")
        val total: TotalObject? = null
    )

    data class CategoryObject(
        @JsonProperty("category_id")
        val categoryId: Int? = null,

        @JsonProperty("category_name")
        val categoryName: String? = null,

        @JsonProperty("month_data")
        val monthData: List<MonthDataObject>? = null,

        @JsonProperty("year_data")
        val yearData: YearDataObject? = null,
    )

    data class MonthDataObject(
        @JsonProperty("expense_amount")
        val expenseAmount: Int? = null,

        @JsonProperty("budgets_amount_and_income")
        val budgetsAmountAndIncome: Int? = null,

        @JsonProperty("average")
        val average: Int? = null,

        @JsonProperty("usage_rate")
        val usageRate: Double? = null,

        @JsonProperty("remnant")
        val remnant: Int? = null

    )

    data class YearDataObject(
        @JsonProperty("year_expense_amount")
        val yearExpenseAmount: Int? = null,

        @JsonProperty("year_budgets_amount_and_income")
        val yearBudgetsAmountAndIncome: Int? = null,

        @JsonProperty("year_average")
        val yearAverage: Int? = null,

        @JsonProperty("year_usage_rate")
        val yearUsageRate: Double? = null,

        @JsonProperty("year_remnant")
        val yearRemnant: Int? = null

    )

    data class TotalObject(
        @JsonProperty("total_expense_amount")
        val totalExpenseAmount: Int? = null,

        @JsonProperty("total_budgets_amount_and_income")
        val totalBudgetsAmountAndIncome: Int? = null,

        @JsonProperty("total_average")
        val totalAverage: Int? = null,

        @JsonProperty("total_usage_rate")
        val totalUsageRate: Double? = null,

        @JsonProperty("total_remnant")
        val totalRemnant: Int? = null
    )

}