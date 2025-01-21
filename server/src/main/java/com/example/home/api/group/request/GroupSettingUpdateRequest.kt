package com.example.home.api.group.request

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

data class GroupSettingUpdateRequest(
    @JsonProperty("groups_id")
    @field:NotNull(message = "GroupsID is Null")
    @field:NotBlank(message = "GroupsID cannot be blank")
    @field:Size(max = 64, message = "GroupsID must be 64 characters")
    val groupsId: String,

    @JsonProperty("group_setting_key")
    @field:NotNull(message = "GroupSettingKey is Null")
    @field:NotBlank(message = "GroupSettingKey name cannot be blank")
    @field:Size(max = 512, message = "GroupSettingKey name must be 512 characters")
    val groupSettingKey: String,

    @JsonProperty("group_setting_value")
    @field:Size(max = 256, message = "GroupSettingValue name must be 256 characters")
    val groupSettingValue: String
)
