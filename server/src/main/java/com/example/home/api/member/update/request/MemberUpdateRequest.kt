package com.example.home.api.member.update.request

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.NotNull

data class MemberUpdateRequest(
    @JsonProperty("member_id")
    @field:NotNull(message = "キー「member_id」が存在しません")
    val memberId: Int,

    @JsonProperty("member_no")
    val memberNo: Int? = null,

    @JsonProperty("member_name")
    val memberName: String? = null,

    )
