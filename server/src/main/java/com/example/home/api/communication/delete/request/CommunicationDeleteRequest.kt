package com.example.home.api.communication.delete.request

import com.fasterxml.jackson.annotation.JsonProperty

data class CommunicationDeleteRequest(
    @JsonProperty("groups_id")
    val groupsId: String? = null,

    @JsonProperty("gift_id")
    val giftId: Int? = null,

    )
