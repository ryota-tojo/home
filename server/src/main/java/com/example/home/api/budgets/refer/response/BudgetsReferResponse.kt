package com.example.home.api.budgets.refer.response

import com.fasterxml.jackson.annotation.JsonProperty

data class BudgetsReferResponse(
    @JsonProperty("status")
    val status: String,

    @JsonProperty("message")
    val message: String,

    @JsonProperty("data")
    val data: DataObject

) {
    data class DataObject(
        @JsonProperty("budgets_list")
        val group: List<BudgetsObject>? = null
    )

    data class BudgetsObject(
        @JsonProperty("groups_id")
        val groupsId: String? = null,

        @JsonProperty("yyyy")
        val yyyy: String? = null,

        @JsonProperty("mm")
        val mm: String? = null,

        @JsonProperty("category_id")
        val categoryId: String? = null,

        @JsonProperty("amount")
        val amount: String? = null,
    )
}