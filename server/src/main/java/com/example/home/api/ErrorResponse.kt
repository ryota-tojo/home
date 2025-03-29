package com.example.home.api

import com.fasterxml.jackson.annotation.JsonProperty

data class ErrorResponse(
    @JsonProperty("status")
    val status: String,

    @JsonProperty("message")
    val message: String,

    @JsonProperty("errors")
    val data: DataObject

) {
    data class DataObject(
        @JsonProperty("parameter")
        val parameter: String,

        @JsonProperty("message")
        val message: String
    )
}