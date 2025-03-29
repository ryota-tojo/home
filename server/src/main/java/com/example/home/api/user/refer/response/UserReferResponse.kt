package com.example.home.api.user.refer.response

import com.fasterxml.jackson.annotation.JsonProperty

data class UserReferResponse(
    @JsonProperty("status")
    val status: String,

    @JsonProperty("message")
    val message: String,

    @JsonProperty("data")
    val data: DataObject

) {
    data class DataObject(
        @JsonProperty("user")
        val user: List<UserObject>? = null
    )

    data class UserObject(
        @JsonProperty("user_info")
        val userInfo: UserInfoObject? = null,

        @JsonProperty("user_setting")
        val userSetting: List<UserSettingObject>? = null,

        @JsonProperty("group_info")
        val groupInfo: List<GroupInfoObject>? = null,

        )

    data class UserInfoObject(
        @JsonProperty("user_id")
        val userId: String? = "",

        @JsonProperty("user_name")
        val userName: String? = "",

        @JsonProperty("password")
        val password: String? = "",

        @JsonProperty("permission")
        val permission: String? = "",

        @JsonProperty("approval")
        val approval: String? = "",

        @JsonProperty("delete")
        val delete: String? = "",

        @JsonProperty("create_date")
        val createDate: String? = "",

        @JsonProperty("update_date")
        val updateDate: String? = "",

        @JsonProperty("approval_date")
        val approvalDate: String? = "",

        @JsonProperty("delete_date")
        val deleteDate: String? = "",

        )

    data class UserSettingObject(
        @JsonProperty("setting_key")
        val settingKey: String? = "",

        @JsonProperty("setting_value")
        val settingValue: String? = "",

        )

    data class GroupInfoObject(
        @JsonProperty("groups_id")
        val id: String? = "",

        @JsonProperty("leader")
        val leader: String? = "",

        @JsonProperty("approval")
        val approval: String? = "",

        @JsonProperty("create_date")
        val createDate: String? = "",

        @JsonProperty("update_date")
        val updateDate: String? = "",

        )

}