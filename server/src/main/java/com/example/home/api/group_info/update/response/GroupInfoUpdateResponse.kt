package com.example.home.api.group_info.update.response

import com.fasterxml.jackson.annotation.JsonProperty

data class GroupInfoUpdateResponse(
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