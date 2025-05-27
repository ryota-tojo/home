package com.example.home.api.group.create.response

import com.fasterxml.jackson.annotation.JsonProperty

data class UserSettingCreateResponse(
    @JsonProperty("status")
    val status: String,

    @JsonProperty("message")
    val message: String,

    @JsonProperty("data")
    val data: DataObject

) {
    data class DataObject(
        @JsonProperty("user_setting")
        val group: UserSettingObject? = null
    )

    data class UserSettingObject(
        @JsonProperty("setting_key")
        val settingKey: String? = null,

        @JsonProperty("setting_value")
        val settingValue: String? = null,

        )
}