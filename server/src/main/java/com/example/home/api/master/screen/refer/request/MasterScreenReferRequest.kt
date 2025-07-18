package com.example.home.api.master.screen.refer.request

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.Size

data class MasterScreenReferRequest(
    @JsonProperty("screen_id")
    @field:Size(max = 256, message = "キー「screen_id」は256桁以内で入力してください")
    val screenId: String?,

    )
