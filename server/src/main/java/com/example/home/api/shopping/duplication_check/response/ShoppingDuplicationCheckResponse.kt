package com.example.home.api.shopping.duplication_check.response

import com.fasterxml.jackson.annotation.JsonProperty

data class ShoppingDuplicationCheckResponse(
    @JsonProperty("status")
    val status: String,

    @JsonProperty("message")
    val message: String,

    @JsonProperty("data")
    val data: DataObject

) {
    data class DataObject(
        @JsonProperty("duplication_flag")
        val duplicationFlag: Int? = null
    )
}