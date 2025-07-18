package com.example.home.api.master.screen.refer.response

import com.fasterxml.jackson.annotation.JsonProperty

data class MasterScreenReferResponse(
    @JsonProperty("status")
    val status: String,

    @JsonProperty("message")
    val message: String,

    @JsonProperty("data")
    val data: DataObject

) {
    data class DataObject(
        @JsonProperty("screen_list")
        val settingList: List<ScreenObject>? = null
    )

    data class ScreenObject(
        @JsonProperty("screen_id")
        val screenId: String,

        @JsonProperty("screen_name")
        val screenName: String,

        @JsonProperty("remarks")
        val remarks: String,

        )

}