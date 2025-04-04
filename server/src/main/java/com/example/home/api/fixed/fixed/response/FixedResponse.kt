package com.example.home.api.fixed.fixed.response

import com.fasterxml.jackson.annotation.JsonProperty

data class FixedResponse(
    @JsonProperty("status")
    val status: String,

    @JsonProperty("message")
    val message: String,

    @JsonProperty("data")
    val data: DataObject

) {
    data class DataObject(
        @JsonProperty("fixed_rows")
        val fixedRowsObject: FixedRowsObject? = null
    )

    data class FixedRowsObject(
        @JsonProperty("budgets")
        val budgetsFixedRows: Int,

        @JsonProperty("shopping")
        val shoppingFixedRows: Int,

        @JsonProperty("comment")
        val commentFixedRows: Int,

        )

}