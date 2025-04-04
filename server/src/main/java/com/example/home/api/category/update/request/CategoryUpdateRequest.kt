package com.example.home.api.category.update.request

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.NotNull

data class CategoryUpdateRequest(
    @JsonProperty("category_id")
    @field:NotNull(message = "キー「category_id」が存在しません")
    val categoryId: Int,

    @JsonProperty("category_no")
    val categoryNo: Int? = null,

    @JsonProperty("category_name")
    val categoryName: String? = null,

    )
