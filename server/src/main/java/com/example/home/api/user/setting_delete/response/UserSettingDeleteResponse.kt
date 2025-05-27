package com.example.home.api.group.setting_delete.response

import com.fasterxml.jackson.annotation.JsonProperty

data class UserSettingDeleteResponse(
    @JsonProperty("status")
    val status: String,

    @JsonProperty("message")
    val message: String,

    @JsonProperty("data")
    val data: DataObject

) {
    data class DataObject(
        @JsonProperty("delete_rows")
        val updateRows: Int? = 0
    )

}