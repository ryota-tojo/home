package com.example.home.api.budgets.update.response

import com.fasterxml.jackson.annotation.JsonProperty

data class BudgetsUpdateResponse(
    @JsonProperty("status")
    val status: String,

    @JsonProperty("message")
    val message: String,

    @JsonProperty("data")
    val data: DataObject

) {
    data class DataObject(
        @JsonProperty("update_rows")
        val updateRows: Int
    )
}