package com.example.home.api.user.delete.request

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.NotNull

data class UserDeleteRequest(
    @JsonProperty("user_id")
    @field:NotNull(message = "キー「user_id」が存在しません")
    val userId: Int,

    )
