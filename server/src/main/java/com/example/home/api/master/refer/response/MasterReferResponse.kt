package com.example.home.api.master.refer.response

import com.fasterxml.jackson.annotation.JsonProperty

data class MasterReferResponse(
    @JsonProperty("status")
    val status: String,

    @JsonProperty("message")
    val message: String,

    @JsonProperty("data")
    val data: DataObject

) {
    data class DataObject(
        @JsonProperty("setting_list")
        val settingList: List<SettingObject>? = null
    )

    data class SettingObject(
        @JsonProperty("setting_key")
        val settingKey: String? = null,

        @JsonProperty("setting_value")
        val settingValue: String? = null,

        @JsonProperty("remarks")
        val remarks: String? = null,

        )

}