package com.example.home.api.group_info_and_user_info.refer.response

import com.fasterxml.jackson.annotation.JsonProperty

data class GroupInfoAndUserInfoReferResponse(
    @JsonProperty("status")
    val status: String,

    @JsonProperty("message")
    val message: String,

    @JsonProperty("data")
    val data: DataObject

) {
    data class DataObject(
        @JsonProperty("group_info")
        val groupInfo: List<GroupInfoObject>? = null
    )

    data class GroupInfoObject(
        @JsonProperty("groups_id")
        val groupsId: String? = null,

        @JsonProperty("user_id")
        val userId: String? = null,

        @JsonProperty("user_name")
        val userName: String? = null,

        @JsonProperty("user_permission")
        val userPermission: String? = null,

        @JsonProperty("user_approval")
        val userApprovalFlg: String? = null,

        @JsonProperty("user_deleted")
        val userDeleteFlg: String? = null,

        @JsonProperty("group_leader")
        val leader: String? = null,

        @JsonProperty("group_approval")
        val approval: String? = null,

        )
}