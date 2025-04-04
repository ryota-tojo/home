package com.example.home.api.budgets.create.request

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.Size

data class BudgetsCreateRequest(
    @JsonProperty("groups_id")
    @field:Size(max = 64, message = "キー「groups_id」は64桁以内で入力してください")
    val groupsId: String,

    @JsonProperty("yyyy")
    val yyyy: Int,

    @JsonProperty("mm")
    val mm: Int,

    @JsonProperty("category_id")
    val categoryId: Int,

    @JsonProperty("amount")
    val amount: Int

)
