package com.example.home.api.member.refer.request

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.Size

data class MemberReferRequest(
    @JsonProperty("member_id")
    val memberId: Int,

    @JsonProperty("groups_id")
    @field:Size(max = 64, message = "キー「groups_id」は64桁以内で入力してください")
    val groupsId: String,

    @JsonProperty("member_no")
    val memberNo: Int,

    )
