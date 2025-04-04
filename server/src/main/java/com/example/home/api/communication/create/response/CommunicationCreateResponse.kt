package com.example.home.api.communication.create.response

import com.fasterxml.jackson.annotation.JsonProperty

data class CommunicationCreateResponse(
    @JsonProperty("status")
    val status: String,

    @JsonProperty("message")
    val message: String,

    @JsonProperty("data")
    val data: DataObject

) {
    data class DataObject(
        @JsonProperty("communication_list")
        val communicationList: CommunicationObject? = null
    )

    data class CommunicationObject(
        @JsonProperty("gift_id")
        val giftId: Int? = null,

        @JsonProperty("groups_id")
        val groupsId: String? = null,

        @JsonProperty("date")
        val date: String? = null,

        @JsonProperty("from")
        val from: String? = null,

        @JsonProperty("to")
        val to: String? = null,

        @JsonProperty("item")
        val item: String? = null,

        @JsonProperty("amount")
        val amount: Int? = null,

        @JsonProperty("remarks")
        val remarks: String? = null,

        @JsonProperty("rtn_date")
        val rtnDate: String? = null,

        @JsonProperty("rtn_item")
        val rtnItem: String? = null,

        @JsonProperty("rtn_amount")
        val rtnAmount: Int? = null,

        @JsonProperty("rtn_remarks")
        val rtnRemarks: String? = null,

        @JsonProperty("return_flag")
        val returnFlg: Int? = null,

        )
}