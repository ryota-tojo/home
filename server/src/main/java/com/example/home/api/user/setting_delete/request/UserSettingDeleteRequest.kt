package com.example.home.api.group.create.request

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

data class UserSettingDeleteRequest(
    @JsonProperty("user_id")
    @field:NotNull(message = "キー「user_id」が存在しません")
    val userId: Int,

    @JsonProperty("setting_key")
    @field:Size(max = 64, message = "キー「setting_key」は512桁以内で入力してください")
    val settingKey: String?=null,

)
