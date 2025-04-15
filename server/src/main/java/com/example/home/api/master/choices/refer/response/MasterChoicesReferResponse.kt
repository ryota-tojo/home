package com.example.home.api.master.choices.refer.response

import com.fasterxml.jackson.annotation.JsonProperty

data class MasterChoicesReferResponse(
    @JsonProperty("status")
    val status: String,

    @JsonProperty("message")
    val message: String,

    @JsonProperty("data")
    val data: DataObject

) {
    data class DataObject(
        @JsonProperty("choices_list")
        val choicesList: List<ChoicesObject>? = null
    )

    data class ChoicesObject(
        @JsonProperty("id")
        val id: Int? = null,

        @JsonProperty("item_type")
        val itemType: String? = null,

        @JsonProperty("item_no")
        val itemNo: Int? = null,

        @JsonProperty("item_name_pc")
        val itemNamePC: String? = null,

        @JsonProperty("item_name_sp")
        val itemNameSP: String? = null,
    )

}