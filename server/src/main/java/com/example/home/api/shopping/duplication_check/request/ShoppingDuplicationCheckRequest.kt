package com.example.home.api.shopping.duplication_check.request

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size

data class ShoppingDuplicationCheckRequest(

    @JsonProperty("groups_id")
    @field:Size(max = 64, message = "キー「groups_id」は64桁以内で入力してください")
    val groupsId: String? = null,

    @JsonProperty("user_id")
    val userId: Int? = null,

    @JsonProperty("shopping_date")
    @field:Pattern(regexp = """\d{4}-\d{2}-\d{2}""", message = "キー「shopping_date」はYYYY-MM-DD形式で入力してください")
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
    @field:Size(max = 1024, message = "キー「remarks」は1024桁以内で入力してください")
    val remarks: String? = null,

    @JsonProperty("fixed")
    val fixed: Int? = null
)
