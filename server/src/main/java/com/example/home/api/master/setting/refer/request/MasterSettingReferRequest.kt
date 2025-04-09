package com.example.home.api.master.setting.refer.request

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.Size

data class MasterSettingReferRequest(
    @JsonProperty("setting_key")
    @field:Size(max = 512, message = "キー「setting_key」は512桁以内で入力してください")
    val settingKey: String?,

    )
