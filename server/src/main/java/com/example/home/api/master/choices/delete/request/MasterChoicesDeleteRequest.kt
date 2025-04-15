package com.example.home.api.master.choices.delete.request

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.NotNull

data class MasterChoicesDeleteRequest(
    @JsonProperty("id")
    @field:NotNull(message = "キー「id」が存在しません")
    val id: Int,

    )
