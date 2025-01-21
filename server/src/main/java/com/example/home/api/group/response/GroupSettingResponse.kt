package com.example.home.api.group.response

import com.fasterxml.jackson.annotation.JsonProperty

data class GroupSettingResponse(
    @JsonProperty("id")
    val id: Int? = null,

    @JsonProperty("groups_id")
    val groupsId: String? = null,

    @JsonProperty("group_setting_key")
    val groupSettingKey: String? = null,

    @JsonProperty("group_setting_value")
    val groupSettingValue: String? = null
)
