package com.example.home.api.group.create.response

import com.fasterxml.jackson.annotation.JsonProperty

data class GroupCreateResponse(
    @JsonProperty("status")
    val status: String,

    @JsonProperty("message")
    val message: String,

    @JsonProperty("data")
    val data: DataObject

) {
    data class DataObject(
        @JsonProperty("group")
        val group: GroupObject? = null
    )

    data class GroupObject(
        @JsonProperty("group_list")
        val groupList: GroupListObject? = null,

        @JsonProperty("group_setting")
        val groupSettingList: List<GroupSettingObject>? = null
    )

    data class GroupListObject(
        @JsonProperty("id")
        val id: String,

        @JsonProperty("groups_id")
        val groupsId: String? = null,

        @JsonProperty("group_name")
        val groupName: String? = null,

        @JsonProperty("group_password")
        val groupPassword: String? = null,

        )

    data class GroupSettingObject(
        @JsonProperty("setting_key")
        val settingKey: String? = null,

        @JsonProperty("setting_value")
        val settingValue: String? = null,

        )
}