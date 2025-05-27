package com.example.home.api.group.create.response

import com.fasterxml.jackson.annotation.JsonProperty

data class GroupSettingCreateResponse(
    @JsonProperty("status")
    val status: String,

    @JsonProperty("message")
    val message: String,

    @JsonProperty("data")
    val data: DataObject

) {
    data class DataObject(
        @JsonProperty("group_setting")
        val group: GroupSettingObject? = null
    )

    data class GroupSettingObject(
        @JsonProperty("setting_key")
        val settingKey: String? = null,

        @JsonProperty("setting_value")
        val settingValue: String? = null,

        )
}