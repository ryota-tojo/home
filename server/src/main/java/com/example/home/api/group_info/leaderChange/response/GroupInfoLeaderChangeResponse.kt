package com.example.home.api.group_info.leaderChange.response

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.NotNull

data class GroupInfoLeaderChangeResponse(
    @JsonProperty("status")
    val status: String,

    @JsonProperty("message")
    val message: String,

    @JsonProperty("data")
    val data: DataObject

) {
    data class DataObject(
        @JsonProperty("leader_user_id")
        val leaderUserId: Int,

        @JsonProperty("new_leader_user_id")
        val newLeaderUserId: Int,
    )
}