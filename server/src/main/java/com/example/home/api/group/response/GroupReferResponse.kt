package com.example.home.api.group.response

import com.example.home.domain.group.GroupList
import com.example.home.domain.group.GroupSetting
import com.fasterxml.jackson.annotation.JsonProperty

data class GroupReferResponse(
    @JsonProperty("status")
    val status: String,

    @JsonProperty("response_code")
    val responseCode: String,

    @JsonProperty("message")
    val message: String,

    @JsonProperty("data")
    val data: GroupReferResponseData

) {
    data class GroupReferResponseData(
        @JsonProperty("group_list")
        val grouplistList: List<GroupListResponse>,

        @JsonProperty("group_setting")
        val groupSettingList: List<GroupSettingResponse>
    )
}