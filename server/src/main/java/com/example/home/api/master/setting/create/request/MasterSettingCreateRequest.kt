package com.example.home.api.master.setting.create.request

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

data class MasterSettingCreateRequest(
    @JsonProperty("setting_key")
    @field:NotNull(message = "キー「setting_key」が存在しません")
    @field:NotBlank(message = "キー「setting_key」が未入力です")
    @field:Size(max = 512, message = "キー「setting_key」は512桁以内で入力してください")
    val settingKey: String,

    @JsonProperty("setting_value")
    @field:NotNull(message = "キー「setting_value」が存在しません")
    @field:NotBlank(message = "キー「setting_value」が未入力です")
    @field:Size(max = 512, message = "キー「setting_key」は512桁以内で入力してください")
    val settingValue: String,

    @JsonProperty("remarks")
    @field:NotNull(message = "キー「remarks」が存在しません")
    @field:NotBlank(message = "キー「remarks」が未入力です")
    val remarks: String,

    )
