package com.example.home.api.category.delete.request

import com.fasterxml.jackson.annotation.JsonProperty

data class CategoryDeleteRequest(
    @JsonProperty("groups_id")
    val groupsId: String,

    @JsonProperty("category_id")
    val categoryId: Int? = null,
)
