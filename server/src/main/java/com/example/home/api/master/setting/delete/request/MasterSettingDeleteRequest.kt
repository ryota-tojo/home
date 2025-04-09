package com.example.home.api.master.setting.delete.request

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

data class MasterSettingDeleteRequest(
    @JsonProperty("setting_key")
    @field:NotNull(message = "キー「setting_key」が存在しません")
    @field:NotBlank(message = "キー「setting_key」が未入力です")
    @field:Size(max = 512, message = "キー「setting_key」は512桁以内で入力してください")
    val settingKey: String,

    )
