package com.example.home.api.template.input.update.response

import com.fasterxml.jackson.annotation.JsonProperty

data class ShoppingInputUpdateResponse(
    @JsonProperty("status")
    val status: String,

    @JsonProperty("message")
    val message: String,

    @JsonProperty("data")
    val data: DataObject

) {
    data class DataObject(
        @JsonProperty("update_rows")
        val updateRows: Int? = null

    )
}