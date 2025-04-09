package com.example.home.api.template.entry.delete.response

import com.fasterxml.jackson.annotation.JsonProperty

data class ShoppingEntryDeleteResponse(
    @JsonProperty("status")
    val status: String,

    @JsonProperty("message")
    val message: String,

    @JsonProperty("data")
    val data: DataObject

) {
    data class DataObject(
        @JsonProperty("delete_rows")
        val deleteRows: Int? = null

    )
}