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
        @JsonProperty("year")
        val year: String? = null,

        @JsonProperty("fixed_result")
        val fixedResult: FixedResultObject? = null
    )

    data class FixedResultObject(
        @JsonProperty("month_01")
        val month01: Boolean,

        @JsonProperty("month_02")
        val month02: Boolean,

        @JsonProperty("month_03")
        val month03: Boolean,

        @JsonProperty("month_04")
        val month04: Boolean,

        @JsonProperty("month_05")
        val month05: Boolean,

        @JsonProperty("month_06")
        val month06: Boolean,

        @JsonProperty("month_07")
        val month07: Boolean,

        @JsonProperty("month_08")
        val month08: Boolean,

        @JsonProperty("month_09")
        val month09: Boolean,

        @JsonProperty("month_10")
        val month10: Boolean,

        @JsonProperty("month_11")
        val month11: Boolean,

        @JsonProperty("month_12")
        val month12: Boolean

    )

}