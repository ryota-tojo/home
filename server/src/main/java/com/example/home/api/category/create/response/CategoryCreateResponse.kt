package com.example.home.api.category.create.response

import com.fasterxml.jackson.annotation.JsonProperty

data class CategoryCreateResponse(
    @JsonProperty("status")
    val status: String,

    @JsonProperty("message")
    val message: String,

    @JsonProperty("data")
    val data: DataObject

) {
    data class DataObject(
        @JsonProperty("category")
        val category: CategoryObject? = null
    )

    data class CategoryObject(
        @JsonProperty("category_id")
        val categoryId: Int? = null,

        @JsonProperty("groups_id")
        val groupsId: String? = null,

        @JsonProperty("category_no")
        val categoryNo: Int? = null,

        @JsonProperty("category_name")
        val categoryName: String? = null,
    )
}