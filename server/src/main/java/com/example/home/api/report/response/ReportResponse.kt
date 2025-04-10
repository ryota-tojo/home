package com.example.home.api.report.response

import com.fasterxml.jackson.annotation.JsonProperty

data class ReportResponse(
    @JsonProperty("status")
    val status: String,

    @JsonProperty("message")
    val message: String,

    @JsonProperty("data")
    val data: DataObject

) {
    data class DataObject(
        @JsonProperty("report")
        val report: ReportObject? = null
    )

    data class ReportObject(
        @JsonProperty("target_year")
        val targetYear: Int? = null,

        @JsonProperty("target_month")
        val targetMonth: Int? = null,

        @JsonProperty("settlement_report")
        val settlementReport: SettlementReportObject? = null,

        @JsonProperty("balance_report")
        val balanceReport: BalanceReportObject? = null,

        )

    data class SettlementReportObject(

        @JsonProperty("un_settlement_count")
        val unSettlementCount: Int? = null,

        @JsonProperty("un_settlement_data_list")
        val unSettlementDataList: List<UnSettlementDataObject>? = null,

        @JsonProperty("un_settlement_total")
        val unSettlementTotal: Int? = null,

        )

    data class UnSettlementDataObject(

        @JsonProperty("member_id")
        val memberId: Int? = null,

        @JsonProperty("member_name")
        val memberName: String? = null,

        @JsonProperty("un_settlement_amount")
        val unSettlementAmount: Int? = null,

        )

    data class BalanceReportObject(

        @JsonProperty("category_list")
        val categoryList: List<CategoryListObject>? = null,

        @JsonProperty("balance_total")
        val balanceTotal: Int? = null,

        )

    data class CategoryListObject(

        @JsonProperty("category_id")
        val categoryId: Int? = null,

        @JsonProperty("category_name")
        val categoryName: String? = null,

        @JsonProperty("balance_amount")
        val balanceAmount: Int? = null,

        )
}