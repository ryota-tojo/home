package com.example.home.api.group.response

import com.fasterxml.jackson.annotation.JsonProperty

data class GroupListResponse(
    @JsonProperty("id")
    val groupId: Int? = null,

    @JsonProperty("groups_id")
    val groupsId: String? = null,

    @JsonProperty("group_name")
    val groupName: String? = null,

    @JsonProperty("group_password")
    val groupPassword: String? = null

)
