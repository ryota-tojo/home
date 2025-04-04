package com.example.home.api.fixed.refer.response

import com.fasterxml.jackson.annotation.JsonProperty

data class FixedReferResponse(
    @JsonProperty("status")
    val status: String,

    @JsonProperty("message")
    val message: String,

    @JsonProperty("data")
    val data: DataObject

) {
    data class DataObject(
        @JsonProperty("fixed_result")
        val fixedResult: FixedResultObject? = null
    )

    data class FixedResultObject(
        @JsonProperty("month_01")
        val month01: Int,

        @JsonProperty("month_02")
        val month02: Int,

        @JsonProperty("month_03")
        val month03: Int,

        @JsonProperty("month_04")
        val month04: Int,

        @JsonProperty("month_05")
        val month05: Int,

        @JsonProperty("month_06")
        val month06: Int,

        @JsonProperty("month_07")
        val month07: Int,

        @JsonProperty("month_08")
        val month08: Int,

        @JsonProperty("month_09")
        val month09: Int,

        @JsonProperty("month_10")
        val month10: Int,

        @JsonProperty("month_11")
        val month11: Int,

        @JsonProperty("month_12")
        val month12: Int

    )

}