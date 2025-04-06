package com.example.home.api.communication.refer.request

import com.fasterxml.jackson.annotation.JsonProperty

data class CommunicationReferRequest(
    @JsonProperty("gift_id")
    val giftId: Int? = null,

    )
