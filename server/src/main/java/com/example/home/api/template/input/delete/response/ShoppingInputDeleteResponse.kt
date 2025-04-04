package com.example.home.api.template.input.delete.response

import com.fasterxml.jackson.annotation.JsonProperty

data class ShoppingInputDeleteResponse(
    @JsonProperty("status")
    val status: String,

    @JsonProperty("message")
    val message: String,

    @JsonProperty("data")
    val data: DataObject

) {
    data class DataObject(
        @JsonProperty("delete_rows")
        val deleteRows: Int

    )
}