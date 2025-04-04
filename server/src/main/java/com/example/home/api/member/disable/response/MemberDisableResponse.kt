package com.example.home.api.member.disable.response

import com.fasterxml.jackson.annotation.JsonProperty

data class MemberDisableResponse(
    @JsonProperty("status")
    val status: String,

    @JsonProperty("message")
    val message: String,

    @JsonProperty("data")
    val data: DataObject

) {
    data class DataObject(
        @JsonProperty("disable_rows")
        val disableRows: Int

    )
}