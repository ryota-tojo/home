package com.example.home.api.group_info.update.request

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

data class GroupInfoUpdateRequest(
    @JsonProperty("groups_id")
    @field:NotNull(message = "キー「groups_id」が存在しません")
    @field:NotBlank(message = "キー「groups_id」が未入力です")
    @field:Size(max = 64, message = "キー「groups_id」は64桁以内で入力してください")
    val groupsId: String,

    @JsonProperty("user_id")
    @field:NotNull(message = "キー「user_id」が存在しません")
    val userId: Int,

    @JsonProperty("leader")
    val leader: Int? = null,

    @JsonProperty("approval")
    val approval: Int? = null,
)
