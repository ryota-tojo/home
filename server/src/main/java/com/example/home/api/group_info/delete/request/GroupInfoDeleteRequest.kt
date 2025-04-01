package com.example.home.api.group_info.delete.request

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.Size

data class GroupInfoDeleteRequest(
    @JsonProperty("groups_id")
    @field:Size(max = 64, message = "キー「groups_id」は64桁以内で入力してください")
    val groupsId: String? = null,

    @JsonProperty("user_id")
    val userId: Int? = null,
)
