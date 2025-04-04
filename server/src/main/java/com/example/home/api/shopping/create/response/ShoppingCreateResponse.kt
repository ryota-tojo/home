package com.example.home.api.shopping.create.response

import com.fasterxml.jackson.annotation.JsonProperty

data class ShoppingCreateResponse(
    @JsonProperty("status")
    val status: String,

    @JsonProperty("message")
    val message: String,

    @JsonProperty("data")
    val data: DataObject

) {
    data class DataObject(
        @JsonProperty("shopping")
        val shopping: ShoppingObject? = null
    )

    data class ShoppingObject(
        @JsonProperty("shopping_id")
        val shoppingId: Int? = null,

        @JsonProperty("groups_id")
        val groupsId: String? = null,

        @JsonProperty("user_id")
        val userId: Int? = null,

        @JsonProperty("shopping_date")
        val shoppingDate: String? = null,

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

        @JsonProperty("fixed")
        val fixed: Int? = null
    )

}