package com.example.home.api.group.update.list.request

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

data class GroupListUpdateRequest(
    @JsonProperty("groups_id")
    @field:NotNull(message = "キー「groups_id」が存在しません")
    @field:NotBlank(message = "キー「groups_id」が未入力です")
    @field:Size(max = 64, message = "キー「groups_id」は64桁以内で入力してください")
    val groupsId: String,

    @JsonProperty("group_name")
    @field:NotBlank(message = "キー「group_name」が未入力です")
    @field:Size(max = 64, message = "キー「group_name」は64桁以内で入力してください")
    val groupName: String? = null,

    @JsonProperty("group_password")
    @field:NotBlank(message = "キー「group_password」が未入力です")
    @field:Size(max = 64, message = "キー「group_password」は64桁以内で入力してください")
    val groupPassword: String? = null,
)
