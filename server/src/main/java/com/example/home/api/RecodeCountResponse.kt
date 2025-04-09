package com.example.home.api

import com.fasterxml.jackson.annotation.JsonProperty

data class RecodeCountResponse(
    @JsonProperty("status")
    val status: String,

    @JsonProperty("message")
    val message: String,

    @JsonProperty("data")
    val data: DataObject

) {
    data class DataObject(
        @JsonProperty("recode_count")
        val recodeCount: Int? = 0

    )
}