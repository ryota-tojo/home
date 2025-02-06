package com.example.home.api.group.response

import com.example.home.domain.group.GroupList
import com.fasterxml.jackson.annotation.JsonProperty

data class GroupListUpdateResponse(
    @JsonProperty("status")
    val status: String,

    @JsonProperty("response_code")
    val responseCode: String,

    @JsonProperty("message")
    val message: String,

    @JsonProperty("data")
    val data: GroupListUpdateResponseData

) {
    data class GroupListUpdateResponseData(
        @JsonProperty("update_rows")
        val updateRows: Int? = 0
    )

}