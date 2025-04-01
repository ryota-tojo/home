package com.example.home.api.group.update.list.response

import com.fasterxml.jackson.annotation.JsonProperty

data class GroupListUpdateResponse(
    @JsonProperty("status")
    val status: String,

    @JsonProperty("message")
    val message: String,

    @JsonProperty("data")
    val data: DataObject

) {
    data class DataObject(
        @JsonProperty("update_rows")
        val updateRows: Int? = 0
    )

}