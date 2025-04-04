package com.example.home.api.category.disable.request

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.NotNull

data class CategoryDisableRequest(
    @JsonProperty("category_id")
    @field:NotNull(message = "キー「category_id」が存在しません")
    val categoryId: Int,

    )
