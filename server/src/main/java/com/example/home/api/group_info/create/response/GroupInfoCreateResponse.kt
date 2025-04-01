package com.example.home.api.group_info.create.response

import com.fasterxml.jackson.annotation.JsonProperty

data class GroupInfoCreateResponse(
    @JsonProperty("status")
    val status: String,

    @JsonProperty("message")
    val message: String,

    @JsonProperty("data")
    val data: DataObject

) {
    data class DataObject(
        @JsonProperty("group_info")
        val groupInfo: GroupInfoObject? = null
    )

    data class GroupInfoObject(
        @JsonProperty("groups_id")
        val groupsId: String? = null,

        @JsonProperty("user_id")
        val userId: String? = null,

        @JsonProperty("leader")
        val leader: String? = null,

        @JsonProperty("approval")
        val approval: String? = null,

        @JsonProperty("create_date")
        val createDate: String? = null,

        @JsonProperty("update_date")
        val updateDate: String? = null,

        )
}