package com.example.home.api.group.response

import com.fasterxml.jackson.annotation.JsonProperty

data class GroupEntryResponse(
    @JsonProperty("status")
    val status: String,

    @JsonProperty("response_code")
    val responseCode: String,

    @JsonProperty("message")
    val message: String,

    @JsonProperty("data")
    val data: GroupEntryResponseData

) {
    data class GroupEntryResponseData(
        @JsonProperty("group_list")
        val groupListResponse: GroupListResponse? = null,

        @JsonProperty("group_setting")
        val groupSettingResponseList: List<GroupSettingResponse>? = null
    )

}