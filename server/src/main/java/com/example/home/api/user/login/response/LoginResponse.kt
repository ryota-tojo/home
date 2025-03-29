package com.example.home.api.user.login.response

import com.fasterxml.jackson.annotation.JsonProperty

data class LoginResponse(
    @JsonProperty("status")
    val status: String,

    @JsonProperty("message")
    val message: String,

    @JsonProperty("data")
    val data: DataObject

) {
    data class DataObject(
        @JsonProperty("login_result")
        val loginResult: Boolean

    )
}