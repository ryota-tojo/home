package com.example.home.api.template.input.refer.response

import com.fasterxml.jackson.annotation.JsonProperty

data class ShoppingInputReferResponse(
    @JsonProperty("status")
    val status: String,

    @JsonProperty("message")
    val message: String,

    @JsonProperty("data")
    val data: DataObject

) {
    data class DataObject(
        @JsonProperty("template_list")
        val templateList: List<TemplateObject>? = null
    )

    data class TemplateObject(
        @JsonProperty("id")
        val id: Int? = null,

        @JsonProperty("groups_id")
        val groupsId: String? = null,

        @JsonProperty("template_id")
        val templateId: String? = null,

        @JsonProperty("template_name")
        val templateName: String? = null,

        @JsonProperty("member_id")
        val memberId: Int? = null,

        @JsonProperty("category_id")
        val categoryId: Int? = null,

        @JsonProperty("type")
        val type: Int? = null,

        @JsonProperty("payment")
        val payment: Int? = null,

        @JsonProperty("settlement")
        val settlement: Int? = null,

        @JsonProperty("amount")
        val amount: Int? = null,

        @JsonProperty("remarks")
        val remarks: String? = null,

        @JsonProperty("use")
        val use: Int? = null,

        @JsonProperty("deleted")
        val deleted: Int? = null
    )

}