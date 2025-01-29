package com.example.home.api.group.request

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

data class GroupListUpdateRequest(
    @JsonProperty("groups_id")
    @field:NotNull(message = "GroupsID is Null")
    @field:NotBlank(message = "GroupsID cannot be blank")
    @field:Size(max = 64, message = "GroupsID must be 64 characters")
    val groupsId: String,

    @JsonProperty("group_name")
    @field:Size(max = 64, message = "Group name must be 64 characters")
    val groupName: String,

    @JsonProperty("group_password")
    @field:Size(max = 64, message = "Group password must be 64 characters")
    val groupPassword: String,
)
