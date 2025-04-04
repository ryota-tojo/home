package com.example.home.api.budgets.delete.request

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.Size

data class BudgetsDeleteRequest(
    @JsonProperty("groups_id")
    @field:Size(max = 64, message = "キー「groups_id」は64桁以内で入力してください")
    val groupsId: String,

    @JsonProperty("yyyy")
    val yyyy: Int? = null,

    @JsonProperty("mm")
    val mm: Int? = null,

    @JsonProperty("category_id")
    val categoryId: Int? = null
)
