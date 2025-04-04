package com.example.home.api.member.disable.request

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.NotNull

data class MemberDisableRequest(
    @JsonProperty("member_id")
    @field:NotNull(message = "キー「member_id」が存在しません")
    val memberId: Int,

    )
