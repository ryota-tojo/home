package com.example.home.api.group.response

import com.example.home.domain.group.GroupSetting
import com.fasterxml.jackson.annotation.JsonProperty

data class GroupSettingUpdateResponse(
    @JsonProperty("status")
    val status: String,

    @JsonProperty("response_code")
    val responseCode: String,

    @JsonProperty("message")
    val message: String,

    @JsonProperty("data")
    val data: GroupSettingUpdateResponseData

) {
    data class GroupSettingUpdateResponseData(
        @JsonProperty("update_rows")
        val updateRows: Int? = 0
    )

}