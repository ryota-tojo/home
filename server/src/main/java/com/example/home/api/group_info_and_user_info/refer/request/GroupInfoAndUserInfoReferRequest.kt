package com.example.home.api.group_info_and_user_info.refer.request

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.Size

data class GroupInfoAndUserInfoReferRequest(
    @JsonProperty("groups_id")
    @field:Size(max = 64, message = "キー「groups_id」は64桁以内で入力してください")
    val groupsId: String? = null,

    @JsonProperty("user_id")
    val userId: Int? = null,

    @JsonProperty("leader")
    val leader: Int? = null,

    @JsonProperty("offset")
    val offSet: Long? = null,

    @JsonProperty("limit")
    val limit: Int? = null,
)
