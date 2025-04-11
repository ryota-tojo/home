package com.example.home.api.analysis.yoy.response

import com.fasterxml.jackson.annotation.JsonProperty

data class AnalysisYoYResponse(
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

        @JsonProperty("month_01")
        val month1: MonthlyDataObject? = null,

        @JsonProperty("month_02")
        val month2: MonthlyDataObject? = null,

        @JsonProperty("month_03")
        val month3: MonthlyDataObject? = null,

        @JsonProperty("month_04")
        val month4: MonthlyDataObject? = null,

        @JsonProperty("month_05")
        val month5: MonthlyDataObject? = null,

        @JsonProperty("month_06")
        val month6: MonthlyDataObject? = null,

        @JsonProperty("month_07")
        val month7: MonthlyDataObject? = null,

        @JsonProperty("month_08")
        val month8: MonthlyDataObject? = null,

        @JsonProperty("month_09")
        val month9: MonthlyDataObject? = null,

        @JsonProperty("month_10")
        val month10: MonthlyDataObject? = null,

        @JsonProperty("month_11")
        val month11: MonthlyDataObject? = null,

        @JsonProperty("month_12")
        val month12: MonthlyDataObject? = null,

        @JsonProperty("average")
        val average: MonthlyDataObject? = null,

        @JsonProperty("total")
        val total: MonthlyDataObject? = null
    )

    data class MonthlyDataObject(
        @JsonProperty("pre_expense_amount")
        val preExpenseAmount: Int? = null,

        @JsonProperty("expense_amount")
        val expenseAmount: Int? = null,

        @JsonProperty("difference")
        val difference: Int? = null,

        @JsonProperty("yoy")
        val yoy: Double? = null
    )

    data class TotalObject(
        @JsonProperty("month_01")
        val month1: TotalMonthlyDataObject? = null,

        @JsonProperty("month_02")
        val month2: TotalMonthlyDataObject? = null,

        @JsonProperty("month_03")
        val month3: TotalMonthlyDataObject? = null,

        @JsonProperty("month_04")
        val month4: TotalMonthlyDataObject? = null,

        @JsonProperty("month_05")
        val month5: TotalMonthlyDataObject? = null,

        @JsonProperty("month_06")
        val month6: TotalMonthlyDataObject? = null,

        @JsonProperty("month_07")
        val month7: TotalMonthlyDataObject? = null,

        @JsonProperty("month_08")
        val month8: TotalMonthlyDataObject? = null,

        @JsonProperty("month_09")
        val month9: TotalMonthlyDataObject? = null,

        @JsonProperty("month_10")
        val month10: TotalMonthlyDataObject? = null,

        @JsonProperty("month_11")
        val month11: TotalMonthlyDataObject? = null,

        @JsonProperty("month_12")
        val month12: TotalMonthlyDataObject? = null,

        @JsonProperty("average")
        val average: TotalMonthlyDataObject? = null,

        @JsonProperty("total")
        val total: TotalMonthlyDataObject? = null
    )

    data class TotalMonthlyDataObject(
        @JsonProperty("pre_expense_amount")
        val preExpenseAmount: Int? = null,

        @JsonProperty("expense_amount")
        val expenseAmount: Int? = null,

        @JsonProperty("difference")
        val difference: Int? = null,

        @JsonProperty("yoy")
        val yoy: Double? = null
    )

}