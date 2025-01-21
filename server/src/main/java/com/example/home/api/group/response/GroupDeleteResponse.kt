package com.example.home.api.group.response

import com.fasterxml.jackson.annotation.JsonProperty

data class GroupDeleteResponse(
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
        @JsonProperty("groups_id")
        val groupsId: String,

        @JsonProperty("group_list_deleted_rows")
        val groupListDeletedRows: Int? = 0,

        @JsonProperty("group_setting_deleted_rows")
        val groupSettingDeletedRows: Int? = 0
    )

}