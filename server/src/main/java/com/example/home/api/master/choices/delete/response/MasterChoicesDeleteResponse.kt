package com.example.home.api.master.choices.delete.response

import com.fasterxml.jackson.annotation.JsonProperty

data class MasterChoicesDeleteResponse(
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