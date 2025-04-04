package com.example.home.api.member.update.request

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class MemberUpdateRequest(
    @JsonProperty("member_id")
    @field:NotNull(message = "キー「member_id」が存在しません")
    @field:NotBlank(message = "キー「member_id」が未入力です")
    val memberId: Int,

    @JsonProperty("member_no")
    val memberNo: Int? = null,

    @JsonProperty("member_name")
    val memberName: String? = null,

    )
