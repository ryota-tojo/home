package com.example.home.api.user.refer.request

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.Size

data class UserReferRequest(
    @JsonProperty("user_id")
    val userId: Int? = null,

    @JsonProperty("user_name")
    @field:Size(max = 64, message = "キー「user_name」は64桁以内で入力してください")
    val userName: String? = null,

    @JsonProperty("offset")
    val offSet: Long? = null,

    @JsonProperty("limit")
    val limit: Int? = null,
    )
