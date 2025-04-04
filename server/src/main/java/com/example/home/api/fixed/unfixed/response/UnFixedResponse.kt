package com.example.home.api.fixed.unfixed.response

import com.fasterxml.jackson.annotation.JsonProperty

data class UnFixedResponse(
    @JsonProperty("status")
    val status: String,

    @JsonProperty("message")
    val message: String,

    @JsonProperty("data")
    val data: DataObject

) {
    data class DataObject(
        @JsonProperty("unfixed_rows")
        val unFixedRowsObject: UnFixedRowsObject? = null
    )

    data class UnFixedRowsObject(
        @JsonProperty("budgets")
        val budgetsUnFixedRows: Int,

        @JsonProperty("shopping")
        val shoppingUnFixedRows: Int,

        @JsonProperty("comment")
        val commentUnFixedRows: Int,

        )

}