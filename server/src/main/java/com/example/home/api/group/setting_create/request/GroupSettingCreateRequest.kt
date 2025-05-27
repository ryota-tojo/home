package com.example.home.api.group.create.request

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

data class GroupSettingCreateRequest(
    @JsonProperty("groups_id")
    @field:NotNull(message = "キー「groups_id」が存在しません")
    @field:NotBlank(message = "キー「groups_id」が未入力です")
    @field:Size(max = 64, message = "キー「groups_id」は64桁以内で入力してください")
    val groupsId: String,

    @JsonProperty("setting_key")
    @field:NotNull(message = "キー「setting_key」が存在しません")
    @field:NotBlank(message = "キー「setting_key」が未入力です")
    @field:Size(max = 64, message = "キー「setting_key」は512桁以内で入力してください")
    val settingKey: String,

    @JsonProperty("setting_value")
    @field:NotNull(message = "キー「setting_value」が存在しません")
    @field:Size(max = 64, message = "キー「setting_value」は256桁以内で入力してください")
    val settingValue: String,
)
