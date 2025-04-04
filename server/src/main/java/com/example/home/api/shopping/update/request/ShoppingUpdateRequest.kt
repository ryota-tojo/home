package com.example.home.api.shopping.update.request

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size

data class ShoppingUpdateRequest(
    @JsonProperty("groups_id")
    @field:NotBlank(message = "キー「groups_id」は必須です")
    @field:Size(max = 64, message = "キー「groups_id」は64桁以内で入力してください")
    val groupsId: String,

    @JsonProperty("user_id")
    @field:NotNull(message = "キー「user_id」は必須です")
    val userId: Int,

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
