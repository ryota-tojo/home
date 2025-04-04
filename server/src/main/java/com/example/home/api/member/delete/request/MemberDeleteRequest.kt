package com.example.home.api.member.delete.request

import com.fasterxml.jackson.annotation.JsonProperty

data class MemberDeleteRequest(
    @JsonProperty("groups_id")
    val groupsId: String? = null,

    @JsonProperty("member_id")
    val memberId: Int? = null,
)
