package com.example.home.api.category.update.request

import com.fasterxml.jackson.annotation.JsonProperty

data class CategoryUpdateRequest(
    @JsonProperty("category_id")
    val categoryId: Int,

    @JsonProperty("category_no")
    val categoryNo: Int? = null,

    @JsonProperty("category_name")
    val categoryName: String? = null,

    )
